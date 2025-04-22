# Spring Security OAuth2 Demo Applications for Okta

[![Java CI with Gradle](https://github.com/gennadyyonov/hello-okta/actions/workflows/gradle.yml/badge.svg)](https://github.com/gennadyyonov/hello-okta/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/gennadyyonov/hello-okta/graph/badge.svg)](https://codecov.io/gh/gennadyyonov/hello-okta)
[![codecov-bff](https://codecov.io/gh/gennadyyonov/hello-okta/graph/badge.svg?flag=bff)](https://codecov.io/gh/gennadyyonov/hello-okta?flag=bff)
[![codecov-api](https://codecov.io/gh/gennadyyonov/hello-okta/graph/badge.svg?flag=api)](https://codecov.io/gh/gennadyyonov/hello-okta?flag=api)

This repository contains demo applications that demonstrates how to integrate the following OAuth 2.0 flows into your [Spring Boot](https://projects.spring.io/spring-boot/) Application:

- [Authorization Code](https://developer.okta.com/docs/guides/implement-auth-code/overview/) Flow
- [Client Credentials](https://developer.okta.com/docs/guides/implement-client-creds/overview/) Flow

SPA Demo to show [Authorization Code Flow with PKCE](https://developer.okta.com/docs/guides/implement-auth-code-pkce/overview/) can be found [here](https://github.com/gennadyyonov/hello-okta-spa)

## Required Software

### Java

- JDK 21

### Lombok

#### IntelliJ 

- Download and install Lombok [plugin](https://plugins.jetbrains.com/plugin/6317-lombok-plugin)
- Enable Annotation Processors
  -  Go to **Setting > Build, Execution, Deployment > Compiler > Annotation Processors**
  -  Check _Enable annotation processing_
  
### Build/Deployment Automation Software

[Gradle](https://gradle.org/) is used as a build automation tool. 
Several major IDEs allow you to import Gradle builds and interact with them.
IntelliJ IDEA supports a fully-functional integration with Gradle.

[Docker](https://www.docker.com/) is a software platform that allows us to build, test, and deploy applications quickly. 

## Build Project

`$ gradlew clean build`

## Modules

This project contains several modules, here are the main ones you to focus on and run:

| MODULE                                                   | DESCRIPTION                                                                                                                                                                                                  |
|----------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [hello-okta-api](hello-okta-api/README.md)               | REST API Backend Server.<br> Exposes domain API over REST.<br>Secured by Okta.                                                                                                                               |
| [hello-okta-bff](hello-okta-bff/README.md)               | BFF (Backend for Frontend) Server.<br>Aggregates data from downstream services (for example [hello-okta-api](hello-okta-api/README.md)) providing API tailored to Front-end (SPA) needs.<br>Secured by Okta. |
| [hello-okta-api-client](hello-okta-api-client/README.md) | [REST API Server](hello-okta-api/README.md) Client.                                                                                                                                                          |

Other modules contains shared source code.

### Dependencies

* API module:

`$ gradlew hello-okta-api:dependencies > api-dependencies.txt`

* BFF module:

`$ gradlew hello-okta-bff:dependencies > bff-dependencies.txt`

## Run Modules

There are 2 runnable modules in this project:

- [hello-okta-api](hello-okta-api/README.md)
- [hello-okta-bff](hello-okta-bff/README.md)

Each of them contains instructions how to **Run Application on localhost** in `README.md` file.

**Note**, both of them should be up and running to see the demo.

## Tech Stack
- [Spring Boot](https://projects.spring.io/spring-boot/) : Application framework
- [Lombok](https://projectlombok.org/features/index.html) : Utility library for Java language
- [GraphQL](http://graphql.org/learn/) : API query runtime
  - [Spring for GraphQL](https://spring.io/projects/spring-graphql)
- [Feign](https://github.com/OpenFeign/feign) : Declarative REST Client
- [WireMock](https://github.com/tomakehurst/wiremock) : Simulator for HTTP-based APIs
- [Springdoc OpenAPI 3.0](https://github.com/springdoc/springdoc-openapi) : Spring Boot RESTful API Documentation
- [Checkstyle](https://checkstyle.sourceforge.io/index.html)
- [Helm](https://helm.sh) : Package manager for Kubernetes

## Okta Configuration

Head on over to [Set up your Okta org](https://developer.okta.com/docs/guides/oie-embedded-common-org-setup/java/main/) to create a free developer account. 
Look for the email to complete the initialization of your Okta org. 

### Set Up OpenID Connect Application

- Login to your Okta account.
- Navigate to **Applications** in the admin console and click: **Create App Integration**.
- Select the following values:

| FIELD NAME           | VALUE                     |
|----------------------|---------------------------|
| **Sign-in method**   | `OIDC - OpenID Connect`   |
| **Application type** | `Single-Page Application` |

- Click **Next**.

- Populate the fields with the following values:

| SECTION              | FIELD NAME               | VALUE                                                                                                                                                                                                                                                                                                                                                                         |
|----------------------|--------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **General Settings** | **App integration name** | `Hello Okta App`                                                                                                                                                                                                                                                                                                                                                              |
| **General Settings** | **Grant type**           | `Authorization Code`, `Refresh Token`                                                                                                                                                                                                                                                                                                                                         |
| **General Settings** | **Login redirect URIs**  | http://localhost:8060/bff/swagger-ui/oauth2-redirect.html<br>http://localhost:8070/api/swagger-ui/oauth2-redirect.html<br>http://localhost:3000/implicit/callback<br>https://kubernetes.docker.internal/bff/swagger-ui/oauth2-redirect.html<br>https://kubernetes.docker.internal/api/swagger-ui/oauth2-redirect.html<br>https://kubernetes.docker.internal/implicit/callback |
| **General Settings** | **Logout redirect URIs** | http://localhost:3000<br>https://kubernetes.docker.internal                                                                                                                                                                                                                                                                                                                   |
| **Trusted Origins**  | **Base URIs**            | http://localhost:3000<br>https://kubernetes.docker.internal                                                                                                                                                                                                                                                                                                                   |
| **Assignments**      | **Controlled access**    | `Skip group assignment for now`                                                                                                                                                                                                                                                                                                                                               |

- Click **Save**. **General Settings** tab will be displayed
- Scroll to the **Client Credentials** section and copy the `Client ID`. This value will be used by our app.
- Make sure that:
  * **Initiate login URI** is empty
  * **Use PKCE (for public clients)** radio button is selected as **Client authentication** in **Client Credentials** section.

### Set Up Client Application

This Application will be used for Server-to-Server Communication between Client and Server application back-ends using [Client Credentials](https://developer.okta.com/docs/guides/implement-client-creds/overview/) authorization flow.

- Navigate to **Applications** in the admin console and click: **Create App Integration**.
- Select the following values:

| FIELD NAME         | VALUE          |
|--------------------|----------------|
| **Sign-in method** | `API Services` |

- Click **Next**.
- Populate the fields with the following values:

| FIELD NAME               | VALUE                   |
|--------------------------|-------------------------|
| **App integration name** | `Hello Okta App Client` |

- Click **Save**. **General Settings** tab will be displayed
- **IMPORTANT!** Ensure **Proof of possession** _Require Demonstrating Proof of Possession (DPoP) header in token requests_ checkbox is unchecked.
- Scroll down to the **Client Credentials** section and copy the `Client ID` and `Client Secret`. These values will be used by our app.

### Set Up Authorization Server

Navigate to **Security > API > Authorization Servers**. Click **Add Authorization Server**. 
- Fill in the values:

| FIELD NAME      | VALUE             |
|-----------------|-------------------|
| **Name**        | `Hello Okta App`  |
| **Description** | `Hello Okta App`  |
| **Audience**    | `api://hellookta` |

- Click **Save**. **Settings** tab will be displayed
- Return to **Authorization Servers** and copy the `Issuer URI`. This value will be used by our app.

#### Scopes

Create a [custom scope](https://help.okta.com/en-us/content/topics/security/api-config-scopes.htm) for our consumer application to restrict access token to this example.

- From the menu bar select **API > Authorization Servers**. 
- Edit the authorization server created in the previous step by clicking on the edit pencil, then click **Scopes > Add Scope**. 
- Fill in the values:

| FIELD NAME         | VALUE                     |
|--------------------|---------------------------|
| **Name**           | `message.read`            |
| **Display phrase** | `Read messages`           |
| **Description**    | `Allows to read messages` |
| **User consent**   | `Implicit`                |

- Press **Create**.

#### Claims

To include custom claims in an _access token_, they should be added to our Custom Authorization Server.

Let's add a **Groups** claim to _access tokens_ to perform authentication and authorization using the out Custom Authorization Server.

##### Access Token groups Claim

- Click **Claims > Add Claim**. 
- Fill in the fields with these values (leave those not mentioned as their defaults):

| FIELD NAME                | VALUE                      |
|---------------------------|----------------------------|
| **Name**                  | `groups`                   |
| **Include in token type** | `Access Token`<br>`Always` |
| **Value type**            | `Groups`                   |
| **Filter**                | `Starts with` `HelloOkta_` |
| **Include in**            | `Any scope`                |

- Click **Create**.

##### Access Token given_name, family_name and email Claim

- Click **Claims > Add Claim**.

| FIELD NAME                | VALUE                      |
|---------------------------|----------------------------|
| **Include in token type** | `Access Token`<br>`Always` |
| **Value type**            | `Expression`               |

- For each claim fill in the fields with the following values:

| **Name**      | **Value**        | **Include in** |
|---------------|------------------|----------------|
| `given_name`  | `user.firstName` | `profile`      |
| `family_name` | `user.lastName`  | `profile`      |
| `email`       | `user.email`     | `email`        |

- Click **Create**.

#### Access Policies

Okta **Access Policies** allows to restrict access to application resources.

Each **Access Policy** applies to a particular OpenID Connect application. 

**Access Policies** are containers for rules.

**Rules** define different access depending on the nature of the token request.

##### Access Policy for Hello Okta App

- Click the **Access Policies** tab. Click **Add Policy**. 
- Fill in the fields with these values:

| FIELD NAME      | VALUE                                   |
|-----------------|-----------------------------------------|
| **Name**        | `Hello Okta App`                        |
| **Description** | `Hello Okta App`                        |
| **Assign to**   | The following clients: `Hello Okta App` |

- Click **Create Policy**.

###### Hello Okta App Access Policy Rules

- Click **Add Rule**. Fill in the fields with these values:

| FIELD NAME                            | VALUE                                       |
|---------------------------------------|---------------------------------------------|
| **Name**                              | `All users of Hello Okta App has access`    |
| **Grant type is**                     | `Authorization Code`                        |
| **User is**                           | `Any user assigned the app`                 |
| **Scopes requested**                  | The following scopes: `OIDC default scopes` |
| **Use this inline hook**              | `None (disabled)`                           |
| **Access token lifetime is**          | `15 Minutes`                                |
| **Refresh token lifetime is**         | `2 Days`                                    |
| **but will expire if not used every** | `1 Hours`                                   |

- Click **Create Rule**.

##### Access Policy for Hello Okta Client App

- Click the **Access Policies** tab. Click **Add Policy**. 
- Fill in the fields with these values:

| FIELD NAME      | VALUE                                          |
|-----------------|------------------------------------------------|
| **Name**        | `Hello Okta App Client`                        |
| **Description** | `Hello Okta App Client`                        |
| **Assign to**   | The following clients: `Hello Okta App Client` |

- Click **Create Policy**.

###### Hello Okta App Client Access Policy Rules

- Click **Add Rule**. 
- Fill in the fields with these values:

| FIELD NAME                            | VALUE                                |
|---------------------------------------|--------------------------------------|
| **Name**                              | `Message read`                       |
| **Grant type is**                     | `Client Credentials`                 |
| **User is**                           | `Any user assigned the app`          |
| **Scopes requested**                  | The following scopes: `message.read` |
| **Use this inline hook**              | `None (disabled)`                    |
| **Access token lifetime is**          | `15 Minutes`                         |
| **Refresh token lifetime is**         | `2 Days`                             |
| **but will expire if not used every** | `1 Hours`                            |

- Click **Create Rule**.

### Set Up Okta Group

#### Add HelloOkta_StandardUser Group
- Navigate to **Directory > Groups**. Click **Add Group**. 
- Fill in the values:

| FIELD NAME            | VALUE                     |
|-----------------------|---------------------------|
| **Name**              | `HelloOkta_StandardUser`  |
| **Group Description** | `HELLOOKTA Standard User` |

- Click **Add Group**.

#### Add User to HelloOkta_StandardUser Group

- Navigate to **Directory > Groups**.
- Click on the **HelloOkta_StandardUser** group. 
- Click on the **Manage People** button. 
- Use the search box to find your user and add yourself to the group.
- Click **Save** button

#### Setup Hello Okta App Application Group Assignments

- Navigate to **Applications > Applications**.
- Click on the **Hello Okta App**. 
- Navigate to the **Assignments** tab. 
- Click **Assign** split button and select **Assign to Groups**, **Assign Hello Okta App to Groups** dialog will appear.
- Click **Assign** next to **HelloOkta_StandardUser**
- Click **Done**

### Ensure Trusted Origins

- Navigate to **API > Trusted Origins**.
- Ensure the following 2 Origins with both `CORS` and `Redirect` present:
    - http://localhost:3000
    - https://kubernetes.docker.internal

## Docker

### Spring Profile Configuration

**API**
- Copy [`.env.dev.sample`](hello-okta-api/env/.env.dev.sample) to `.env.dev` under `hello-okta-api/env`
- Fill in your configuration properties instead of `???`

**BFF**
- Copy [`.env.dev.sample`](hello-okta-bff/env/.env.dev.sample) to `.env.dev` under `hello-okta-bff/env`
- Fill in your configuration properties instead of `???`

To [build](https://docs.docker.com/compose/reference/build/) API and BFF images:
```
docker-compose build api
docker-compose build bff
```
**Note**, `app.jar` should be built first from the parent directory using the following command:
```
gradlew clean build
```
To [bring up](https://docs.docker.com/compose/reference/up/) API and BFF services:
```
docker-compose up api
docker-compose up bff
```
To bring up all the services:
```
docker-compose up -d
```

See [README.md](./helm/README.md) to deploy application to Kubernetes Cluster (Docker Desktop)