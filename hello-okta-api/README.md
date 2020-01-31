# Run Application on localhost

One of the easiest options to run application locally is using IntelliJ Gradle Run Configuration.

## IntelliJ Gradle Run Configuration

![IntelliJ Gradle Run Configuration](images/01-Gradle-Run-Configuration.PNG)

This option uses `build.gradle` and adds current module `src/localhost` sources and resources to classpath.

So, configuration properties can be changed the following way:
- **Corporate Proxy Settings** 
    - [`gradle.properties`](../gradle.properties) file. If no proxy exists leave these properties as is, i.e. empty.
- **Application Properties**
    - _Environment variables_ - mostly Okta related can be changed in [localhost.properties](src/localhost/resources/localhost.properties) file.
    - _Spring Boot Application properties_ in [application-localhost.yml](src/localhost/resources/application-localhost.yml) file.

