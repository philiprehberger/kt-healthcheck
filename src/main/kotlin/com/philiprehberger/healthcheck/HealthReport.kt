package com.philiprehberger.healthcheck

import kotlin.time.Duration

/** Aggregated health report. */
public data class HealthReport(
    public val status: HealthStatus,
    public val checks: List<CheckResult>,
    public val duration: Duration,
) {
    public fun isHealthy(): Boolean = status == HealthStatus.UP
    public fun toJson(): String = buildString {
        append("{"status":"$status","checks":[")
        append(checks.joinToString(",") { "{"name":"${it.name}","status":"${it.status}","duration":"${it.duration}"${if (it.error != null) ","error":"${it.error}"" else ""}}" })
        append("],"duration":"$duration"}")
    }
}
