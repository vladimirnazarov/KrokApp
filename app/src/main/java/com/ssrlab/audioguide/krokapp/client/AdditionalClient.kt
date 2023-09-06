package com.ssrlab.audioguide.krokapp.client

import android.util.Log
import com.ssrlab.audioguide.krokapp.db.dao.AdditionalDao
import com.ssrlab.audioguide.krokapp.db.objects.AdditionalObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object AdditionalClient {

    private var additionalClient: OkHttpClient? = null

    fun getAdditionalInfo(language: String, id: Int, additionalDao: AdditionalDao, scope: CoroutineScope, onSuccess: (AdditionalObject) -> Unit) {

        if (additionalClient == null) additionalClient = OkHttpClient.Builder().build()

        val request = Request.Builder()
            .url("https://krokapp.com/api-v2/point-ext/$language/$id")
            .build()

        additionalClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("city_client", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val jArray = JSONArray(responseBody)

                for (i in 0 until jArray.length()) {
                    (jArray[i] as JSONObject).apply {
                        val additionalObject = AdditionalObject(
                            id = this.getInt("point_id"),
                            description = this.getString("point_text"),
                            audio = this.getString("point_sound"),
                            images = parseJsonToMap(this.getString("point_images")),
                            language = language
                        )

                        scope.launch {
                            additionalDao.insert(additionalObject)
                            onSuccess(additionalObject)
                        }
                    }
                }
            }
        })
    }

    private fun parseJsonToMap(json: String) : Map<String, String> {
        val jsonObject = JSONObject(json)
        val map = HashMap<String, String>()

        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = jsonObject.getString(key)
            map[key] = value
        }

        return map
    }
}