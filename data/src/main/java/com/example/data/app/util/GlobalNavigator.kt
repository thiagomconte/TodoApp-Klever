package com.example.data.app.util

object GlobalNavigator {
    private var handler: GlobalNavigatorHandler? = null

    fun registerHandler(handler: GlobalNavigatorHandler) {
        this.handler = handler
    }

    fun unregisterHandler() {
        handler = null
    }

    fun logout() {
        handler?.logout()
    }
}
