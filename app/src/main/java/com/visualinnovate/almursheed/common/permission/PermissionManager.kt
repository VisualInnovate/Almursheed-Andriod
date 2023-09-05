package com.visualinnovate.almursheed.common.permission

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class PermissionManager private constructor(private val fragment: WeakReference<Fragment>) {

    private val permissionsList = mutableListOf<Permission>()
    private val grantedList = mutableSetOf<Permission>()
    private val deniedList = mutableSetOf<Permission>()
    private var permissionsResultCallback: (MutableSet<Permission>, MutableSet<Permission>) -> Unit =
        { grantedList, deniedList -> }

    private val permissionCallbackFromActivity =
        // return result of permission check from activity||fragment callback
        fragment.get()?.registerForActivityResult(RequestMultiplePermissions()) { grantResults ->
            grantResults.forEach { it ->
                val permission = Permission.from(it.key)
                if (!isPermissionGranted(it.key)) {
                    deniedList.add(permission)
                } else {
                    grantedList.add(permission)
                }
            }
            sendResult()
        }

    companion object {
        fun init(fragment: WeakReference<Fragment>) = PermissionManager(fragment)
    }

    fun addPermissionList(permissions: MutableList<Permission>): PermissionManager {
        permissionsList.addAll(permissions)
        return this
    }

    fun checkPermission(permissionResultCallback: (MutableSet<Permission>, MutableSet<Permission>) -> Unit) {
        this.permissionsResultCallback = permissionResultCallback
        launchPermissionsRequest()
    }

    private fun launchPermissionsRequest() {
        permissionCallbackFromActivity?.launch(getPermissionList())
    }

    private fun sendResult() {
        permissionsResultCallback(grantedList, deniedList)
        cleanUp()
    }

    private fun getPermissionList() = // convert list<Permissions> to array<String>
        permissionsList.flatMap { it.permissions.toList() }.toTypedArray()

    private fun isPermissionGranted(permission: String) =
        fragment.get()?.requireContext()?.let {
            ContextCompat.checkSelfPermission(
                it,
                permission
            )
        } == PackageManager.PERMISSION_GRANTED

    private fun cleanUp() {
        grantedList.clear()
        deniedList.clear()
        permissionsList.clear()
    }

    // all comments for if need to check if permission is asked before or not
    /*   private fun handlePermissionRequest() {
           // run when permissions already denied
           fragment.get()?.let { fragment ->
               when {
                   areAllPermissionsGranted(fragment) -> sendPositiveResult()
                   shouldShowPermissionRationale(fragment) -> displayRationale(fragment)
                   else -> {
                       requestPermissions()
                   }
               }
           }
       }
       private fun sendDeniedAllTimeResult() {
           // run only when asked permission before and user click deny
           deniedList.addAll(permissionsList)
           sendResultAndCleanUp(true)
       }

       private fun sendGrantedResult() {
           sendResult()
       }

       private fun areAllPermissionsGranted(fragment: Fragment) = permissionsList.all { it.isGranted(fragment) }

       private fun shouldShowPermissionRationale(fragment: Fragment) = permissionsList.any { it.requiresRationale(fragment) }

       private fun Permission.isGranted(fragment: Fragment): Boolean {
           return permissions.all { hasPermission(fragment, it) }
       }

       private fun Permission.requiresRationale(fragment: Fragment) =
           permissions.any { fragment.shouldShowRequestPermissionRationale(it) }

       private fun hasPermission(fragment: Fragment, permission: String) =
           ContextCompat.checkSelfPermission(
               fragment.requireContext(),
               permission,
           ) == PackageManager.PERMISSION_GRANTED

           */
}
