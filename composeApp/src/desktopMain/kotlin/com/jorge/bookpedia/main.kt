package com.jorge.bookpedia

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.jorge.bookpedia.app.App
import com.jorge.bookpedia.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "CMP-Bookpedia",
        ) {
            App()
        }
    }
}