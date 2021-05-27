# BFF (Backend for Frontend) Server Application

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
    - Fill in your configuration properties instead of `???`

Application will be running on port `8060` by default.
 
It's API can be accessed via [GraphiQL](http://localhost:8060/graphiql) in your browser.

**Note** that in order to use it you need to be logged either into SPA application or BFF, authorization header then will be passed to graphql calls to server from graphiql.

In order to do it in BFF, [index page](http://localhost:8060/) can be used. If you see Okta hosted sign-in page then things are working!

You can login with the same account that you created when signing up for your Developer Org, or you can use a known username and password from your Okta Directory.

![GraphiQL](images/02-GraphiQL.PNG)
