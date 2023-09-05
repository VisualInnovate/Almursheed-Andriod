package com.visualinnovate.almursheed.network

import com.visualinnovate.almursheed.auth.model.DriverResponse
import com.visualinnovate.almursheed.auth.model.LoginResponse
import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.auth.model.TouristResponse
import com.visualinnovate.almursheed.home.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("drivers/create")
    suspend fun registerDriver(
        @Part("name") name: RequestBody,
        @Part("country_id") country_id: RequestBody,
        @Part("state_id") state_id: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("password") password: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("car_number") car_number: RequestBody,
        @Part("driver_licence_number") driver_licence_number: RequestBody,
        @Part("gov_id") gov_id: RequestBody,
        @Part car_photos: List<MultipartBody.Part>,
        @Part personal_pictures: MultipartBody.Part, // @Part image: MultipartBody.Part,
        @Part("languages[]") languages: List<Int>
    ): Response<DriverResponse>

    @Multipart
    @POST("tourists/create")
    suspend fun registerTourist(
        @Part("name") name: RequestBody,
        @Part("country_id") country_id: RequestBody,
        @Part("state_id") state_id: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("password") password: RequestBody,
        @Part("email") email: RequestBody,
        @Part personal_pictures: MultipartBody.Part // @Part image: MultipartBody.Part,
    ): Response<TouristResponse>

    @POST("login-clients")
    suspend fun login(
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("type") type: Int?
    ): Response<LoginResponse>

    @POST("password/send-otp")
    suspend fun forgetPassword(
        @Query("email") email: String?
    ): Response<MessageResponse>

    @POST("password/validate-otp")
    suspend fun validateOTP(
        @Query("email") email: String?,
        @Query("otp") otp: String?
    ): Response<MessageResponse>

    @POST("password/reset")
    suspend fun resetPassword(
        @Query("otp") otp: String?,
        @Query("email") email: String?,
        @Query("password") newPassword: String?,
        @Query("password_confirmation") password_confirmation: String?
    ): Response<MessageResponse>

    @GET("guides/latest/{id}")
    suspend fun getLatestGuide(@Path("id") cityId: Int): Response<GuideResponse>

    @GET("drivers/latest/{id}") // https://mursheed.visualinnovate.net/api/drivers/latest/42
    suspend fun getLatestDriver(@Path("id") cityId: Int): Response<DriverLatestResponse>

    @GET("drivers")
    suspend fun getAllDrivers(): Response<DriverListResponse>

    @GET("guides")
    suspend fun getAllGuides(): Response<GuideResponse>

    @GET("offers")
    suspend fun getAllOffers(): Response<OfferResponse>

    @GET("attracives") // https://mursheed.visualinnovate.net/api/attracives
    suspend fun getAttractives(): Response<AttractivesListResponse>

    @GET("attracives/{id}") // https://mursheed.visualinnovate.net/api/attracives
    suspend fun getAttractiveDetailsById(
        @Path("id") id: Int
    ): Response<AttraciveDetailsResponse>

    @GET("offers/{id}")
    suspend fun getOfferDetailsById(
        @Path("id") offerId: Int
    ): Response<OfferDetailsResponse>

    @GET("accommodition")
    suspend fun getAllAccommodation(): Response<AccommodationResponse>

    @GET("accommodition/{id}")
    suspend fun getAccommodationDetailsById(
        @Path("id") id: Int
    ): Response<AccommodationDetailsResponse>

    @GET("drivers/{id}")
    suspend fun getDriverDetailsById(
        @Path("id") id: Int
    ): Response<DriverDetailsResponse>

    @GET("guides/{id}")
    suspend fun getGuideDetailsById(
        @Path("id") id: Int
    ): Response<GuideDetailsResponse>

    @GET("flights")
    suspend fun getAllFlights(): Response<FlightResponse>
}
