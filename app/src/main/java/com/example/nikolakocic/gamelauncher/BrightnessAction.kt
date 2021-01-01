package com.example.nikolakocic.gamelauncher

import android.content.Context
import android.provider.Settings


class BrightnessAction(private var previousBrightness: Int? = null) : ActionManager {
    override fun requiredPermissions(): List<PermissionManager> {
        return listOf(PermissionManagerInstances.writeSettingsPermissionManager)
    }

    override fun performAction(context: Context) {
        previousBrightness = Settings.System.getInt(
            context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 0
        )
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 10)
    }

    override fun revertAction(context: Context) {
        val previousBrightness = previousBrightness
        if (previousBrightness != null) {
            Settings.System.putInt(
                context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, previousBrightness
            )
        }
    }
}
