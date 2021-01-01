package com.example.nikolakocic.gamelauncher

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.atomic.AtomicInteger

class MyViewModel : ViewModel() {
    private val actions = listOf<ActionManager>(BrightnessAction())
    private val allRequiredPermissions = {
        val requiredPermissions = mutableSetOf<PermissionManager>()
        for (action in actions) {
            requiredPermissions.addAll(action.requiredPermissions())
        }
        requiredPermissions
    }()

    private val permissionToRequestIterator: Iterator<PermissionManager> = allRequiredPermissions.iterator()
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

    private fun performActions(context: Context) {
        for (action in actions) {
            action.performAction(context)
        }
    }

    fun run(context: Context) {
        finishedPermissionRequest(null, context)
    }
}
