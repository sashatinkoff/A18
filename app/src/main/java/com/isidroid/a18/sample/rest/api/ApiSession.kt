package com.isidroid.squareoffsdk.network.api

import com.isidroid.a18.rest.ApiFactory
import com.isidroid.squareoffsdk.network.response.RegisterRequest
import com.isidroid.squareoffsdk.network.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiSession {
    @POST("registrations")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    companion object {
        fun create() = ApiFactory.create(ApiSession::class.java)
    }
}

