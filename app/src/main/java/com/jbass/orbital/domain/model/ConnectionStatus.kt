package com.jbass.orbital.domain.model

sealed interface ConnectionState {
    object Connected : ConnectionState
    object Connecting : ConnectionState
    object Disconnected : ConnectionState
    data class Reconnecting(val attempt: Int) : ConnectionState
}
