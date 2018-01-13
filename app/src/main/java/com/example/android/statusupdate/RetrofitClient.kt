package com.example.android.statusupdate


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitClient {
    fun getClient(): ApiInterface {
        val httpClient = OkHttpClient.Builder()
        val builder = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(ScalarsConverterFactory.create())

        val retrofit = builder
                .client(httpClient.build())
                .build()
        return retrofit.create(ApiInterface::class.java)
    }
}