package com.visualinnovate.almursheed.auth.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("user")
    var user: User? = null,

    @field:SerializedName("status")
    var status: Boolean? = null,

    @field:SerializedName("token")
    var token: String? = null,
)

@Parcelize
data class User(

    @field:SerializedName("nationality")
    var nationality: String? = null,

    @field:SerializedName("gender")
    var gender: String? = null,

    // @field:SerializedName("dest_city_id")
    @SerializedName(value = "dest_city_id", alternate = ["dest_city_id "])
    var destCityId: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phone")
    var phone: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("notification_id")
    var notificationId: Int? = null,

    @field:SerializedName("state_id")
    var stateId: Int? = null,

    @field:SerializedName("type")
    var type: String? = null,

    @field:SerializedName("is_verified")
    var isVerified: Boolean? = null,

    @field:SerializedName("bio")
    var bio: String? = null,

    @field:SerializedName("country_id")
    var countryId: Int? = null,

    @field:SerializedName("des_city_id")
    var desCityId: Int? = null,

    @field:SerializedName("personal_photo") //personal_photo
    var personalPhoto: String? = null,

    @field:SerializedName("car_brand_name")
    var carBrandName: String? = null,

    @field:SerializedName("car_manufacturing_date")
    var carManufacturingDate: String? = null,

    @field:SerializedName("car_type")
    var carType: String? = null,

    @field:SerializedName("driver_licence_number")
    var licenceNumber: String? = null,

    @field:SerializedName("car_number")
    var carNumber: String? = null,

    @field:SerializedName("gov_id")
    var govId: String? = null,


    ) : Parcelable
