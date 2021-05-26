# REST API Backend Server Application

## Run Application on localhost

One of the easiest options to run application locally is using IntelliJ Gradle Run Configuration.

### IntelliJ Gradle Run Configuration

![IntelliJ Gradle Run Configuration](images/01-Gradle-Run-Configuration.PNG)

This option uses `build.gradle` and sets localhost environment variables.

So, configuration properties can be changed the following way:
- **Corporate Proxy Settings** 
    - [`gradle.properties`](../gradle.properties) file. If no proxy exists leave these properties as is, i.e. empty.
- **Application Properties**
    - _Environment variables_ - mostly Okta related can be changed in [.env.localhost](env/.env.localhost) file.

Application will be running on port `8070` by default.
 
It's API can be accessed via [Swagger UI](http://localhost:8070/swagger-ui.html) in your browser.

![Swagger UI](images/02-OpenApi-UI.PNG)
