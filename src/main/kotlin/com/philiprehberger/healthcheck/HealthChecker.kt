package com.philiprehberger.healthcheck

import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTimedValue

/** Build a health checker. */
public fun healthCheck(block: HealthCheckBuilder.() -> Unit): HealthChecker {
    val b = HealthCheckBuilder()
    b.block()
    return HealthChecker(b.checks)
}

public class HealthCheckBuilder {
    internal val checks = mutableListOf<CheckDef>()
    /** Define a health check. */
    public fun check(name: String, critical: Boolean = true, timeout: Duration = 5.seconds, block: suspend () -> Boolean) {
        checks.add(CheckDef(name, critical, timeout, block))
    }
}

internal data class CheckDef(val name: String, val critical: Boolean, val timeout: Duration, val block: suspend () -> Boolean)

/** Executes health checks and produces a report. */
public class HealthChecker internal constructor(private val checks: List<CheckDef>) {
    /** Run all checks in parallel. */
    public suspend fun check(): HealthReport {
        val (results, totalDuration) = measureTimedValue {
            coroutineScope {
                checks.map { def ->
                    async {
                        val (result, dur) = measureTimedValue {
                            try { withTimeout(def.timeout) { if (def.block()) HealthStatus.UP to null else HealthStatus.DOWN to "check returned false" } }
                            catch (e: Exception) { HealthStatus.DOWN to e.message }
                        }
                        CheckResult(def.name, result.first, dur, result.second)
                    }
                }.awaitAll()
            }
        }
        val criticalDown = results.any { r -> checks.first { it.name == r.name }.critical && r.status == HealthStatus.DOWN }
        val overall = if (criticalDown) HealthStatus.DOWN else HealthStatus.UP
        return HealthReport(overall, results, totalDuration)
    }
}
