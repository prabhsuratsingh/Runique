# Run: Data Module

This module is responsible for managing the data related to runs. It includes workers for creating, deleting, and fetching runs from the network, as well as a scheduler for syncing runs with a remote server.

## Features

- **SyncRunWorkerScheduler:** A scheduler that uses WorkManager to periodically sync runs with a remote server.
- **CreateRunWorker, DeleteRunWorker, FetchRunsWorker:** WorkManager workers that perform the actual data synchronization tasks.
- **DataErrorToWorkerResult:** A utility function for converting a `DataError` from the `core:domain` module into a `ListenableWorker.Result`.

## Dependencies

- `core:domain`: This module depends on the `core:domain` module for the `DataError` class and other domain models.
- `core:database`: This module depends on the `core:database` module for local data storage.
- `run:domain`: This module depends on the `run:domain` module for run-specific domain models and interfaces.
- **WorkManager:** Used for scheduling and executing background work.
- **Koin:** Used for dependency injection.
- **Kotlinx Serialization:** Used for serializing and deserializing data.
- **Play Services Location:** Used for accessing location services.
