# healthcheck

[![Tests](https://github.com/philiprehberger/kt-healthcheck/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-healthcheck/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/healthcheck.svg)](https://central.sonatype.com/artifact/com.philiprehberger/healthcheck)
[![Last updated](https://img.shields.io/github/last-commit/philiprehberger/kt-healthcheck)](https://github.com/philiprehberger/kt-healthcheck/commits/main)

Composable health check framework with readiness and liveness probes.

## Installation

### Gradle (Kotlin DSL)

```kotlin
implementation("com.philiprehberger:healthcheck:0.1.6")
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>healthcheck</artifactId>
    <version>0.1.6</version>
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

## Support

If you find this project useful:

⭐ [Star the repo](https://github.com/philiprehberger/kt-healthcheck)

🐛 [Report issues](https://github.com/philiprehberger/kt-healthcheck/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

💡 [Suggest features](https://github.com/philiprehberger/kt-healthcheck/issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement)

❤️ [Sponsor development](https://github.com/sponsors/philiprehberger)

🌐 [All Open Source Projects](https://philiprehberger.com/open-source-packages)

💻 [GitHub Profile](https://github.com/philiprehberger)

🔗 [LinkedIn Profile](https://www.linkedin.com/in/philiprehberger)

## License

[MIT](LICENSE)
