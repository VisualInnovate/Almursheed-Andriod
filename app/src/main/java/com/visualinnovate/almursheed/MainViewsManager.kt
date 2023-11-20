package com.visualinnovate.almursheed

interface MainViewsManager {
    fun showLoading()
    fun hideLoading()

    fun showBottomNav()
    fun hideBottomNav()
    fun changeSelectedBottomNavListener(id: Int)
}
