# LINE SDK Test application

This is a small toy app that exercises the LINE SDK login and profile APIs. It is meant to be a starting point
for developers wishing to use these in their own application.

## Getting started

To build the app, first check out the repository.

```
$ git clone https://github.com/LINE/line-sdk-starter-android
```

Then set up the LINE SDK by copying SDK libraries and configuring your channel ID.

* Copy the SDK jar file (e.g., line-android-sdk-3.1.13.jar) into app/libs
* Copy the SDK native libs (e.g., armebi, x86) into app/src/main/jniLibs
* Replace {{ ENTER YOUR CHANNEL ID }} with your channel id (from https://developers.line.me/channels/) in AndroidManifest.xml

Then open the directory in Android Studio and build and develop as you would any normal Android application.

LINE Android SDK API Docs: https://developers.line.me/android/api-reference

This application has been built with Android Studio 1.3. Previous versions should work but may require editing the build tools versions in build.gradle.
