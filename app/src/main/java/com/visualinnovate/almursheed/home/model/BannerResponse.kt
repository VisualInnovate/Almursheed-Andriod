package com.visualinnovate.almursheed.home.model

import com.google.gson.annotations.SerializedName

data class BannerResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("banners")
	val banners: List<BannersItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class BannersItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null,

	@field:SerializedName("pictures")
	val pictures: Pictures? = null,

	// @field:SerializedName("updated_at")
	// val updatedAt: String? = null,

	// @field:SerializedName("created_at")
	// val createdAt: String? = null,
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

data class MediaItem(

	@field:SerializedName("manipulations")
	val manipulations: List<Any?>? = null,

	@field:SerializedName("order_column")
	val orderColumn: Int? = null,

	@field:SerializedName("file_name")
	val fileName: String? = null,

	@field:SerializedName("model_type")
	val modelType: String? = null,

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
