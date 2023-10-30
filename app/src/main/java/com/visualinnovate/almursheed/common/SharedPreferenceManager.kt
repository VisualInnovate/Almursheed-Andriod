package com.visualinnovate.almursheed.common

import com.orhanobut.hawk.Hawk
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.utils.Constant

object SharedPreference {

    fun setNotificationId(notificationId: Int?) {
        Hawk.put(Constant.NOTIFICATION_ID, notificationId)
    }

    fun getNotificationId(): Int? {
        return Hawk.get(Constant.NOTIFICATION_ID)
    }

    fun saveUser(user: User?) {
        setUserRole(user?.type)
        Hawk.put(Constant.USER, user)
    }

    fun getUser(): User? {
        return Hawk.get(Constant.USER)
    }

    private fun setUserRole(role: String?) {
        Hawk.put(Constant.USER_Role, role)
    }

    fun getUserRole(): String? {
        return Hawk.get(Constant.USER_Role)
    }

    fun setUserToken(token: String?) {
        Hawk.put(Constant.USER_TOKEN, token)
    }

    fun getUserToken(): String? {
        return Hawk.get(Constant.USER_TOKEN)
    }

    fun setStateId(stateId: Int?) {
        Hawk.put(Constant.STATE_ID, stateId)
    }

    fun getStateId(): Int? {
        return Hawk.get(Constant.STATE_ID)
    }

    fun saveDeviceToken(currentToken: String) {
        Hawk.put("DeviceToken", currentToken)
    }

    fun getDeviceToken(): String? {
        return Hawk.get<String>("DeviceToken")
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        Hawk.put(Constant.LOGGED_IN, isLoggedIn)
    }

    fun getUserLoggedIn(): Boolean {
        return Hawk.get(Constant.LOGGED_IN)?:false
    }

    fun setLanguage(language: String) {
        Hawk.put(Constant.KEY_LANGUAGE, language)
    }

    fun getLanguage(): String {
        return Hawk.get(Constant.KEY_LANGUAGE) ?: "en"
    }

//    fun saveCityId(city: Int?) {
//        Hawk.put(Constant.KEY_CITY_TOURIST_CHOOSSED, city)
//    }
//
//    fun getCityId(): Int {
//        return Hawk.get(Constant.KEY_CITY_TOURIST_CHOOSSED) ?: 999999
//    }

//    fun saveCurrentUser(currentUser: UserModel) {
//        userModel = currentUser
//        Hawk.put("currentUser", currentUser)
//    }
//
//    fun getCurrentUser(): UserModel? {
//        return Hawk.get<UserModel>("currentUser")
//    }
//
//    fun signOut() {
//        userModel = UserModel()
//        Hawk.put("currentUser", null)
//    }
//
//    fun isFirstInstall(): Boolean? {
//        return Hawk.get("firstInstall")
//    }
//
//    fun isFirstInstall(isFirst: Boolean) {
//        Hawk.put("firstInstall", isFirst)
//    }
}
