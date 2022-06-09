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

    /**
     * Returns the health report as a [Map] for flexible serialization.
     *
     * @return a map representation of the report
     */
    public fun toMap(): Map<String, Any?> {
        return mapOf(
            "status" to status.name,
            "checks" to checks.map { c ->
                mapOf(
                    "name" to c.name,
                    "status" to c.status.name,
                    "duration" to c.duration.toString(),
                    "error" to c.error,
                )
            },
            "duration" to duration.toString(),
        )
    }
}
