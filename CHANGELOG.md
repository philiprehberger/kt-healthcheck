# Changelog

## [0.1.2] - 2026-03-18

- Fix JSON string escaping in HealthReport.toJson()

## [0.1.1] - 2026-03-18

- Upgrade to Kotlin 2.0.21 and Gradle 8.12
- Enable explicitApi() for stricter public API surface
- Add issueManagement to POM metadata

## [0.1.0] - 2026-03-18

### Added

- `healthCheck {}` DSL for defining health checks

- Parallel check execution with per-check timeout

- Critical vs non-critical checks

- `HealthReport` with JSON output

- `HealthStatus.UP` / `DOWN`
