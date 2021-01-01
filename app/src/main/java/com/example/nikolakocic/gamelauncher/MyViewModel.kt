package com.example.nikolakocic.gamelauncher

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    companion object {
        const val PREF_NAME_ACTIVE = "active"
    }

    private val actions = listOf(AutoBrightnessAction(), BrightnessAction())
    private val allRequiredPermissions = {
        val requiredPermissions = mutableSetOf<PermissionManager>()
        for (action in actions) {
            requiredPermissions.addAll(action.requiredPermissions())
        }
        requiredPermissions
    }()

    private val permissionToRequestIterator: Iterator<PermissionManager> =
        allRequiredPermissions.iterator()
    private val permissionToRequest = MutableLiveData<PermissionManager?>()

    fun getPermissionToRequest(): LiveData<PermissionManager?> {
        return permissionToRequest
    }

    private fun getNextPermissionToRequest(context: Context): PermissionManager? {
        while (permissionToRequestIterator.hasNext()) {
            val permission = permissionToRequestIterator.next()
            if (!permission.hasPermission(context)) {
                return permission
            }
        }
        return null
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            Consts.SHARED_PREFERENCE_NAME_OLD_VALUES,
            Context.MODE_PRIVATE
        )
    }

    fun finishedPermissionRequest(code: Int?, context: Context) {
        if (code != null) {
            val requestedPermission = permissionToRequest.value
            if (requestedPermission != null && !requestedPermission.hasPermission(context)) {
                throw Exception("Permission not granted for " + requestedPermission.activityCode())
            }
        }
        val nextPermissionToRequest = getNextPermissionToRequest(context)
        permissionToRequest.value = nextPermissionToRequest
        if (nextPermissionToRequest == null) {
            performActions(context)
        }
    }

    private fun saveCurrentValues(context: Context, pref: SharedPreferences) {
        val editor = pref.edit()
        editor.putBoolean(PREF_NAME_ACTIVE, true)
        for (action in actions) {
            val currentValue = action.getCurrentValue(context)
            val preferenceKey = action.getPreferenceName()
            when (currentValue) {
                is PreviousIntValue -> editor.putInt(preferenceKey, currentValue.value)
                is PreviousBoolValue -> editor.putBoolean(preferenceKey, currentValue.value)
                null -> {
                }
            }
        }
        editor.apply()
    }

    private fun performActions(context: Context) {
        val pref = getPreferences(context)
        val active = pref.getBoolean(PREF_NAME_ACTIVE, false)
        if (active) {
            revertActions(context)
        } else {
            saveCurrentValues(context, pref)
            for (action in actions) {
                action.performAction(context)
            }
        }
    }

    private fun revertActions(context: Context) {
        val pref = getPreferences(context)
        val allPrefs = pref.all
        for (action in actions) {
            val previousValue: PreviousValue? =
                when (val prefValue = allPrefs[action.getPreferenceName()]) {
                    null -> null
                    is Int -> PreviousIntValue(prefValue)
                    is Boolean -> PreviousBoolValue(prefValue)
                    else -> null
                }
            if (previousValue != null) {
                action.revertAction(context, previousValue)
            }
        }
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }

    fun run(context: Context) {
        finishedPermissionRequest(null, context)
    }
}
