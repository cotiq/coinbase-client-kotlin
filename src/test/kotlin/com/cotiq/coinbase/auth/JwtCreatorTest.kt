package com.cotiq.coinbase.auth

import com.nimbusds.jwt.SignedJWT
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JwtCreatorTest {

    private val jwtCreator = JwtCreator(
        keyName = "testApiKey",
        privateKey = """
            -----BEGIN EC PRIVATE KEY-----
            MHcCAQEEIN/37NFyCvL7brp4zljP83sNj1PvtFsp8dMR86EDwLZUoAoGCCqGSM49
            AwEHoUQDQgAEK0acP7Ml6fgKy35YE7JGVP7AmNy7oJ6gl4QIqiwiSExbr4iDPfxT
            81550HxXoiQiBJXBJxhgXYpIcJVmFGk20w==
            -----END EC PRIVATE KEY-----
            """.trimIndent()
    )

    @Test
    fun `test createJwt generates valid JWT`() {

        val uri = "GET /test/uri"
        val jwt = jwtCreator.createJwt(uri)

        val signedJWT = SignedJWT.parse(jwt)
        val claims = signedJWT.jwtClaimsSet

        assertEquals("testApiKey", claims.subject)
        assertEquals("cdp", claims.issuer)
        assertEquals(uri, claims.getStringClaim("uri"))
        assertTrue(claims.notBeforeTime.toInstant().isBefore(Instant.now()))
        assertTrue(claims.expirationTime.toInstant().isAfter(Instant.now()))
    }
}