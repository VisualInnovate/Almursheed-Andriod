package com.visualinnovate.almursheed.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.android.material.snackbar.Snackbar
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.home.view.LiveEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

inline fun View.onDebouncedListener(
    delayInClick: Long = 500L,
    crossinline listener: (View) -> Unit,
) {
    val enableAgain = Runnable { isEnabled = true }
    setOnClickListener {
        if (isEnabled) {
            isEnabled = false
            postDelayed(enableAgain, delayInClick)
            listener(it)
        }
    }
}

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
    data: Bundle? = null,
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
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    this.finish()
}

val EditText.value
    get() = text?.toString()?.trim() ?: ""
// ex: val name = etName.value

fun String.isEmptySting(): Boolean {
    return (
        TextUtils.isEmpty(this) ||
            this.equals("", ignoreCase = true) ||
            this.equals("{}", ignoreCase = true) ||
            this.equals("null", ignoreCase = true) ||
            this.equals("nullnullnullnull", ignoreCase = true) ||
            this.equals("undefined", ignoreCase = true)
        )
}

fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}

fun Date.formatDate(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.format(this)
    } catch (e: Exception) {
        ""
    }
}

fun Date.getDatesBetweenTwoDates(endDate: Date): ArrayList<String> {
    val datesString = ArrayList<String>()
    val dates = mutableListOf<Date>()
    val calendar = Calendar.getInstance()
    calendar.time = this // startDate
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    while (!calendar.time.after(endDate)) {
        dates.add(calendar.time)
        calendar.add(Calendar.DATE, 1)
    }
    dates.forEach {
        datesString.add(it.formatDate())
    }
    return datesString
}
