package com.example.nikolakocic.gamelauncher

sealed class PreviousValue
data class PreviousIntValue(val value: Int) : PreviousValue()
data class PreviousBoolValue(val value: Boolean) : PreviousValue()
