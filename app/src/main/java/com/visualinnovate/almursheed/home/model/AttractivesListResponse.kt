package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class AttractivesListResponse(

    @field:SerializedName("attractives")
    val attractives: List<AttractivesItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class AttractivesItem(

    @field:SerializedName("country")
    val country: Country? = null,

    @field:SerializedName("description")
    val description: LocalizedName? = null,

    @field:SerializedName("media")
    val media: List<MediaItem?>? = null,

    @field:SerializedName("long")
    val jsonMemberLong: String? = null,

    @field:SerializedName("pictures")
    val pictures: Pictures? = null,

    @field:SerializedName("name")
    val name: LocalizedName? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state")
    val state: Any? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("lat")
    val lat: String? = null,

    @field:SerializedName("city_id")
    val cityId: Int? = null
)

data class PersonalPicturesItem(

    // @field:SerializedName("manipulations")
    // val manipulations: List<Any?>? = null,

    @field:SerializedName("order_column")
    val orderColumn: Int? = null,

    @field:SerializedName("file_name")
    val fileName: String? = null,

    @field:SerializedName("model_type")
    val modelType: String? = null,

    @field:SerializedName("model_id")
    val modelId: Int? = null,

    // @field:SerializedName("custom_properties")
    // val customProperties: List<Any?>? = null,

    @field:SerializedName("uuid")
    val uuid: String? = null,

    @field:SerializedName("conversions_disk")
    val conversionsDisk: String? = null,

    @field:SerializedName("disk")
    val disk: String? = null,

    @field:SerializedName("size")
    val size: Int? = null,

    // @field:SerializedName("generated_conversions")
    // val generatedConversions: List<Any?>? = null,

    // @field:SerializedName("responsive_images")
    // val responsiveImages: List<Any?>? = null,

    @field:SerializedName("mime_type")
    val mimeType: String? = null,

    @field:SerializedName("original_url")
    val originalUrl: String? = null,

    @field:SerializedName("preview_url")
    val previewUrl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("collection_name")
    val collectionName: String? = null
)

data class Language(

    @field:SerializedName("is_active")
    val isActive: Int? = null,

    @field:SerializedName("native")
    val jsonMemberNative: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("is_rtl")
    val isRtl: Int? = null,

    @field:SerializedName("lang")
    val lang: String? = null,

    @field:SerializedName("iso_code")
    val isoCode: String? = null,

    @field:SerializedName("is_default")
    val isDefault: Int? = null
)


data class Pictures(

    @field:SerializedName("photos")
    val photos: List<PhotosItem?>? = null,

    @field:SerializedName("personal_pictures")
    val personalPictures: List<PersonalPicturesItem?>? = null,

    @field:SerializedName("car_photos")
    val carPhotos: List<CarPhotosItem?>? = null
)

data class LanguagesAbleItem(

    @field:SerializedName("languagesable_id")
    val languagesAbleId: Int? = null,

    @field:SerializedName("language")
    val language: Language? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("languagesable_type")
    val languagesableType: String? = null,

    @field:SerializedName("language_id")
    val languageId: Int? = null
)

data class CarPhotosItem(

    // @field:SerializedName("manipulations")
    // val manipulations: List<Any?>? = null,

    @field:SerializedName("order_column")
    val orderColumn: Int? = null,

    @field:SerializedName("file_name")
    val fileName: String? = null,

    @field:SerializedName("model_type")
    val modelType: String? = null,

    @field:SerializedName("model_id")
    val modelId: Int? = null,

    // @field:SerializedName("custom_properties")
    // val customProperties: List<Any?>? = null,

    @field:SerializedName("uuid")
    val uuid: String? = null,

    @field:SerializedName("conversions_disk")
    val conversionsDisk: String? = null,

    @field:SerializedName("disk")
    val disk: String? = null,

    @field:SerializedName("size")
    val size: Int? = null,

    // @field:SerializedName("generated_conversions")
    // val generatedConversions: List<Any?>? = null,

    // @field:SerializedName("responsive_images")
    // val responsiveImages: List<Any?>? = null,

    @field:SerializedName("mime_type")
    val mimeType: String? = null,

    @field:SerializedName("original_url")
    val originalUrl: String? = null,

    @field:SerializedName("preview_url")
    val previewUrl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("collection_name")
    val collectionName: String? = null
)
