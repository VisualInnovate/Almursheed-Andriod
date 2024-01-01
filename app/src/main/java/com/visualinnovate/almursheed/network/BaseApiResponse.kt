package com.visualinnovate.almursheed.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.utils.ResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response

abstract class BaseApiResponse(private val application: Application) :
    AndroidViewModel(application) {

    private var errorMessage = ""

    // safe Api Call function (Unit)
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<ResponseHandler<T?>> =
        flow {
            emit(ResponseHandler.Loading)
            try {
                val response = apiCall()
                val code = response.code()
                if (response.isSuccessful) {
                    emit(ResponseHandler.StopLoading)
                    emit(ResponseHandler.Success(response.body()))
                } else {
                    when {
                        code == 400 -> {
                            errorMessage =
                                application.getString(R.string.no_price_for_this_country_please_contact_administrator)
                        }

                        code == 401 -> {
                            errorMessage =
                                application.getString(R.string.email_or_password_is_not_correct)
                        }

                        code == 402 -> {
                            errorMessage =
                                application.getString(R.string.email_must_be_verified_first)
                        }

                        code == 422 -> {
                            errorMessage =
                                application.getString(R.string.validation_error_email_and_password_are_required)
                        }

                        code == 404 -> {
                            errorMessage = application.getString(R.string.driver_or_guide_not_found)
                        }

                        code == 409 -> {
                            errorMessage =
                                application.getString(R.string.there_is_no_price_for_this_city)
                        }

                        code == 500 -> {
                            errorMessage =
                                application.getString(R.string.server_error_or_other_error_ask_user_to_try_again_later)
                        }

                        else -> {
                            errorMessage = application.getString(R.string.something_went_wrong)
                        }
                    }
                    emit(ResponseHandler.StopLoading)
                    emit(ResponseHandler.Error(errorMessage))
                }
            } catch (e: Exception) {
                Log.d("MyDebugData", "BaseApiResponse : safeApiCall :  " + e.localizedMessage)
                emit(ResponseHandler.StopLoading)
                emit(ResponseHandler.Error(application.getString(R.string.something_went_wrong)))
            }
        }.flowOn(Dispatchers.IO)

    private fun parseError(error: String) {
        val jsonObject = JSONObject(error)
        errorMessage = jsonObject.getString("message")
    }

//    // safe Api Call function
//    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<ResponseHandler<T>> =
//        flow {
//            emit(ResponseHandler.Loading)
//            try {
//                val response = apiCall()
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    body?.let {
//                        emit(ResponseHandler.Success(body))
//                    }
//                } else {
//                    val jsonObject = JSONObject(response.errorBody()!!.string())
//                    emit(ResponseHandler.Error(jsonObject.getString("message")))
//                }
//            } catch (e: Exception) {
//                emit(ResponseHandler.Error(e.message ?: e.toString()))
//            }
//        }.flowOn(Dispatchers.IO)
}

/*
suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ResponseHandler<T> {
        ResponseHandler.Loading
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return ResponseHandler.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    // safe Api Call function
    private fun <T> error(errorMessage: String): ResponseHandler<T> =
        ResponseHandler.Error("Api call failed $errorMessage")
 */
