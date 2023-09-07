package com.visualinnovate.almursheed.common

import com.orhanobut.hawk.Hawk
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.utils.Constant

object SharedPreference {

    fun saveUser(user: User?) {
        Hawk.put(Constant.USER, user)
    }

    fun getUser(): User? {
        return Hawk.get(Constant.USER)
    }

    fun saveUserToken(token: String?) {
        Hawk.put(Constant.USER_TOKEN, token)
    }

    fun getUserToken(): String? {
        return Hawk.get(Constant.USER_TOKEN)
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
