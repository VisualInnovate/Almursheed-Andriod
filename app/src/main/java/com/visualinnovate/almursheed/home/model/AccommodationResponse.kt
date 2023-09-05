package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class AccommodationResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("accommmoditions")
    val accommodations: List<AccommodationItem?>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class Photo(

    @field:SerializedName("photos")
    val photos: List<PhotosItem?>? = null
)

data class Address(

    @field:SerializedName("gb")
    val gb: String? = null
)

data class Description(

    @field:SerializedName("gb")
    val gb: String? = null
)

data class AccommodationItem(

    @field:SerializedName("country")
    val country: Country? = null,

    @field:SerializedName("address")
    val address: LocalizedName? = null,

    @field:SerializedName("info_status")
    val infoStatus: Int? = null,

    @field:SerializedName("owner_info")
    val ownerInfo: LocalizedName? = null,

    @field:SerializedName("description")
    val description: LocalizedName? = null,

    @field:SerializedName("media")
    val media: List<MediaItem?>? = null,

    // @field:SerializedName("pictures")
    // val pictures: Pictures? = null,

    @field:SerializedName("name")
    val name: LocalizedName? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state_id")
    val stateId: Int? = null,

    @field:SerializedName("state")
    val state: State? = null,

    @field:SerializedName("aval_status")
    val avalStatus: Int? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("city_id")
    val cityId: Int? = null
)

data class PhotosItem(

    @field:SerializedName("manipulations")
    val manipulations: List<Any?>? = null,

    @field:SerializedName("order_column")
    val orderColumn: Int? = null,

    @field:SerializedName("file_name")
    val fileName: String? = null,

    @field:SerializedName("model_type")
    val modelType: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("model_id")
    val modelId: Int? = null,

    @field:SerializedName("custom_properties")
    val customProperties: List<Any?>? = null,

    @field:SerializedName("uuid")
    val uuid: String? = null,

    @field:SerializedName("conversions_disk")
    val conversionsDisk: String? = null,

    @field:SerializedName("disk")
    val disk: String? = null,

    @field:SerializedName("size")
    val size: Int? = null,

    @field:SerializedName("generated_conversions")
    val generatedConversions: List<Any?>? = null,

    @field:SerializedName("responsive_images")
    val responsiveImages: List<Any?>? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

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

data class OwnerInfo(

    @field:SerializedName("gb")
    val gb: String? = null
)

data class Name(

    @field:SerializedName("gb")
    val gb: String? = null
)
