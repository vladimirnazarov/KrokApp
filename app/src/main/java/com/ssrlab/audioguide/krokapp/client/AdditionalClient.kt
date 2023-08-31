package com.ssrlab.audioguide.krokapp.client

import android.util.Log
import okhttp3.*
import java.io.IOException

object AdditionalClient {

    private var additionalClient: OkHttpClient? = null

    fun getAdditionalInfo(language: String, id: Int) {

        if (additionalClient == null) additionalClient = OkHttpClient.Builder().build()

        val request = Request.Builder()
            .url("https://krokapp.com/api-v2/point-ext/$language/$id")
            .build()

        additionalClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("city_client", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                println(response)
            }
        })
    }
}