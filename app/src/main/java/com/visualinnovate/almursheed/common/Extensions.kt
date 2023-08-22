package com.visualinnovate.almursheed.common

import android.view.View

fun View.gone() = run { visibility = View.GONE }

fun View.visible() = run { visibility = View.VISIBLE }

fun View.invisible() = run { visibility = View.INVISIBLE }
