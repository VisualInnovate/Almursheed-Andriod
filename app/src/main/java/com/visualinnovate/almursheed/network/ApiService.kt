package com.visualinnovate.almursheed.network

import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.auth.model.UserResponse
import com.visualinnovate.almursheed.home.model.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

//    @Multipart
//    @POST("drivers/create")
//    suspend fun registerDriver(
//        @Part("name") name: RequestBody,
//        @Part("country_id") country_id: RequestBody,
//        @Part("state_id") state_id: RequestBody,
//        @Part("gender") gender: RequestBody,
//        @Part("password") password: RequestBody,
//        @Part("email") email: RequestBody,
//        @Part("phone") phone: RequestBody,
//        @Part("bio") bio: RequestBody,
//        @Part("car_number") car_number: RequestBody,
//        @Part("driver_licence_number") driver_licence_number: RequestBody,
//        @Part("gov_id") gov_id: RequestBody,
//        @Part car_photos: List<MultipartBody.Part>,
//        @Part personal_pictures: MultipartBody.Part, // @Part image: MultipartBody.Part,
//        @Part("languages[]") languages: List<Int>
//    ): Response<DriverResponse>
//

    @POST("drivers/create")
    suspend fun registerDriver(
        @Body requestBody: RequestBody,
    ): Response<UserResponse>

    @POST("guides/create")
    suspend fun registerGuide(
        @Body requestBody: RequestBody,
    ): Response<UserResponse>

    @POST("tourists/create")
    suspend fun registerTourist(
        @Body requestBody: RequestBody,
    ): Response<UserResponse>

    @POST("login-clients")
    suspend fun login(
        @Query("email") email: String?,
        @Query("password") password: String?
    ): Response<UserResponse>

    @POST("password/send-otp")
    suspend fun forgetPassword(
        @Query("email") email: String?,
    ): Response<MessageResponse>

    @POST("password/validate-otp")
    suspend fun validateOTP(
        @Body requestBody: RequestBody,
    ): Response<MessageResponse>

    @POST("password/reset")
    suspend fun resetPassword(
        @Query("otp") otp: String?,
        @Query("email") email: String?,
        @Query("password") newPassword: String?,
        @Query("password_confirmation") password_confirmation: String?,
    ): Response<MessageResponse>

    @GET("guides/latest/{id}")
    suspend fun getLatestGuide(@Path("id") cityId: Int): Response<GuideListResponse>

    @GET("drivers/latest/{id}") // https://mursheed.visualinnovate.net/api/drivers/latest/42
    suspend fun getLatestDriver(@Path("id") cityId: Int): Response<DriverListResponse>

    @GET("drivers/all")
    suspend fun getAllDrivers(): Response<DriverListResponse>

    @GET("guides/all")
    suspend fun getAllGuides(): Response<GuideListResponse>

    @GET("offers")
    suspend fun getAllOffers(): Response<OfferResponse>

    @GET("attracives") // https://mursheed.visualinnovate.net/api/attracives
    suspend fun getAttractives(): Response<AttractivesListResponse>

    @GET("attracives/{id}") // https://mursheed.visualinnovate.net/api/attracives
    suspend fun getAttractiveDetailsById(
        @Path("id") id: Int,
    ): Response<AttraciveDetailsResponse>

    @GET("offers/{id}")
    suspend fun getOfferDetailsById(
        @Path("id") offerId: Int,
    ): Response<OfferDetailsResponse>

    @GET("accommodition")
    suspend fun getAllAccommodation(): Response<AccommodationResponse>

    @GET("accommodition/{id}")
    suspend fun getAccommodationDetailsById(
        @Path("id") id: Int,
    ): Response<AccommodationDetailsResponse>

    @GET("drivers/{id}")
    suspend fun getDriverDetailsById(
        @Path("id") id: Int,
    ): Response<DriverDetailsResponse>

    @GET("guides/{id}")
    suspend fun getGuideDetailsById(
        @Path("id") id: Int,
    ): Response<GuideDetailsResponse>

    @GET("flights")
    suspend fun getAllFlights(): Response<FlightResponse>

    @Multipart
    @POST("drivers/update")
    suspend fun updateDriver(
        @Part("name") name: RequestBody,
        @Part("country_id") country_id: RequestBody,
        @Part("state_id") state_id: RequestBody,
        // @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("gov_id") gov_id: RequestBody,
        // @Part("languages[]") languages: List<Int>
        // @Part personal_pictures: MultipartBody.Part,
        @Part("driver_licence_number") driver_licence_number: RequestBody,
        // @Part car_photos: List<MultipartBody.Part>,
        @Part("car_number") car_number: RequestBody,
        @Part("car_type") car_type: RequestBody,
        @Part("car_brand_name") car_brand_name: RequestBody,
        @Part("car_manufacturing_date") car_manufacturing_date: RequestBody,
    ): Response<UpdateDriverResponse>
}
