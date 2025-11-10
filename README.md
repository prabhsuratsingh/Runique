# Runique - Android Running App

Runique is a modern, offline-first, multi-module Android application designed for tracking running activities. This project showcases modern Android development practices, emphasizing a clean, scalable, and maintainable architecture.

## Table of Contents

1.  [High-Level Architecture](#high-level-architecture)
2.  [Technology Stack & Key Libraries](#technology-stack--key-libraries)
3.  [Module Breakdown](#module-breakdown)
    -   [App Module](#app-module)
    -   [Core Modules](#core-modules)
    -   [Feature Modules](#feature-modules)
4.  [Technical Deep Dive](#technical-deep-dive)
    -   [Offline-First Architecture and Data Synchronization](#offline-first-architecture-and-data-synchronization)
    -   [Dependency Injection with Koin](#dependency-injection-with-koin)
    -   [Networking with Ktor](#networking-with-ktor)
    -   [Database with Room](#database-with-room)
    -   [Asynchronous Programming](#asynchronous-programming)
    -   [UI with Jetpack Compose](#ui-with-jetpack-compose)
5.  [Getting Started](#getting-started)

---

## High-Level Architecture

This project follows the principles of **Clean Architecture** and is structured into multiple modules. The architecture enforces a strict separation of concerns, enhances scalability, and allows for independent development and testing of features.

The architecture is layered into three main parts:

-   **Data Layer**: Responsible for sourcing data. This includes network operations (remote data source) and database access (local data source). It contains repositories that abstract the data sources from the rest of the app.
-   **Domain Layer**: Contains the core business logic, use cases, and domain models. This layer is pure Kotlin, with no dependencies on the Android framework, making it highly testable and reusable.
-   **Presentation (UI) Layer**: Responsible for displaying the data to the user and handling user interactions. It is built entirely with Jetpack Compose and follows the MVVM (Model-View-ViewModel) pattern.

![Diagram of app architecture](https://example.com/architecture.png) *Note: A diagram would be a great addition here to visually represent the module dependencies and architectural layers.*

---

## Technology Stack & Key Libraries

-   **[Kotlin](https://kotlinlang.org/)**: The official programming language for Android development.
-   **[Jetpack Compose](https://developer.android.com/jetpack/compose)**: A modern declarative UI toolkit for building native Android UI.
-   **[Koin](https://insert-koin.io/)**: A pragmatic and lightweight dependency injection framework for Kotlin.
-   **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)**: For asynchronous programming and reactive data streams.
-   **[Ktor](https://ktor.io/)**: A modern asynchronous HTTP client for making network requests.
-   **[Room](https://developer.android.com/training/data-storage/room)**: A persistence library that provides an abstraction layer over SQLite.
-   **[WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)**: For deferrable, asynchronous background tasks, crucial for the offline-first data synchronization strategy.
-   **[Material 3](https://m3.material.io/)**: The latest version of Google's design system.
-   **[Google Maps Compose SDK](https://developers.google.com/maps/documentation/android-sdk/compose)**: For integrating Google Maps directly within Jetpack Compose.

---

## Module Breakdown

The project is organized into several modules, categorized as `app`, `core`, or `feature`.

### App Module

-   **:app**: This is the main application module that assembles the entire application. It has dependencies on all other feature and core modules and is responsible for:
    -   Initializing the Koin dependency graph.
    -   Setting up the main `Activity`.
    -   Hosting the Jetpack Compose navigation graph (`NavigationRoot.kt`).

### Core Modules

These modules provide shared functionality across all feature modules.

-   **:core:domain**: Contains the essential business logic and models (e.g., `Run`, `User`, `DataError`). It defines the repository interfaces (`RunRepository`, `AuthRepository`) that the data layer implements. It is a pure Kotlin module.
-   **:core:data**: Implements the repository interfaces from the domain layer. It contains the `OfflineFirstRunRepository`, which orchestrates data fetching between the local database and the remote network source, providing a single source of truth for the application's data.
-   **:core:database**: Manages the local database using Room. It defines the `RunDatabase`, Data Access Objects (DAOs), and entity classes. It also provides the Koin module (`databaseModule`) to inject the database and DAOs.
-   **:core:presentation:designsystem**: The application's design system. It contains the Compose theme (`RuniqueTheme`), colors, typography, and a library of reusable, stateless UI components (buttons, text fields, etc.) to ensure a consistent look and feel.
-   **:core:presentation:ui**: Holds shared UI logic and utility functions, such as `UiText` for handling string resources, `RunDataFormatters` for formatting data for display, and `ObserveAsEvents` for handling one-off UI events.

### Feature Modules

Features are self-contained and vertically sliced, often with their own `data`, `domain`, and `presentation` modules.

#### Auth (`:auth:*`)

-   Handles user registration and login.
-   `:auth:domain`: Defines the `AuthRepository` interface and validation logic like `UserDataValidator`.
-   `:auth:data`: Implements `AuthRepository` by making network calls using Ktor. Includes data transfer objects (DTOs) for requests and responses.
-   `:auth:presentation`: Contains the Compose screens for Intro, Login, and Register, along with their `ViewModel`s.

#### Run (`:run:*`)

-   Manages everything related to tracking runs.
-   `:run:domain`: Defines run-specific interfaces like `LocationObserver`.
-   `:run:location`: Implements `LocationObserver` using the Android Fused Location Provider to track the user's location during a run.
-   `:run:data`: Contains `WorkManager` logic (`SyncRunWorkerScheduler`, `CreateRunWorker`) to sync runs with the backend, ensuring data is not lost even if the user is offline.
-   `:run:network`: Provides the `KtorRemoteRunDataSource` for run-specific network operations.
-   `:run:presentation`: The UI for the run feature, including the `RunOverviewScreen` and the `ActiveRunScreen`, which features a real-time map display.

#### Analytics (`:analytics:*`)

-   A **dynamic feature module** that can be downloaded on demand.
-   `:analytics:domain`: Defines the `AnalyticsRepository` for fetching aggregated run data.
-   `:analytics:data`: Implements the repository, reading data directly from the local Room database.
--   `:analytics:presentation`: The UI for the `AnalyticsDashboardScreen`, displaying statistics and charts.

---

## Technical Deep Dive

### Offline-First Architecture and Data Synchronization

Runique is built with an offline-first approach to provide a reliable and seamless user experience, regardless of network connectivity. This is achieved through a combination of local database caching, a smart data repository, and background synchronization using `WorkManager`.

-   **Local Cache as Single Source of Truth**: The UI layers read data exclusively from the local Room database. This ensures that the app is always responsive and can display data even when the device is offline. All data displayed is queried directly from the local database using `Flow`, so the UI updates reactively to any changes in the cache.

-   **Repository-level Orchestration**: The `OfflineFirstRunRepository` is the cornerstone of this strategy. When a request for data is made (e.g., fetching all runs), it first serves the cached data from the Room database. It then triggers a background task to fetch the latest data from the network.

-   **Queueing Outgoing Operations**: When a user performs an action that modifies data (e.g., creating or deleting a run), the operation is immediately applied to the local database for a fast UI update. Simultaneously, the operation is queued in a separate table (`RunPendingSyncEntity` for creations, `DeletedRunSyncEntity` for deletions). This ensures no data is lost and provides instant feedback to the user.

-   **Robust Synchronization with `WorkManager`**: `WorkManager` is used to reliably execute these queued operations in the background.
    -   `CreateRunWorker` and `DeleteRunWorker` are responsible for pushing the queued local changes to the remote server.
    -   `FetchRunsWorker` periodically fetches fresh data from the server to keep the local cache up-to-date.
    -   `WorkManager` handles constraints (e.g., requiring network connectivity), implements backoff and retry policies, and guarantees that the work will be completed, even if the app is closed or the device is restarted.

This strategy guarantees that the user can always interact with the app, and their data will be automatically and efficiently synchronized with the backend whenever a network connection is available.

### Dependency Injection with Koin

Koin is used to manage dependencies throughout the app. Each module is responsible for defining its own Koin module, which declares its provided dependencies. The `:app` module then loads all necessary modules to construct the complete dependency graph.

Example from `:core:database`:

```kotlin
val databaseModule = module {
    single { // Provides a singleton instance of RunDatabase
        Room.databaseBuilder(
            androidApplication(),
            RunDatabase::class.java,
            "run.db"
        ).build()
    }
    single { get<RunDatabase>().runDao } // Provides the RunDao
    // ... other DAOs
}
```

### Networking with Ktor

Ktor is configured in the `:core:data` module via a `HttpClientFactory`. This factory sets up a reusable Ktor client with default headers, serialization (using `kotlinx.serialization`), and logging. Feature-specific data sources then use this client to perform network operations.

### Database with Room

The local database is managed by Room. The `RunDatabase` class defines all entities and the version. DAOs expose suspend functions and Flows to interact with the database asynchronously and reactively. For example, `runDao.getRuns()` returns a `Flow<List<RunEntity>>`, so the UI can automatically update whenever the list of runs changes.

### Asynchronous Programming

Coroutines and Flow are used extensively to manage background threads and handle streams of data. This results in non-blocking, responsive UI.

-   **Repository Layer**: Uses `suspend` functions for one-shot operations (e.g., creating a run) and `Flow` for observing data changes (e.g., watching a list of runs).
-   **ViewModel Layer**: Collects Flows from the repositories and exposes the data to the UI as `StateFlow`, which is a hot, state-holding Flow.
-   **UI Layer**: Collects the `StateFlow` from the ViewModel using `collectAsStateWithLifecycle` to ensure collection is done in a lifecycle-aware manner.

### UI with Jetpack Compose

The entire UI is built with Jetpack Compose, following a Unidirectional Data Flow (UDF) pattern. State flows down from the `ViewModel` to the Composables, and events flow up from the Composables to the `ViewModel`. This makes the UI predictable and easy to debug.

---

## Getting Started

1.  Clone the repository:
    ```bash
    git clone https://your-repository-url.git
    ```
2.  Open the project in Android Studio.
3.  Create a `local.properties` file in the root directory and add your Google Maps API key:
    ```properties
    MAPS_API_KEY=YOUR_API_KEY_HERE
    ```
4.  Sync the project with Gradle files.
5.  Build and run the `app` module on an Android emulator or a physical device.
""
