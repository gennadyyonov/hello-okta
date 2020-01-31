# hello-okta

## Okta Configuration

### Set Up OpenID Connect Application

Login to your Okta account.
Navigate to **Applications** in the admin console and click: **Add Application**. 
Choose _Web_ and click **Next**.

Populate the fields with these values:

| FIELD NAME | VALUE |
| --- | ---|
| **Name** | `Hello Okta App` |
| **Base URIs** | http://localhost:8060<br>http://localhost:8070 |
| **Login redirect URIs** | http://localhost:8060/login/oauth2/code/okta<br>http://localhost:8070/login/oauth2/code/okta |
| **Allowed grant types** | `Authorization Code` |

Click **Done**.

**General Settings** tab will be displayed:

![Hello Okta App General Settings](images/01-Hello-Okta-App.PNG)

Scroll down to the **Client Credentials** section and copy the `Client ID` and `Client Secret`. These values will be used by our app.

### Set Up Client Application

This Application will be used for Server-to-Server Communication between Client and Server application back-ends using [Client Credentials](https://developer.okta.com/docs/guides/implement-client-creds/overview/) authorization flow.

Navigate to **Applications** in the admin console and click: **Add Application**. 
Choose _Service_ and click **Next**.

Populate the fields with these values:

| FIELD NAME | VALUE |
| --- | ---|
| **Name** | `Hello Okta App Client` |

Click **Done**.

**General Settings** tab will be displayed:

![Hello Okta App Client General Settings](images/02-Hello-Okta-App-Client.PNG)

Scroll down to the **Client Credentials** section and copy the `Client ID` and `Client Secret`. These values will be used by our app.

### Set Up Authorization Server

#### Scopes

#### Claims

##### Access Token groups Claim

##### ID Token groups Claim

#### Access Policies

##### Access Policy for Hello Okta App

###### Hello Okta App Access Policy Rules

##### Access Policy for Hello Okta Client App

###### Hello Okta App Client Access Policy Rules
