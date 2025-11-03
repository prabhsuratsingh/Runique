# Core: Data Module

This module is the single source of truth for all application data. It is responsible for fetching data from the network and storing it in the local database, as well as providing a unified API for accessing the data to the rest of the application.

## Features

- **OfflineFirstRunRepository:** An implementation of the `RunRepository` interface from the `core:domain` module. It follows an offline-first approach, meaning that it will always try to fetch data from the local database first and then sync it with the network.
- **HttpClientFactory:** A factory for creating Ktor `HttpClient` instances with the default configuration for the application.
- **Data-specific Koin modules:** Provides the necessary dependencies for the `core:data` module, such as the `OfflineFirstRunRepository` and the `HttpClient`.

## Dependencies

- `core:domain`: This module depends on the `core:domain` module for the `RunRepository` interface and other domain models.
- `core:database`: This module depends on the `core:database` module for local data storage.
- **Ktor:** Used for making network requests.
- **Koin:** Used for dependency injection.
- **Timber:** Used for logging.
