package com.cotiq.coinbase.auth

import com.nimbusds.jose.JOSEObjectType.JWT
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jwt.JWTClaimNames.EXPIRATION_TIME
import com.nimbusds.jwt.JWTClaimNames.ISSUER
import com.nimbusds.jwt.JWTClaimNames.NOT_BEFORE
import com.nimbusds.jwt.JWTClaimNames.SUBJECT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import java.io.StringReader
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Security
import java.security.interfaces.ECPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant

class JwtCreator(
    private val keyName:
    String, privateKey: String
) {

    private val jwsSigner: JWSSigner

    init {
        check(keyName.isNotBlank()) { "keyName must not be blank" }
        check(privateKey.isNotBlank()) { "privateKey must not be blank" }
        jwsSigner = createJWSSigner(privateKey)
    }

    fun createJwt(uri: String? = null): String {
        val timeNow = Instant.now().epochSecond
        val jwsHeader = JWSHeader.Builder(JWSAlgorithm.ES256)
            .type(JWT)
            .keyID(keyName)
            .customParam("nonce", timeNow.toString())
            .build()
        val claimsSet = JWTClaimsSet.Builder()
            .claim(ISSUER, "cdp")
            .claim(NOT_BEFORE, timeNow)
            .claim(EXPIRATION_TIME, timeNow + TTL_SECONDS)
            .claim(SUBJECT, keyName)
            .apply { uri?.let { claim("uri", it) } }
            .build()
        return SignedJWT(jwsHeader, claimsSet)
            .apply { sign(jwsSigner) }
            .serialize()
    }

    private fun createJWSSigner(privateKeyString: String): JWSSigner {
        val pemParser = PEMParser(StringReader(privateKeyString))
        val converter = JcaPEMKeyConverter().setProvider("BC")
        val privateKey = when (val obj = pemParser.readObject()) {
            is PrivateKey -> obj
            is PEMKeyPair -> converter.getPrivateKey(obj.privateKeyInfo)
            else -> throw RuntimeException("Unexpected private key format")
        }
        pemParser.close()

        val keyFactory: KeyFactory = KeyFactory.getInstance("EC")
        val keySpec = PKCS8EncodedKeySpec(privateKey.encoded)
        val ecPrivateKey = keyFactory.generatePrivate(keySpec) as ECPrivateKey
        return ECDSASigner(ecPrivateKey)
    }

    companion object {
        private const val TTL_SECONDS = 120

        init {
            Security.addProvider(BouncyCastleProvider())
        }
    }
}