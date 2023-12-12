package com.visualinnovate.almursheed.common

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.visualinnovate.almursheed.R

class CustomButton(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

//    private val binding: CustomButtonViewBinding =
//        CustomButtonViewinflate(LayoutInflater.from(context))

    // Define a listener interface

    private var button: Button
    private var loading: ProgressBar
    init {
        //  addView(root)
        View.inflate(context, R.layout.custom_button_view, this)
        button = findViewById(R.id.button)
        loading = findViewById(R.id.loading)
        rootView.setOnClickListener(null)
        loading.setOnClickListener(null)
        button.setOnClickListener {
            handleButtonAnimation()
            this.callOnClick()
        }

//

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.customButton)
        // Set the text attribute

        setCustomBackground(typedArray)
        setCustomBackgroundTint(typedArray)
        setCustomTextColor(typedArray)
        setCustomLayoutParams(typedArray)
        setText(typedArray)
        setTextSize(typedArray)
        setStyle(attrs)
        setLeftDrawable(typedArray)
        setRightDrawable(typedArray)

        typedArray.recycle()
    }

    private fun setLeftDrawable(typedArray: TypedArray) {
        val customLeftDrawable = typedArray.getResourceId(
            R.styleable.customButton_leftDrawable,
            R.drawable.ic_back,
        )
        if (customLeftDrawable != R.drawable.ic_back) {
            button.setCompoundDrawablesWithIntrinsicBounds(customLeftDrawable, 0, 0, 0)
            button.setPadding(8, 0, 8, 0)
        }
    }

    private fun setRightDrawable(typedArray: TypedArray) {
        val customRightDrawable = typedArray.getResourceId(
            R.styleable.customButton_rightDrawable,
            R.drawable.ic_back,
        )
        if (customRightDrawable != R.drawable.ic_back) {
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, customRightDrawable, 0)
            button.setPadding(8, 0, 8, 0)
        }
    }

    private fun setTextSize(typedArray: TypedArray) {
        val textSize = typedArray.getDimension(
            R.styleable.customButton_textSize,
            14f,
        )
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    private fun setStyle(attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.customButton, 0, R.style.customButtonStyle)
        try {
            val customBackground = styledAttributes.getResourceId(
                R.styleable.customButton_background,
                R.drawable.ic_back,
            )
            if (customBackground != R.drawable.ic_back) {
                button.setBackgroundResource(customBackground)
            }
            // Handle other custom attributes as needed
        } finally {
            styledAttributes.recycle()
        }
    }

    private fun setCustomLayoutParams(typedArray: TypedArray) {
        val height = typedArray.getInt(R.styleable.customButton_custom_layout_height, 200)
        val width = typedArray.getInt(R.styleable.customButton_custom_layout_width, 50)
        layoutParams = ViewGroup.LayoutParams(
            width,
            height,
        )
    }

    private fun setCustomTextColor(typedArray: TypedArray) {
        val customTextColor = typedArray.getColor(
            R.styleable.customButton_textColor,
            ContextCompat.getColor(context, android.R.color.black),
        )
        button.setTextColor(customTextColor)
    }

    private fun setCustomBackgroundTint(typedArray: TypedArray) {
        val customBackgroundTint = typedArray.getColor(
            R.styleable.customButton_backgroundTint,
            ContextCompat.getColor(context, android.R.color.transparent),
        )
        if (customBackgroundTint != ContextCompat.getColor(context, android.R.color.transparent)) {
            ViewCompat.setBackgroundTintList(button, ColorStateList.valueOf(customBackgroundTint))
        }
    }

    private fun setCustomBackground(typedArray: TypedArray) {
        val customBackground = typedArray.getResourceId(
            R.styleable.customButton_background,
            R.drawable.ic_back,
        )
        if (customBackground != R.drawable.ic_back) {
            button.setBackgroundResource(customBackground)
        }
    }

    private fun setText(typedArray: TypedArray) {
        val text = typedArray.getString(R.styleable.customButton_text)
        button.text = text
    }

    private fun handleButtonAnimation() {
        if (loading.visibility == View.GONE) {
            showProgressWithAnimation()
        } else {
            hideProgressWithAnimation()
        }
    }

    private fun showProgressWithAnimation() {
        val cx = button.width / 2
        val cy = button.height / 2

        val initialRadius = button.width.coerceAtLeast(button.height) / 2.0f

        // Reverse circular reveal animation
        val anim = ViewAnimationUtils.createCircularReveal(button, cx, cy, initialRadius, 0f)
        anim.duration = 200

        anim.start()
        button.gone()
        loading.visible()
    }

    private fun hideProgressWithAnimation() {
        val cx = button.width / 2
        val cy = button.height / 2

        val finalRadius = button.width.coerceAtLeast(button.height) / 2.0f

        // Circular reveal animation
        ViewCompat.setElevation(button, ViewCompat.getElevation(button))

        val anim = ViewAnimationUtils.createCircularReveal(button, cx, cy, 0f, finalRadius)
        anim.duration = 200
        button.visible()
        loading.gone()
        anim.start()
    }

    fun handleBtnAnimation() {
        handleButtonAnimation()
    }
}
