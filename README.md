# Stardew 3D Quest — Build 003: Android rendering feasibility

**Goal:** The original Android Stardew Valley displayed in a Quest 3 stereoscopic virtual screen, standalone, retaining game controls and saves where feasible.

**Status:** Static APK inventory and development decision gate. **Not a stereo patch or playable 3D APK.** The earlier WebXR farm demonstration is a separate rendering proof of concept, not a bridge into the game.

## Verified from the provided APK ZIP (static inspection only)

- APK contains `lib/arm64-v8a/libaot-StardewValley.dll.so` and `libaot-MonoGame.Framework.dll.so`.
- Contains `libAPKVISION.so` in multiple architectures. Its purpose and trustworthiness are **unknown**.
- This is not evidence that a practical `SpriteBatch` hook exists. Android AOT and rendering internals require further analysis.
- User reports that the APK launches and plays on Quest 3; not independently tested here.

## Realistic rendering routes

1. **Game-side renderer modification (preferred, unproven):** Change draw operations before flattening, producing a per-object depth assignment and stereo eye images. Need trusted app build, exact managed/native runtime details, compatible Quest stereo presentation API, input and save testing.
2. **Quest-side external capture (not assumed possible):** Android capture restrictions and XR display access make this a poor initial route. Even with frames, monocular capture lacks object depth.
3. **Frame-derived stereo (fallback):** Approximate depth from flat frames, but can introduce halos, missing pixels, lag and incorrect occlusion. It cannot reliably reproduce true object-separated stereo.

## Next decision gate

Obtain **verified provenance** for an authorized game installation. Examine manifest, signatures, runtime and render APIs without running untrusted native libraries. Then implement a *small* instrumented render experiment in a controlled test harness before attempting to patch the commercial app. Do not upload the APK or extracted proprietary assets to public GitHub repositories.

## Run the static inspector

If an authorized APK is locally available in a Python environment:

```sh
python tools/inspect_apk.py /path/to/game.apk --output apk-inventory.json
```

The script reads ZIP directory metadata, names and a whole-file hash. It does **not** extract assets, modify the APK or execute game code.

The GitHub Actions workflow validates the inspector with a synthetic test archive; it does not accept or download the commercial APK. It can run entirely in GitHub's cloud and requires no development computer.

## Quest-only workflow

Use the Quest browser to upload this project's files to your existing repository. GitHub Actions can validate source automatically. A future Android project can be built in GitHub Actions, but that will require a separate Quest-compatible app project and on-device installation/testing. This package alone does not build an Android APK.
