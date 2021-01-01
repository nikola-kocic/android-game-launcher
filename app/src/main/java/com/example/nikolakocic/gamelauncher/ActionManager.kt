package com.example.nikolakocic.gamelauncher

import android.content.Context

interface ActionManager {

    fun getPreferenceName(): String
    fun requiredPermissions(): List<PermissionManager>
    fun getCurrentValue(context: Context): PreviousValue?
    fun performAction(context: Context): Boolean
    fun revertAction(context: Context, previousValue: PreviousValue)
}
