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
        append("{\"status\":\"")
        append(status)
        append("\",\"checks\":[")
        append(checks.joinToString(",") { c ->
            buildString {
                append("{\"name\":\"")
                append(c.name)
                append("\",\"status\":\"")
                append(c.status)
                append("\",\"duration\":\"")
                append(c.duration)
                append("\"")
                if (c.error != null) {
                    append(",\"error\":\"")
                    append(c.error)
                    append("\"")
                }
                append("}")
            }
        })
        append("],\"duration\":\"")
        append(duration)
        append("\"}")
    }
}
