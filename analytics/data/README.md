# Analytics: Data Module

This module is responsible for providing a concrete implementation of the `AnalyticsRepository` interface from the `analytics:domain` module.

## Features

- **RoomAnalyticsRepository:** An implementation of the `AnalyticsRepository` that uses the local Room database to store and retrieve analytics data.

## Dependencies

- `core:database`: This module depends on the `core:database` module for local data storage.
- `core:domain`: This module depends on the `core:domain` module for domain models.
- `analytics:domain`: This module depends on the `analytics:domain` module for the `AnalyticsRepository` interface.
- **Koin:** Used for dependency injection.
- **Coroutines:** Used for asynchronous programming.
- **Room:** Used for local data persistence.
