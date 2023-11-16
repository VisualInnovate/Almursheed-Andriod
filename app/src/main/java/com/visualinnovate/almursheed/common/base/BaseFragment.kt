package com.visualinnovate.almursheed.common.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.auth.AuthViewsManager
import com.visualinnovate.almursheed.MainViewsManager

open class BaseFragment : Fragment() {

    val activityResultsCallBack = MutableLiveData<Intent?>()

    protected fun showAuthLoading() {
        (requireActivity() as AuthViewsManager).showLoading()
    }

    protected fun hideAuthLoading() {
        (requireActivity() as AuthViewsManager).hideLoading()
    }

    protected fun showMainLoading() {
        (requireActivity() as MainViewsManager).showLoading()
    }

    protected fun hideMainLoading() {
        (requireActivity() as MainViewsManager).hideLoading()
    }

    protected fun validEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                activityResultsCallBack.value = data
            }
        }

    protected fun launchActivityForResult(intent: Intent) {
        try {
            resultLauncher.launch(intent)
        } catch (ex: Exception) {
            Log.d("myDebug", "BaseFragment launchActivityForResult   " + ex.localizedMessage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resultLauncher.unregister()
    }

    fun loadGifImage(context: Context, urlToImage: Int, imgView: ImageView) {
        Glide.with(context)
            .asGif()
            .load(urlToImage)
            .into(imgView)
    }

    fun loadImage(context: Context, urlToImage: String, imgView: ImageView) {
        Glide.with(context)
            .load(urlToImage)
            .into(imgView)
    }
}
