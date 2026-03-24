# StoreExample
A sample Android store app that showcases a clean, scalable, and well-structured approach to application development.


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

## Running the App

**Prerequisites:** Android Studio, JDK 11+

1. Clone the repository
2. Open the project in Android Studio
3. Wait for Gradle sync to complete
4. Select a build variant in the **Build Variants** panel (see below)
5. Run on a device or emulator with Android 7.0+ (API 24)

### Build Variants

| Flavor | Description |
|---|---|
| `vanilla` | Base version of the app |
| `partnerA` | Customized build for Partner A |
| `partnerB` | Customized build for Partner B |
