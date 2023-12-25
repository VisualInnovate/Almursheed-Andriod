package com.visualinnovate.almursheed.home.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.visualinnovate.almursheed.auth.model.LanguageItem
import com.visualinnovate.almursheed.commonView.price.models.PriceItem
import kotlinx.parcelize.Parcelize

data class DriversAndGuidesListResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @SerializedName(value = "drivers", alternate = ["guides", "driver", "guide"]) // guides
    var drivers: List<DriverAndGuideItem?>? = null,
)

@Parcelize
data class DriverAndGuideItem(

    @field:SerializedName("state_name")
    val stateName: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state_id")
    val stateId: Int? = null,

    @field:SerializedName("image_background") // image_background
    val imageBackground: String? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("car_model")
    val carModel: String? = null,

    //@field:SerializedName("car_photo")
    @field:SerializedName(value = "car_photo", alternate = ["car_photos"]) // guides
    val carPhoto: List<String?>? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("state")
    val state: String? = null,

    @field:SerializedName("car_date")
    val carDate: String? = null,

    @field:SerializedName("languages")
    val languages: List<LanguageItem?>? = null,

    @field:SerializedName("car_type")
    val carType: String? = null,

    @field:SerializedName("personal_photo") // personal_photo
    val personalPhoto: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("admin_rating")
    val adminRating: String? = null,

    @field:SerializedName("nationality")
    val nationality: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("ratings_sum")
    val ratingsSum: Int? = null,

    // @field:SerializedName("total_rating")
    @field:SerializedName(value = "total_rating", alternate = ["total_rate"])
    val totalRating: String? = null,

    @field:SerializedName("ratings_count")
    val ratingsCount: Int? = null,

    @field:SerializedName("count_rate")
    val count_rate: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("is_favourite")
    var isFavourite: Boolean? = null,

    @field:SerializedName(value = "price_services", alternate = ["priceServices"])
    val priceServices: ArrayList<PriceItem?>? = null,
): Parcelable {
    fun getLanguage(): ArrayList<String> {
        val lang: ArrayList<String> = ArrayList()
        this.languages?.forEach {
            lang.add(it?.lang.toString())
        }
        return lang
    }
}
