# [REST API Server](../hello-okta-api/README.md) Client

Client demonstrates Server-to-Server _Client Credentials_ integration flow which consists of 2 steps:

- Get `access_token` from authorization server
- Call protected resource using this token

## Running Demo Locally

### IntelliJ Gradle Run Configuration

![IntelliJ Gradle Run Configuration](images/01-Gradle-Run-Configuration.PNG)

Main class `lv.gennadyyonov.hellookta.api.client.HelloClientDemo` in main sources.
In order to run it the following properties should be checked and adjusted:

- **Proxy Settings**
- **Okta Client Application Properties**

### Proxy Settings

- Proxy Settings can be chnged in [`gradle.properties`](../gradle.properties) file. 
- IdP server should not fall into `nonProxyHosts`
- Application server should fall into `nonProxyHosts`.
- If no proxy exists leave these properties as is, i.e. empty. 

### Okta Client Application Properties

Okta related settings are located in [`demo.properties`](src/main/resources/demo.properties).

# Demo Implicit Flow Client

Client demonstrates usage of implicit flow which consists of 3 steps:
* [Primary authentication](https://developer.okta.com/docs/reference/api/authn/#primary-authentication) to get `sessionToken`
* [Use the Implicit Flow](https://developer.okta.com/docs/guides/implement-implicit/use-flow/) to get access token
* Call protected resource using this token

## Primary authentication

```
curl -v -X POST \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-d '{
  "username": "${username}",
  "password": "${password}",
  "options": {
    "multiOptionalFactorEnroll": false,
    "warnBeforePasswordExpired": false
  }
}' "https://${orgUrl}/api/v1/authn"
```
If everything is OK, the application will receive back an session token:

```
{
    "status": "SUCCESS",
    "sessionToken": "$(sessionToken}",
    ...
}
```

## Get Access Token

```
curl -X GET ${okta_oauth2_issuer}/v1/authorize?client_id=${okta.oauth2.clientId}&response_type=token&scope=email+profile+openid&redirect_uri=${serverBaseUri}/implicit/callback&state=foo&nonce=bar&sessionToken=${sessionToken}
```

HTTP 302 response code with `Location` header containing `access_token` will be received:

```
Location: ${serverBaseUri}/implicit/callback/#access_token=${access_token}&token_type=Bearer&expires_in=3600&scope=email+profile+openid&state=foo&nonce=bar
```

## Call Protected Resource

```
Authorization: Bearer ${access_token}
```

## Running Demo Locally

Main class `lv.gennadyyonov.hellookta.api.client.ImplicitFlowHelloClientDemo` in main sources.
In order to run it the following properties should be checked and adjusted:
* Proxy Settings
* Okta User Properties

### Okta User Properties

Okta related settings are located in `implicitFlow.properties`. Currently, only `password` is missing to run demo successfully.

# Demo Authorization Code Flow with PKCE Client

Client demonstrates usage of implicit flow which consists of the following steps:
* [Primary authentication](https://developer.okta.com/docs/reference/api/authn/#primary-authentication) to get `sessionToken`
* [Use the Authorization Code Flow with PKCE](https://developer.okta.com/docs/guides/implement-auth-code-pkce/use-flow/) to get access token
* Call protected resource using this token

## Running Demo Locally

Main class `lv.gennadyyonov.hellookta.api.client.PkceHelloClientDemo` in main sources.
In order to run it the following properties should be checked and adjusted:
* Proxy Settings
* Okta User Properties

### Okta User Properties

Okta related settings are located in `pkce.properties`. Currently, only `password` is missing to run demo successfully.
