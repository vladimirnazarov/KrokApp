package com.ssrlab.audioguide.krokapp.client

import android.util.Log
import okhttp3.*
import java.io.IOException

object CityClient {

    private var cityClient: OkHttpClient? = null

    fun getCities(language: String, onSuccess: () -> Unit) {

        if (cityClient == null) cityClient = OkHttpClient.Builder().build()

        val request = Request.Builder()
            .url("https://krokapp.com/api-v2/cities/$language/")
            .build()

        cityClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("city_client", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.body?.string())
            }
        })
    }
}