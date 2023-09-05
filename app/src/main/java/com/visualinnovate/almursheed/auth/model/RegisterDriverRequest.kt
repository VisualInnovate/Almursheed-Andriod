package com.visualinnovate.almursheed.auth.model

data class RegisterDriverRequest(
    var name: String? = null,
    var country_id: Int? = null,
    var state_id: Int? = null,
    var gender: Int? = null,
    var password: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var bio: String? = null,
    var car_number: String? = null,
    var driver_licence_number: String? = null,
    var gov_id: String? = null,

    var car_photos: String? = null,
    var carImagesList: List<String>? = null,
    var personal_pictures: String? = null,
    var languages: List<Int>? = null
)
