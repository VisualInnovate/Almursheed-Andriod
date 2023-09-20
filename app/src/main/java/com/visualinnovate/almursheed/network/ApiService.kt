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

    @POST("generate-otp")
    suspend fun forgetPassword(
        @Query("identifier") email: String?,
        @Query("type") type: String?,
    ): Response<MessageResponse>

    @POST("validate-otp")
    suspend fun validateOTP(
        @Body requestBody: RequestBody,
    ): Response<MessageResponse>

    @POST("resset-password")
    suspend fun resetPassword(
        @Query("identifier") email: String?,
        @Query("otp") otp: String?,
        @Query("password") newPassword: String?,
        @Query("password_confirmation") password_confirmation: String?,
    ): Response<MessageResponse>

    @GET("guides/latest/{id}")
    suspend fun getLatestGuide(@Path("id") cityId: Int): Response<GuideListResponse>

    @GET("drivers/latest/{id}") // https://mursheed.visualinnovate.net/api/drivers/latest/42
    suspend fun getLatestDriver(
        @Path("id") cityId: Int
    ): Response<DriverListResponse>

    @GET("drivers/all")
    suspend fun getAllDrivers(): Response<DriverListResponse>

    @GET("drivers/get-driver-by-city")
    suspend fun getAllDriversByDistCityId(
        @Query("city_id") cityId: Int
    ): Response<DriverListResponse>

    @GET("guides/all")
    suspend fun getAllGuides(): Response<DriverListResponse>

    @GET("guides/get-guide-by-city")
    suspend fun getAllGuidesByDistCityId(
        @Query("city_id") cityId: Int
    ): Response<DriverListResponse>

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
        @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        // @Part("bio") bio: RequestBody,
        @Part("gov_id") gov_id: RequestBody,
        // @Part("languages[]") languages: List<Int>
        // @Part personal_pictures: MultipartBody.Part,
        @Part("driver_licence_number") driver_licence_number: RequestBody,
        // @Part car_photos: List<MultipartBody.Part>,
        @Part("car_number") car_number: RequestBody,
        @Part("car_type") car_type: RequestBody,
        @Part("car_brand_name") car_brand_name: RequestBody,
        @Part("car_manufacturing_date") car_manufacturing_date: RequestBody,
    ): Response<UpdateResponse>

    @Multipart
    @POST("tourists/update")
    suspend fun updateTourist(
        @Part("name") name: RequestBody,
        @Part("dest_city_id") dest_city_id: Int,
        @Part("gender") gender: RequestBody,
        @Part("nationality") nationality: RequestBody,
        // @Part personal_pictures: MultipartBody.Part,
//        @Body requestBody: RequestBody,
    ): Response<UpdateResponse>

    @Multipart
    @POST("guides/update")
    suspend fun updateGuide(
        @Part("name") name: RequestBody,
        @Part("country_id") country_id: Int?,
        @Part("state_id") dest_city_id: Int?,
        @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("bio") bio: RequestBody,
        // @Part personal_pictures: MultipartBody.Part,
        @Part("nationality") nationality: RequestBody,
        // @Body requestBody: RequestBody,
    ): Response<UpdateResponse>

    @POST("orders/create")
    suspend fun createOrder(
        // hire screen
        @Body requestCreateOrder: RequestCreateOrder,
    ): Response<CreateOrderResponse>

    @POST("orders/submit/{id}")
    suspend fun submitOrder(
        // hire screen
        @Path("id") id: Int,
    ): Response<MessageResponse>

}
