# Run: Presentation Module

This module is responsible for the UI of the run feature. It contains the screens for the run overview and the active run, as well as the view models and other UI-related components.

## Features

- **Run Overview:** A screen that displays a list of the user's past runs.
- **Active Run:** A screen that displays the user's current run in progress, including a map view and real-time stats.
- **Run-specific composables:** A collection of custom composables that are used in the run screens.

## Dependencies

- `core:domain`: This module depends on the `core:domain` module for domain models.
- `run:domain`: This module depends on the `run:domain` module for run-specific domain models and interfaces.
- **Coil:** Used for loading images.
- **Google Maps Compose:** Used for displaying maps.
- **Activity Compose:** Used for handling activities in a composable way.
- **Timber:** Used for logging.
