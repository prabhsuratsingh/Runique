# Run: Network Module

This module is responsible for making network requests related to runs. It provides a concrete implementation of the `RemoteRunDataSource` interface from the `core:data` module.

## Features

- **KtorRemoteRunDataSource:** An implementation of the `RemoteRunDataSource` that uses Ktor to make network requests to the Runique API.
- **RunDto:** A data transfer object for representing run data from the network.
- **RunMappers:** A set of functions for mapping the network DTOs to the domain models.
- **CreateRunRequest:** A data class that represents the request body for creating a run.

## Dependencies

- `core:domain`: This module depends on the `core:domain` module for domain models.
- `core:data`: This module depends on the `core:data` module for the `RemoteRunDataSource` interface.
- **Koin:** Used for dependency injection.
- **Ktor:** Used for making network requests.
