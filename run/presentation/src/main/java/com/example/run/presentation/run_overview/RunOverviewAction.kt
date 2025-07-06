package com.example.run.presentation.run_overview

import com.example.run.presentation.run_overview.model.RunUi

sealed interface RunOverviewAction {
    data object OnStartClick : RunOverviewAction
    data object OnLogoutClick : RunOverviewAction
    data object OnAnalyticsCLick : RunOverviewAction
    data class DeleteRun(val runUi: RunUi) : RunOverviewAction
}