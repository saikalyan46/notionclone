package edu.bu.androiddev.model

import androidx.compose.runtime.Composable

interface Node {
    fun generateJsonMap():MutableMap<String, *>

    @Composable
    open fun render():Unit

}