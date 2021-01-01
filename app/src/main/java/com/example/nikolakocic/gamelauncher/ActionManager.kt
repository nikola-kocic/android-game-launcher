package com.example.nikolakocic.gamelauncher

import android.content.Context

interface ActionManager {
    fun requiredPermissions(): List<PermissionManager>
    fun performAction(context: Context): Unit
    fun revertAction(context: Context): Unit
}