package com.jbass.orbital.domain.model

sealed interface UiError {
    data class ConnectionError(val message: String) : UiError
    data class CommandRejected(
        val errorCode: String?,
        val message: String
    ) : UiError
    data class ParsingError(val rawMessage: String) : UiError
}