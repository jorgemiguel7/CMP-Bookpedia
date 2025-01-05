package com.jorge.bookpedia

import androidx.compose.ui.window.ComposeUIViewController
import com.jorge.bookpedia.app.App
import com.jorge.bookpedia.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }