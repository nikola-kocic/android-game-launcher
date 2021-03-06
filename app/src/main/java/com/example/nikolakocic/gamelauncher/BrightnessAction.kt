package com.example.nikolakocic.gamelauncher

import android.content.Context
import android.provider.Settings

class BrightnessAction : ActionManager {
    override fun getPreferenceName(): String {
        return "brightness_val"
    }

    override fun requiredPermissions(): List<PermissionManager> {
        return listOf(PermissionManagerInstances.writeSettingsPermissionManager)
    }

    override fun getCurrentValue(context: Context): PreviousValue? {
        return try {
            PreviousIntValue(
                Settings.System.getInt(
                    context.contentResolver, Settings.System.SCREEN_BRIGHTNESS
                )
            )
        } catch (e: Settings.SettingNotFoundException) {
            null
        }
    }

    private fun setBrightness(context: Context, value: Int): Boolean {
        return Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            value
        )
    }

    override fun performAction(context: Context): Boolean {
        return setBrightness(context, 10)
    }

    override fun revertAction(context: Context, previousValue: PreviousValue) {
        check(previousValue is PreviousIntValue)
        setBrightness(context, previousValue.value)
    }
}
