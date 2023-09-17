package com.visualinnovate.almursheed.home.model

data class RequestCreateOrder(
    val user_type: Int,
    val user_id: Int,
    val order: Order,
    val order_details: List<OrderDetail>
)

data class Order(
    val trip_type: Int,
    val start_date: String,
    val end_date: String,
    val country_id: Int,
    val lat: String,
    val longitude: String
)

data class OrderDetail(
    val date: String,
    val city_id: Int
)

