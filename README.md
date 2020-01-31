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

Navigate to **API > Authorization Servers**. Click **Add Authorization Server**. 
Fill in the values:

| FIELD NAME | VALUE |
| --- | ---|
| **Name** | `Hello Okta App` |
| **Description** | `Hello Okta App` |
| **Audience** | `api://hellookta` |

Click **Done**.

**Settings** tab will be displayed:

![Hello Okta App Authorization Server Settings](images/03-Authorization-Server.PNG)

Copy the `Issuer` URL. This value will be used by our app.

#### Scopes

Create a [custom scope](https://www.oauth.com/oauth2-servers/scope/defining-scopes/) for our consumer application to restrict access token to this example.

From the menu bar select **API > Authorization Servers**. 
Edit the authorization server created in the previous step by clicking on the edit pencil, then click **Scopes > Add Scope**. 
Fill out the name field with `message.read` and press **Create**:

![Custom Scope](images/04-Custom-Scope.PNG)

#### Claims

##### Access Token groups Claim

##### ID Token groups Claim

#### Access Policies

##### Access Policy for Hello Okta App

###### Hello Okta App Access Policy Rules

##### Access Policy for Hello Okta Client App

###### Hello Okta App Client Access Policy Rules
