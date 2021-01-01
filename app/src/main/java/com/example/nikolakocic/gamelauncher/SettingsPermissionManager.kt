package com.example.nikolakocic.gamelauncher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

class SettingsPermissionManager: PermissionManager {
    override fun activityCode(): Int {
        return ActivityStartCodes.WRITE_SETTINGS_PERMISSION.code
    }

    override fun hasPermission(context: Context): Boolean {
        return Settings.System.canWrite(context)
    }

    override fun requestPermission(activity: Activity) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + activity.packageName)
        activity.startActivityForResult(intent, activityCode())
    }
}
