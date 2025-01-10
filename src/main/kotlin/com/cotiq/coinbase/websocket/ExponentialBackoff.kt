package com.cotiq.coinbase.websocket

import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.pow
import kotlin.time.Duration

/**
 * Provides an exponentially increasing delay mechanism for retry attempts.
 *
 * This can be used, for example, to delay reconnect attempts to a remote service.
 *
 * @property baseDelay The initial delay used for the first attempt.
 * @property maxDelay  The maximum allowable delay. Once the exponential calculation exceeds this value, this limit is used.
 * @property maxTries  The maximum number of attempts allowed before further retries are disallowed.
 */
internal class ExponentialBackoff(
    private val baseDelay: Duration,
    private val maxDelay: Duration,
    private val maxTries: Int = Int.MAX_VALUE
) {

    private val tries = AtomicInteger(0)

    /**
     * Checks if there is a remaining attempt.
     *
     * @return `true` if the current number of attempts is less than [maxTries], `false` otherwise.
     */
    val hasNext: Boolean
        get() = tries.get() < maxTries

    /**
     * Resets the number of tries to zero.
     *
     * Use this if you need to start fresh
     * (e.g., after a successful connection or a user action).
     */
    fun reset() {
        tries.set(0)
    }

    /**
     * Suspends the current coroutine with an exponentially increasing delay.
     *
     * - If there are no remaining attempts ([hasNext] is `false`), this function throws an [IllegalStateException].
     * - On each call, the delay is `baseDelay * 2^attemptCount`, but it won't exceed [maxDelay].
     * - The number of attempts is incremented after each call, so subsequent calls will have a longer delay (until [maxTries] is reached).
     *
     * @throws IllegalStateException If [hasNext] is `false` (no more retries left).
     */
    suspend fun delayNext() {
        if (!hasNext) error("No more retries left")
        val attempt = tries.getAndIncrement()
        val delayDuration = baseDelay * 2.0.pow(attempt)
        delay(minOf(delayDuration, maxDelay))
    }
}