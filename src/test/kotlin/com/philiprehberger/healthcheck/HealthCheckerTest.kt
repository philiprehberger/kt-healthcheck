package com.philiprehberger.healthcheck

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        assertEquals(HealthStatus.DEGRADED, r.status)
    }
    @Test fun `json output`() = runTest {
        val hc = healthCheck { check("db") { true } }
        val json = hc.check().toJson()
        assertTrue(json.contains("\"status\":\"UP\""))
    }

    @Test
    fun `degraded status when non-critical check fails`() = runBlocking {
        val hc = healthCheck {
            check("db", critical = true) { true }
            check("cache", critical = false) { false }
        }
        val report = hc.check()
        assertEquals(HealthStatus.DEGRADED, report.status)
    }

    @Test
    fun `down status when critical check fails`() = runBlocking {
        val hc = healthCheck {
            check("db", critical = true) { false }
            check("cache", critical = false) { true }
        }
        val report = hc.check()
        assertEquals(HealthStatus.DOWN, report.status)
    }

    @Test
    fun `toMap returns map representation`() = runBlocking {
        val hc = healthCheck {
            check("db") { true }
        }
        val report = hc.check()
        val map = report.toMap()
        assertEquals("UP", map["status"])
        assertTrue(map["checks"] is List<*>)
    }

    @Test
    fun `toJson includes DEGRADED status`() = runBlocking {
        val hc = healthCheck {
            check("db", critical = true) { true }
            check("cache", critical = false) { false }
        }
        val report = hc.check()
        val json = report.toJson()
        assertTrue(json.contains("DEGRADED"))
    }
}
