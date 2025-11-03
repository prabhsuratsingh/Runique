# Auth: Data Module

This module is responsible for handling the authentication data. It provides a concrete implementation of the `AuthRepository` interface from the `auth:domain` module.

## Features

- **AuthRepositoryImpl:** An implementation of the `AuthRepository` that uses a Ktor client to make network requests to the Runique API for authentication.
- **EmailPatternValidator:** A utility class for validating email addresses.
- **Data classes for requests and responses:** `LoginRequest`, `LoginResponse`, and `RegisterRequest`.

## Dependencies

- `core:data`: This module depends on the `core:data` module for the Ktor client and other data-related components.
- `core:domain`: This module depends on the `core:domain` module for domain models.
- `auth:domain`: This module depends on the `auth:domain` module for the `AuthRepository` interface.
- **Koin:** Used for dependency injection.
- **Ktor:** Used for making network requests.
