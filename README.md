# StoreExample
A sample Android store app that showcases a clean, scalable, and well-structured approach to application development.


https://github.com/user-attachments/assets/2e601615-fbc2-4313-99fd-c3f91f0ea60c



## Architecture

**MVVM + Clean Architecture**


The project is organized into distinct layers with clear responsibilities:

```
presentation/   → Fragments, ViewModels, Adapters
data/
  ├── remote/   → Retrofit API calls
  ├── local/    → Room database
  └── repository/ → single source of truth for all data
di/             → Hilt dependency injection modules
```

- **ViewModel + StateFlow** manages UI state in a lifecycle-aware, reactive way
- **Repository pattern** ensures ViewModels never depend directly on the API or database
- **Hilt** handles dependency injection

## Key Design Decisions

- **StateFlow over LiveData** — better coroutines integration, explicit state modeling, no lifecycle leaks
- **Room for favorites** — lightweight local persistence with no backend dependency
- **Repository as the single source of truth** — keeps business logic out of ViewModels and UI layers
- **Hilt for DI** — compile-time safety and Android lifecycle integration
- **Product flavors** — supports multiple partner builds from a single codebase, avoiding duplication

## Tech Stack

| Purpose | Library |
|---|---|
| Networking | Retrofit + OkHttp |
| Local storage | Room |
| Image loading | Glide |
| Dependency injection | Hilt |
| Async / reactive | Coroutines + Flow |

## Staged Rollout Strategy

> Based on [Google Play staged rollout documentation](https://support.google.com/googleplay/android-developer/answer/6346149) and [Android App Rollout Tips – ProAndroidDev](https://proandroiddev.com/ready-aim-release-android-app-rollout-tips-1bc6f851e6fb)

New releases are deployed gradually using Google Play's staged rollout feature:

1. **Internal testing** — release to the internal team for smoke testing
2. **5% rollout** — monitor crash rate and ANRs via Play Console
3. **20% → 50% → 100%** — expand in stages if metrics remain stable
4. **Halt if needed** — rollout can be paused and the previous version remains active for unaffected users

## Running the App

**Prerequisites:** Android Studio, JDK 11+

1. Clone the repository
2. Open the project in Android Studio
3. Wait for Gradle sync to complete
4. Select a build variant in the **Build Variants** panel (see below)
5. Run on a device or emulator with Android 7.0+ (API 24)

### Build Variants

| Flavor | Description |
| Flavor | Description                                 |
|---|---------------------------------------------|
| `vanilla` | Base version of the app                     |
| `partnerA` | Customized build for Partner A (Blue Store) |
| `partnerB` | Customized build for Partner B (Red Store)  |

<img  height="312" alt="Screenshot_20260325_004116" src="https://github.com/user-attachments/assets/f6a04b6e-0f2e-4401-8869-1444d336fca6" />
<img  height="312" alt="Screenshot_20260325_004051" src="https://github.com/user-attachments/assets/15186ec9-c6dd-4dfa-9caa-0b8837e5573a" />

