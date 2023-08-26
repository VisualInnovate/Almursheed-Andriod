package com.visualinnovate.almursheed.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.android.material.snackbar.Snackbar
import com.visualinnovate.almursheed.home.HomeActivity

// fun NavController.navigate(
//    @IdRes destinationId: Int,
//    inclusive: Boolean = false,
//    data: Bundle? = null,
// ) {
//    val navOption =
//        NavOptions.Builder().apply {
//            setPopUpTo(destinationId, inclusive)
//            setEnterAnim(R.anim.slide_from_out_right_to_center)
//            setExitAnim(R.anim.slide_from_center_to_out_left)
//            setPopEnterAnim(R.anim.slide_from_out_left_to_center)
//            setPopExitAnim(R.anim.slide_from_center_to_out_right)
//        }.build()
//    navigate(destinationId, data, navOption)
// }

fun NavController.customNavigate(
    @IdRes destinationId: Int,
    inclusive: Boolean = false,
    data: Bundle? = null
) {
    val navOption =
        NavOptions.Builder().apply {
            // setPopUpTo(destinationId, inclusive)
            popBackStack(destinationId, inclusive)
//            setEnterAnim(R.anim.slide_from_out_right_to_center)
//            setExitAnim(R.anim.slide_from_center_to_out_left)
//            setPopEnterAnim(R.anim.slide_from_out_left_to_center)
//            setPopExitAnim(R.anim.slide_from_center_to_out_right)
        }.build()
    navigate(destinationId, data, navOption)
}

fun View.gone() = run { visibility = View.GONE }

fun View.visible() = run { visibility = View.VISIBLE }

fun View.invisible() = run { visibility = View.INVISIBLE }

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun View.snackBar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Fragment.hideKeyboard() {
    activity?.apply {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Activity.startHomeActivity() {
    val intent = Intent(this, HomeActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    this.finish()
}
