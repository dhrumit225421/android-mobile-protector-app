package com.shreejipackaging.data

import androidx.lifecycle.MutableLiveData
import com.example.mobileprotecterapp.BuildConfig
import com.example.mobileprotecterapp.utils.API_ERROR_MSG
import com.shreejipackaging.api.ApiError
import java.net.ConnectException

sealed class ResponseManager<out T : Any> {
    data class Success<T : Any>(val data: T) : ResponseManager<T>()
    data class Error(val message: String = "", val stringID: Int = 0) : ResponseManager<Nothing>()
    data class Unauthenticated(val message: String = "", val stringID: Int = 0) :
        ResponseManager<Nothing>()

    data class Loading<T : Any>(val data: T) : ResponseManager<T>()
    companion object {
        fun <T : Any> manageException(
            liveData: MutableLiveData<ResponseManager<T>>,
            exception: Exception
        ) {
            try {
                if (exception is ConnectException) {
                    liveData.value =
                        Error(BuildConfig.API_CONNECTION_ERROR_MSG)
                } else {
                    when {
                        (exception as retrofit2.adapter.rxjava.HttpException).code() == 400 -> {
                            liveData.value = Error(ApiError(exception).message)
                        }
                        exception.code() == 401 -> {
                            liveData.value =
                                Unauthenticated(BuildConfig.API_UNAUTHENTICATED_ERROR_MSG)
                        }
                        else -> {
                            liveData.value = Error(API_ERROR_MSG)
                        }
                    }
                }
            } catch (e: Exception) {
                liveData.value = Error(BuildConfig.API_PARSE_ERROR_MSG)
            }
        }
    }
}