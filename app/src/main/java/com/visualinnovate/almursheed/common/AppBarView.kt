package com.visualinnovate.almursheed.common

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.visualinnovate.almursheed.databinding.AppBarMainBinding

class AppBarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding: AppBarMainBinding =
        AppBarMainBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setTitle(@StringRes titleStringId: Int) {
        binding.appBarTitle.setText(titleStringId)
    }

    fun setTitleColor(@ColorRes colorId: Int) {
        binding.appBarTitle.setTextColor(ContextCompat.getColor(context, colorId))
    }

    fun changeBackgroundColor(@ColorRes colorId: Int) {
        binding.appBarBackgroundV.setBackgroundColor(ContextCompat.getColor(context, colorId))
    }

    fun setTitleString(titleString: String) {
        binding.appBarTitle.setSingleLine()
        binding.appBarTitle.isSelected = true
        binding.appBarTitle.text = titleString
    }

    fun setTitleCenter(titleCenter: Boolean) {
        binding.appBarTitle.gravity = when (titleCenter) {
            true -> Gravity.START
            else -> Gravity.CENTER
        }
    }

    private fun setBackButtonAction(backButtonAction: (() -> Unit)?) {
        binding.appBarBackButton.setOnClickListener {
            backButtonAction?.invoke()
        }
    }

    private fun useBackButton(toBeUsed: Boolean) {
        binding.appBarBackButton.visibility = when (toBeUsed) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun useBackButton(toBeUsed: Boolean, backButtonAction: (() -> Unit)?) {
        useBackButton(toBeUsed)
        setBackButtonAction(backButtonAction)
    }

    fun useBackButton(toBeUsed: Boolean, backButtonAction: (() -> Unit)?, @DrawableRes icon: Int) {
        useBackButton(toBeUsed)
        setBackButtonAction(backButtonAction)
        changeBackButtonDrawable(icon)
    }

    private fun changeBackButtonDrawable(@DrawableRes icon: Int) {
        binding.appBarBackButton.setImageResource(icon)
    }

    private fun showButtonOneWithText(
        text: String,
        @DrawableRes icon: Int,
        action: (() -> Unit)?
    ) {
        binding.constraintBtnSort.visibility = View.VISIBLE
        binding.btnSort.setImageResource(icon)
        binding.txtSort.text = text
        binding.constraintBtnSort.setOnClickListener {
            action?.invoke()
        }
    }

    private fun showButtonTwoWithText(
        text: String,
        @DrawableRes icon: Int,
        action: (() -> Unit)?
    ) {
        binding.constraintBtnFilter.visibility = View.VISIBLE
        binding.btnFilter.setImageResource(icon)
        binding.txtFilter.text = text
        binding.constraintBtnFilter.setOnClickListener {
            action?.invoke()
        }
    }

    fun showButtonSortAndFilter(
        text: String,
        text2: String,
        @DrawableRes icon: Int,
        @DrawableRes icon2: Int,
        action1: (() -> Unit)?,
        action2: (() -> Unit)?
    ) {
        // sort
        showButtonOneWithText(text, icon, action1)
        // filter
        showButtonTwoWithText(text2, icon2, action2)
    }
    fun showTextRight(
        text: String,
        @DrawableRes icon: Int,
        action1: (() -> Unit)?,
    ) {
        // sort
        showButtonOneWithText(text, icon, action1)
    }

    fun showButtonOneWithoutImage(text: String, action: (() -> Unit)?) {
        binding.btnOneWithoutImage.visibility = View.VISIBLE
        binding.btnOneWithoutImage.text = text
        binding.btnOneWithoutImage.setOnClickListener {
            action?.invoke()
        }
    }
}
