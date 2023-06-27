package com.shreejipackaging.api

import com.google.gson.JsonParser
import retrofit2.adapter.rxjava.HttpException

class ApiError constructor(error: Throwable) {
    var message = "An error occurred"

    init {
        if (error is HttpException) {
            try {
//                val errorJsonString = error.response()
//                    .errorBody()?.string()
                this.message = "Invalid data.Please try again!"
            } catch (e: Exception) {
                this.message = "Something want wrong"
            }

        } else {
            this.message = error.message ?: this.message
        }
    }
}