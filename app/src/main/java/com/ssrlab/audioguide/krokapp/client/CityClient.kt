package com.ssrlab.audioguide.krokapp.client

import android.util.Log
import com.ssrlab.audioguide.krokapp.db.dao.CityDao
import com.ssrlab.audioguide.krokapp.db.objects.CityObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object CityClient {

    private var cityClient: OkHttpClient? = null

    fun getCities(language: String, cityDao: CityDao, scope: CoroutineScope, onSuccess: () -> Unit) {

        if (cityClient == null) cityClient = OkHttpClient.Builder().build()

        val request = Request.Builder()
            .url("https://krokapp.com/api-v2/cities/$language/")
            .build()

        cityClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("city_client", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val jArray = JSONArray(responseBody)

                for (i in 0 until jArray.length()) {
                    (jArray[i] as JSONObject).apply {
                        val jsonArrayOfPointIds = this.getJSONArray("point_id")
                        val listOfPointIds = arrayListOf<Int>()
                        for (j in 0 until jsonArrayOfPointIds.length()) listOfPointIds.add(
                            jsonArrayOfPointIds[j] as Int
                        )

                        val city = CityObject(
                            id = this.getInt("city_id"),
                            name = this.getString("city_name"),
                            logo = this.getString("city_logo"),
                            points = listOfPointIds,
                            language = language
                        )

                        scope.launch {
                            cityDao.insert(city)
                        }
                    }
                }

                onSuccess()
            }
        })
    }
}