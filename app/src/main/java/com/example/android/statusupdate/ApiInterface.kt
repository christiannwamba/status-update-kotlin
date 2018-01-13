package com.example.android.statusupdate


import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("/update-status")
    fun updateStatus(@Query("status") status:String): Call<String>
}