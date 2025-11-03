# Core: Presentation: UI Module

This module contains common UI-related functionality that can be shared across different feature modules. This includes UI models, utility functions, and base classes for view models and UI components.

## Features

- **RunDataFormatters:** A collection of utility functions for formatting run data, such as distance, duration, and pace.
- **ObserveAsEvents:** A utility function for observing a Flow of events in a composable and handling each event only once.
- **UiText:** A utility class for representing text that can be either a string resource or a dynamic string.
- **DataErrorToText:** A utility function for converting a `DataError` from the `core:domain` module into a human-readable `UiText`.

## Dependencies

- `core:domain`: This module depends on the `core:domain` module for the `DataError` class and other domain models.
- `core:presentation:designsystem`: This module depends on the `core:presentation:designsystem` module for the application's theme and design system.
- **Jetpack Compose:** This module uses Jetpack Compose for building the UI.
- **Lifecycle ViewModel Compose:** This module uses the Lifecycle ViewModel Compose library for integrating view models with composables.
