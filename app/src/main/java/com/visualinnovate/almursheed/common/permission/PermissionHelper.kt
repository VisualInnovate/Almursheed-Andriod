package com.visualinnovate.almursheed.common.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.visualinnovate.almursheed.R
import java.lang.ref.WeakReference

class PermissionHelper private constructor(private val fragment: WeakReference<Fragment>) {

    private var permissionManager: PermissionManager? = null
    private val askedPermissions = mutableListOf<Permission>()
    private var isRequired: Boolean = false
    private var grantedPermissionResultCallback: (MutableSet<Permission>) -> Unit = { grantedList -> }

    private lateinit var deniedDialog: AskPermissionDialog
    private val dialogBtnClickCallback: () -> Unit = {
        deniedDialog.dismiss()
        if (isRequired) openSettings()
    }

    companion object {
        fun init(fragment: Fragment): PermissionHelper { // initialize helper and permissionManager object
            val permissionHelper = PermissionHelper(WeakReference(fragment))
            permissionHelper.initPermissionManager()
            return permissionHelper
        }
    }

    fun initPermissionManager() {
        if (permissionManager == null) {
            permissionManager = PermissionManager.init(fragment)
        }
    }

    fun addPermissionsToAsk(vararg permission: (Permission)): PermissionHelper {
        askedPermissions.addAll(permission)
        return this
    }

    fun isRequired(isRequired: Boolean): PermissionHelper {
        this.isRequired = isRequired
        return this
    }

    fun requestPermissions(grantedPermissionsResultCallback: (MutableSet<Permission>) -> Unit) {
        this.grantedPermissionResultCallback = grantedPermissionsResultCallback
        permissionManager!!.addPermissionList(askedPermissions).checkPermission {
                grantedList, deniedList ->
            if (deniedList.isNotEmpty()) {
                if (isRequired) {
                    handleDeniedRequiredPermissions()
                } else {
                    handleDeniedNotRequiredPermissions()
                }
            } else {
                grantedPermissionsResultCallback.invoke(grantedList)
            }
        }
    }

    private fun handleDeniedNotRequiredPermissions() {
        showDialog("not required Permissions", R.drawable.ic_close, true)
    }

    private fun handleDeniedRequiredPermissions() {
        showDialog("required Permissions", R.drawable.ic_close, false)
    }

    private fun showDialog(message: String?, image: Int?, isCancelable: Boolean) {
        deniedDialog =
            message?.let {
                AskPermissionDialog(
                    { dialogBtnClickCallback() },
                    it,
                    image,
                    isCancelable
                )
            }!!
        fragment.get()?.requireActivity()?.supportFragmentManager?.let {
            deniedDialog.show(
                it,
                "AskPermissionDialog"
            )
        }
    }

    private fun openSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + fragment.get()?.context?.packageName))
            fragment.get()?.context?.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(fragment.get()?.context, "Activity Not Found", Toast.LENGTH_SHORT).show()
        }
    }

    fun cleanUp() {
        permissionManager = null
        askedPermissions.clear()
    }
}
