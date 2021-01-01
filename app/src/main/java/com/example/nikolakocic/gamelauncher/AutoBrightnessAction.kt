package com.example.nikolakocic.gamelauncher

import android.content.Context
import android.provider.Settings

class AutoBrightnessAction : ActionManager {
    override fun getPreferenceName(): String {
        return "auto_brightness_val"
    }

    override fun requiredPermissions(): List<PermissionManager> {
        return listOf(PermissionManagerInstances.writeSettingsPermissionManager)
    }

    override fun getCurrentValue(context: Context): PreviousValue? {
        return try {
            PreviousIntValue(
                Settings.System.getInt(
                    context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE
                )
            )
        } catch (e: Settings.SettingNotFoundException) {
            null
        }
    }

    private fun setAutoBrightness(context: Context, value: Int): Boolean {
        return Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            value
        )
    }

    override fun performAction(context: Context): Boolean {
        return setAutoBrightness(context, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
    }

    override fun revertAction(context: Context, previousValue: PreviousValue) {
        check(previousValue is PreviousIntValue)
        setAutoBrightness(context, previousValue.value)
    }
}
