# [Spring Boot Actuator: Production-ready Features](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html)

## Endpoints

The following endpoint enabled in this module by default:

- `info`
- `health`
- `metrics`
- `auditevents`
- `httptrace`

| ID | Description |
| --- | --- |
| [info](http://localhost:8070/actuator/info) | Displays arbitrary application information. |
| [health](http://localhost:8070/actuator/health) | Shows application health information. |
| [metrics](http://localhost:8070/actuator/metrics) | Shows 'metrics' information for the current application. |
| [auditevents](http://localhost:8070/actuator/auditevents) | Exposes audit events information for the current application. Requires an `AuditEventRepository` bean. |
| [httptrace](http://localhost:8070/actuator/httptrace) | Displays HTTP trace information (by default, the last 100 HTTP request-response exchanges). Requires an `HttpTraceRepository` bean. |

## Application Information

- Auto-configured `InfoContributors`.
- Custom Application Information:
    - `info.*` Spring [application properties](./src/main/resources/application-actuator.yml).
    - Custom written `InfoContributor`-s.