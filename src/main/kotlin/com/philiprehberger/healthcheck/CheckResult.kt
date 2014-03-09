package com.philiprehberger.healthcheck

import kotlin.time.Duration

/** Result of a single health check. */
public data class CheckResult(
    public val name: String,
    public val status: HealthStatus,
    public val duration: Duration,
    public val error: String? = null,
)
