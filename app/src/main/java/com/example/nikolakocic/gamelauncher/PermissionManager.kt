package com.example.nikolakocic.gamelauncher

import android.app.Activity
import android.content.Context

interface PermissionManager {
    fun activityCode(): Int
    fun hasPermission(context: Context): Boolean
    fun requestPermission(activity: Activity)
}
