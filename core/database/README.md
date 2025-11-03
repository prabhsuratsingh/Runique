# Core: Database Module

This module is responsible for providing a local database to store and access data related to runs. It uses Room for persistence and Koin for dependency injection.

## Features

- **RunDatabase:** The main Room database class that defines the entities and DAOs.
- **DAOs:** Data Access Objects for interacting with the database tables:
    - `RunDao`: For accessing run data.
    - `RunPendingSyncDao`: For runs that are pending synchronization with a remote server.
    - `AnalyticsDao`: For analytics data.
- **Entities:** The data models for the database tables:
    - `RunEntity`
    - `RunPendingSyncEntity`
    - `DeletedRunSyncEntity`
- **RoomLocalRunDataSource:** An implementation of the `LocalRunDataSource` interface from the `core:domain` module, which provides a high-level API for interacting with the database.
- **DatabaseModule:** A Koin module that provides the necessary dependencies for using the database, such as the `RunDatabase` instance and the DAOs.

## Dependencies

- `core:domain`: This module depends on the `core:domain` module for the `LocalRunDataSource` interface and other domain models.
- **Room:** Used for local data persistence.
- **Koin:** Used for dependency injection.
- **Bson:** Used for serialization and deserialization of data.
