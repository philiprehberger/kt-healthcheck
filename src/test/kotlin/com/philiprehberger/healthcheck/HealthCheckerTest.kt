package com.philiprehberger.healthcheck

import kotlinx.coroutines.test.runTest
import kotlin.test.*

class HealthCheckerTest {
    @Test fun `all pass`() = runTest {
        val hc = healthCheck { check("db") { true }; check("redis") { true } }
        val r = hc.check()
        assertEquals(HealthStatus.UP, r.status)
        assertTrue(r.isHealthy())
    }
    @Test fun `critical failure`() = runTest {
        val hc = healthCheck { check("db") { false }; check("redis") { true } }
        val r = hc.check()
        assertEquals(HealthStatus.DOWN, r.status)
    }
    @Test fun `non-critical failure`() = runTest {
        val hc = healthCheck { check("db") { true }; check("cache", critical = false) { false } }
        val r = hc.check()
        assertEquals(HealthStatus.UP, r.status)
    }
    @Test fun `json output`() = runTest {
        val hc = healthCheck { check("db") { true } }
        val json = hc.check().toJson()
        assertTrue(json.contains(""status":"UP""))
    }
}
