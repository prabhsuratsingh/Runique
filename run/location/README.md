# Run: Location Module

This module is responsible for observing the user's location during a run. It provides a concrete implementation of the `LocationObserver` interface from the `run:domain` module.

## Features

- **AndroidLocationObserver:** An implementation of the `LocationObserver` that uses the Fused Location Provider from Google Play Services to get location updates.
- **LocationMappers:** A set of functions for mapping the location data from the Android framework to the domain models.
- **LocationModule:** A Koin module that provides the necessary dependencies for using the location observer.

## Dependencies

- `core:domain`: This module depends on the `core:domain` module for domain models.
- `run:domain`: This module depends on the `run:domain` module for the `LocationObserver` interface and other run-specific domain models.
- **Koin:** Used for dependency injection.
- **Coroutines:** Used for asynchronous programming.
- **Play Services Location:** Used for accessing location services.
