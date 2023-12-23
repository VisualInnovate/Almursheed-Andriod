package com.visualinnovate.almursheed.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.visualinnovate.almursheed.utils.ResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response

abstract class BaseApiResponse(application: Application) : AndroidViewModel(application) {

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
                        code / 100 == 4 -> parseError(response.errorBody()!!.string())
                        code / 100 == 5 -> {
                            errorMessage = "something went wrong"
                        }
                        else -> {
                            errorMessage = "something went wrong"
                        }
                    }
                    emit(ResponseHandler.StopLoading)
                    emit(ResponseHandler.Error(errorMessage))
                }
            } catch (e: Exception) {
                Log.d("MyDebugData","BaseApiResponse : safeApiCall :  " + e.localizedMessage)
                emit(ResponseHandler.StopLoading)
                emit(ResponseHandler.Error("something went wrong"))
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
