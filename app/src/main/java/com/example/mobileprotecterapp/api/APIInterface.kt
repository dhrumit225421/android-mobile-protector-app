package com.example.mobileprotecterapp.api

import com.example.mobileprotecterapp.model.ResponseLogin
import com.example.mobileprotecterapp.model.User
import retrofit2.http.*


interface APIInterface {

    @POST("accounts/signin/")
    suspend fun userLogin(@Body request: User): ResponseLogin
}
