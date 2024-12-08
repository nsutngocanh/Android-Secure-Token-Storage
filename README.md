<a id="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="https://firebasestorage.googleapis.com/v0/b/kan-videos.appspot.com/o/download.png?alt=media&token=5b916ada-b0a7-47dc-b033-b3a3ef0af652" alt="Logo" width="100%" height="300">
  </a>

  <h3 align="center">Secure Token Storage Library</h3>
 
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

The **Secure Token Storage** library is an open-source Android library designed to simplify secure storage and management of tokens. It provides robust mechanisms to store and handle sensitive data like authentication tokens, API keys, and more. This library ensures high security by leveraging modern encryption standards and Android’s built-in security features.

## Features

- **Token Manager with Multiple Storage Options:**

  - **SQLite**: Persistent storage for tokens with encryption.

  - **SharedPreferences**: Lightweight and quick access with optional encryption.

  - **File Storage**: Encrypted file-based token storage for specific use cases.

- **Encryption Mechanisms:**

  - **AES-GCM**: For secure encryption and decryption of tokens.

  - **MD5, SHA256**: For generating hash keys and token integrity verification.

- **Key Management:**

  - Utilizes Android **Keystore System** for secure key storage and retrieval.

- **Session Management:**

  - Automatic token refresh on expiration.

  - Alerts for anomalies in token access.

  - Token deletion in case of security breaches.

- **Customizable Storage Policies:**

  - Choose storage type based on application requirements.

## How It Works

1.  **Encryption and Decryption**

    - Tokens are encrypted before being saved to the chosen storage option.

    - Encrypted tokens are decrypted securely during retrieval.

2.  **Hash Key Generation**

    - Securely generate hash keys using MD5, SHA256, or similar algorithms for verification.

3.  **Secure Key Management**

    - Leverages Android’s Keystore to safely store cryptographic keys.

4.  **Token Operations**

    - Supports create, read, update, delete (CRUD) operations on tokens.

    - Implements security checks before sensitive operations.

5.  **Session Manager**
    - Create and manage runtim token.
    - Callback to handle token refresh.
    - Support JWT Token and OAuth Token.
    - Set time expired in local for token , auto delete token when expired.
6.  **Device Anomaly**
    - Check device is rooted , debugging , developer mode enable ,...etc

<!-- GETTING STARTED -->

## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

- java (>= 1.8)
- SDK (>=26)

### Installation

### 1. Add the Library to Your Project

Download and add the library in your project as an AAR file <a href="https://firebasestorage.googleapis.com/v0/b/kan-videos.appspot.com/o/SecurceTokenStorage.aar?alt=media&token=db0e49ff-2faa-4494-bbdd-d2260ab88ac9">here</a>

<!-- USAGE EXAMPLES -->

Learn how to add lib to your project <a href="https://stackoverflow.com/questions/16682847/how-to-manually-include-external-aar-package-using-gradle-for-android">here</a>

### 2. Permissions

Ensure your application has the necessary permissions in the `AndroidManifest.xml` file:
<code>

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

</code>

### 3. Initialize the Library

<code>
SecureTokenManager secureTokenManager = new SecureTokenManager.Builder() 							  
.signStorage(new SharedPreferencesTokenStorage(this, "ACCESS_TOKEN_STORAGE")).enableAnomalyDetector(true).setEncryptionProvider(new SHA256EncryptionProvider()).build();
</code>

## Usage

## 1.Token Storage

### Simple Store a Token

<code>
	String token = "my_secure_token";
	secureTokenManager .saveToken("key", token);
</code>

### Retrieve a Token

<code>
secureTokenManager.getToken("key");
</code>

### Delete a Token

<code>
secureTokenManager.deleteToken("key");
</code>

### Retrieve All Token

<code>
secureTokenManager.getAllToken();
</code>

## 2.Session Manager

### Create Instance

<code>
SessionManager sessionManager = new DefaultSessionManager();
</code>

### Add Token

<code>
sessionManager.addToken(new TokenInfo("asca", "ascasca"), new JwtTokenHandler("ascnascnasc", new JwtTokenHandler.JWTTokenAction() {  
    @Override  
  public String whenTokenExpired() {  
        return null;  
  }  
}));
</code>

### Get Token

<code>
sessionManager.getToken("ascasca");
</code>

### Remove Token

<code>
sessionManager.removeToken("ascasca");
</code>

### Check Token Valid

<code>
sessionManager.isTokenValid("ascasca");
</code>

### Refresh Token

<code>
sessionManager.refreshToken("ascasca");
</code>

### Handle Force Delete Token

<code>
sessionManager.handleAnomaly(secureTokenManager, "ascasca");
</code>

## 3.Key Store Helper

### Generate Secret Key

<code>
	KeyStoreHelper keyStoreHelper = new KeyStoreHelper();  <br/>
	String key = keyStoreHelper.generateSecretKey();
</code>

### Get Secret Key

<code>
	KeyStoreHelper keyStoreHelper = new KeyStoreHelper();  
	<br/>
	SecretKey key = keyStoreHelper.getSecretKey("key");
</code>

### Delete Secret Key

<code>
KeyStoreHelper keyStoreHelper = new KeyStoreHelper();  
<br/>
keyStoreHelper.deleteKey("key");
</code>

### Contains Secret Key

Check if exit key

<code>
KeyStoreHelper keyStoreHelper = new KeyStoreHelper();  <br/>
keyStoreHelper.containsKey("key");
</code>

## Encryption Details

- **AES-GCM**: Provides advanced encryption with integrity checks.

- **KeyStore**: Keys are generated and stored securely in the Android KeyStore.

- **MD5, SHA256**: Used for hashing and ensuring data integrity.

## License

This project is licensed under the MIT License. See the <a href="https://www.apache.org/licenses/LICENSE-2.0">LICENSE</a> file for details.

## Contributions

We welcome contributions from the community. Feel free to submit issues or pull requests to improve the library.

## Contact

For questions or support, contact the library author at support@securetoken.com.
