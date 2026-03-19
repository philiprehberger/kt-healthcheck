# healthcheck

[![CI](https://github.com/philiprehberger/kt-healthcheck/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-healthcheck/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/healthcheck)](https://central.sonatype.com/artifact/com.philiprehberger/healthcheck)

Composable health check framework with readiness and liveness probes.

## Requirements

- Kotlin 1.9+ / Java 17+

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.philiprehberger:healthcheck:0.1.0")
}
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>healthcheck</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

```kotlin
import com.philiprehberger.healthcheck.*

val health = healthCheck {
    check("database") { dataSource.connection.use { it.isValid(2) } }
    check("cache", critical = false) { redis.ping() == "PONG" }
}

val report = health.check()
report.status      // UP
report.isHealthy() // true
report.toJson()    // {"status":"UP",...}
```

## API

| Function / Class | Description |
|------------------|-------------|
| `healthCheck { }` | Build a health checker |
| `check(name, critical, timeout) { Boolean }` | Define a health check |
| `HealthChecker.check()` | Run all checks, return HealthReport |
| `HealthReport.isHealthy()` | Check overall status |
| `HealthReport.toJson()` | JSON output |
| `HealthStatus` | UP or DOWN |

## Development

```bash
./gradlew test
./gradlew build
```

## License

MIT
