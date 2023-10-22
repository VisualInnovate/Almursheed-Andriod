package com.visualinnovate.almursheed.network

import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.auth.model.UserResponse
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersModel
import com.visualinnovate.almursheed.commonView.myOrders.models.RateResponse
import com.visualinnovate.almursheed.commonView.price.models.PricesResponse
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
        @Query("password") password: String?,
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
        @Query("password_confirmation") passwordConfirmation: String?,
    ): Response<MessageResponse>

    @GET("guides/latest")
    suspend fun getLatestGuide(
        @Query("state_id") stateId: Int?
    ): Response<DriversAndGuidesListResponse>

    @GET("drivers/latest") // https://mursheed.visualinnovate.net/api/drivers/latest?stateId=0
    suspend fun getLatestDriver(
        @Query("state_id") stateId: Int?,
    ): Response<DriversAndGuidesListResponse>

    @GET("drivers/all")
    suspend fun getAllDrivers(
        @Query("country_id") countryId: String?,
        @Query("state_id") cityId: String?,
        @Query("car_type") carType: String?,
        @Query("car_model") carModel: String?,
        @Query("name") searchData: String?,
        @Query("price") price: String?,
        @Query("rate") rate: String?,
    ): Response<DriversAndGuidesListResponse>

    @POST("drivers/get-driver-by-city")
    suspend fun getAllDriversByDistCityId(
        @Query("city_id") cityId: Int,
    ): Response<DriversAndGuidesListResponse>

    @GET("guides/all")
    suspend fun getAllGuides(
        @Query("country_id") countryId: String?,
        @Query("state_id") cityId: String?,
        @Query("language_id") language_id: String?,
        @Query("name") searchData: String?,
        @Query("price") price: String?,
        @Query("rate") rate: String?,
    ): Response<DriversAndGuidesListResponse>

    @POST("guides/get-guide-by-city")
    suspend fun getAllGuidesByDistCityId(
        @Query("city_id") cityId: Int,
    ): Response<DriversAndGuidesListResponse>

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
    suspend fun getAllAccommodation(
        @Query("country_id") countryId: String?,
        @Query("state_id") cityId: String?,
        @Query("category_id") category: String?,
        @Query("rooms") roomCCount: String?,
        @Query("name") searchData: String?,
        @Query("price") price: String?,
    ): Response<AccommodationResponse>

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
        @Part("country_id") countryId: RequestBody,
        @Part("state_id") stateId: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        // @Part("bio") bio: RequestBody,
        @Part("gov_id") govId: RequestBody,
        // @Part("languages[]") languages: List<Int>
        // @Part personal_pictures: MultipartBody.Part,
        @Part("driver_licence_number") driver_licence_number: RequestBody,
        // @Part car_photos: List<MultipartBody.Part>,
        @Part("car_number") carNumber: RequestBody,
        @Part("car_type") carType: RequestBody,
        @Part("car_brand_name") car_brand_name: RequestBody,
        @Part("car_manufacturing_date") car_manufacturing_date: RequestBody,
    ): Response<UpdateResponse>

    @Multipart
    @POST("tourists/update")
    suspend fun updateTourist(
        @Part("name") name: RequestBody,
        @Part("dest_city_id") destCityId: Int,
        @Part("gender") gender: RequestBody,
        @Part("nationality") nationality: RequestBody,
        // @Part personal_pictures: MultipartBody.Part,
        // @Body requestBody: RequestBody,
    ): Response<UpdateResponse>

    @Multipart
    @POST("tourists/update")
    suspend fun updateLocationTourist(
        @Part("country_id") countryId: RequestBody,
        @Part("state_id") stateId: RequestBody,
    ): Response<UpdateResponse>

    @Multipart
    @POST("guides/update")
    suspend fun updateGuide(
        @Part("name") name: RequestBody,
        @Part("country_id") countryId: Int?,
        @Part("state_id") destCityId: Int?,
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

    @POST("price-services")
    suspend fun addNewPrice(
        @Body requestBody: RequestBody,
    ): Response<Void>

    @GET("price-services")
    suspend fun getUserPrices(): Response<PricesResponse>

    @POST("orders/myOrders")
    suspend fun getMyOrders(
        @Query("status") status: String,
    ): Response<MyOrdersModel>

    @POST("rating/create")
    suspend fun addRate(
        @Query("tourist_rating") touristRating: Int,
        @Query("comment") comment: String,
        @Query("reviewable_id") reviewableId: Int,
        @Query("type") type: Int,
    ): Response<RateResponse>

    @POST("orders/status{id}")
    suspend fun changeStatus(
        @Path("id") orderId: Int?,
        @Query("status") status: String
    ): Response<Void>

    @POST("favourite/create")
    suspend fun addAndRemoveFavorite(
        @Query("service_id") serviceId: String,
        @Query("type") type: String
    ): Response<FavoriteResponse>
}
