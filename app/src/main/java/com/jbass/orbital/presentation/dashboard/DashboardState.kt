package com.jbass.orbital.presentation.dashboard

import com.jbass.orbital.domain.model.ConnectionState
import com.jbass.orbital.domain.model.SmartDevice


data class DashboardUiState(
    val devices: List<SmartDevice> = emptyList(),
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val isLoading: Boolean = true
)