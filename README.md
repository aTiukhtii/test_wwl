# Giphy App

This project is a Jetpack Compose-based Android app that integrates with the Giphy API to fetch and display GIFs. It utilizes Room for local caching, Hilt for dependency injection and Coil for gifs loading.

## Prerequisites

To run this project, you need to create an API key from Giphy and store it in a `secrets.properties` file in the root folder.

### Steps to Get Giphy API Key
1. Go to [Giphy Developers](https://developers.giphy.com/).
2. Sign in or create an account.
3. Create an API key by registering an application.
4. Copy the API key.

### Setting Up the `secrets.properties` File
In the root folder of your project, create a file named `secrets.properties` and add the following line:

```
API_KEY=your_api_key_here
```

Replace `your_api_key_here` with the actual API key you obtained from Giphy.

## Project Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/aTiukhtii/test_wwl.git
   ```
2. Open the project in Android Studio.
3. Make sure you have the latest version of Android Studio and the required dependencies installed.
4. Sync the project with Gradle.
5. Run the app on an emulator or physical device.

## Features
- Fetch trending GIFs from Giphy API.
- Fetch GIF by id from Giphy API.
- Cache GIFs locally using Room database.
- Modern UI with Jetpack Compose.

## Technologies Used
- **Jetpack Compose** for UI
- **Hilt** for dependency injection
- **Retrofit** for network calls
- **Room** for local database caching
- **StateFlow** for reactive state management
---
