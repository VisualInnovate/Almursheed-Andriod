package com.visualinnovate.almursheed.commonView.bottomSheets.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooserItemModel(
    var id: String? = "",
    var name: String? = "",
    var isSelected: Boolean = false
) : Parcelable