# REST API Backend Server Application

## Run Application on localhost

One of the easiest options to run application locally is using IntelliJ Gradle Run Configuration.

### IntelliJ Gradle Run Configuration

![IntelliJ Gradle Run Configuration](images/01-Gradle-Run-Configuration.PNG)

This option uses `build.gradle`.

So, configuration properties can be changed the following way:
- **Corporate Proxy Settings** 
    - [`gradle.properties`](../gradle.properties) file. If no proxy exists leave these properties as is, i.e. empty.
- **Application Properties**
    - _Environment variables_ - `spring.profiles.active=localhost,actuator`.
    - _Localhost Spring Profile Configuration_
      - Copy [`secrets.yml.sample`](src/main/resources/secrets.yml.sample) to `secrets.yml` under `src/main/resources`
      - Fill in your Okta related configuration instead of `???`

Application will be running on port `8070` by default.
 
It's API can be accessed via [Swagger UI](http://localhost:8070/api/swagger-ui.html) in your browser.

![Swagger UI](images/02-OpenApi-UI.PNG)
