# SDVQ3 Build 006 — Quest Android graphics diagnostic

This is a **separate, unverified Android OpenGL ES 2.0 diagnostic app**. It is **not** Stardew Valley, a VR stereoscopic renderer, a game modification, or a graphics hook. It does not access or alter Stardew Valley or its saves.

## Expected result

An Android app window displays a colored triangle on a dark background. Tapping the window changes the triangle from green to orange and back. This confirms a basic GLES2 rendering path and touch input on the Quest. It does **not** establish access to the Stardew graphics pipeline or stereoscopic display capability.

## Upload to GitHub (no PC)

Keep your existing `index.html`. Upload the project files **preserving folder paths** to your SDVQ3 repository. GitHub's web uploader can upload individual files but may flatten folders; use Create new file and enter the complete path if necessary. The important paths are:

- `settings.gradle`
- `build.gradle`
- `app/build.gradle`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/dev/sdvq3/graphicscheck/MainActivity.java`
- `.github/workflows/build-graphics-check.yml`

For easiest Quest-only setup, consider a temporary GitHub Codespace in your browser and upload/extract the ZIP there, then commit and push. Codespaces availability and billing depend on your account; do not upload any proprietary game APK.

Open **Actions > Build 006 Android graphics check > Run workflow**. On success, open the run and download the **SDVQ3-graphics-check-006-debug** artifact ZIP, extract `app-debug.apk`, and sideload it to Quest using a sideloading method you already trust. An unsigned-for-release *debug-signed* APK is normal for testing. Android may warn about installing from unknown sources. Do not replace or uninstall your existing Stardew installation.

## Safety and limitations

The uploaded third-party game APK is not included. Do not publish it. This source has not been compiled or tested on actual Quest hardware in this environment; build and runtime errors are possible. Next milestone is a defensible design for game-specific rendering instrumentation, which requires more than an external app.
