package com.ssrlab.audioguide.krokapp.client

import android.util.Log
import com.ssrlab.audioguide.krokapp.db.dao.PointDao
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object PointClient {

    private var pointClient: OkHttpClient? = null

    fun getPoints(language: String, pointDao: PointDao, scope: CoroutineScope, onSuccess: () -> Unit) {

        if (pointClient == null) pointClient = OkHttpClient.Builder().build()

        val request = Request.Builder()
            .url("https://krokapp.com/api-v2/points/$language/")
            .build()

        pointClient?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("point_client", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val jArray = JSONArray(responseBody)

                for (i in 0 until jArray.length()) {
                    (jArray[i] as JSONObject).apply {
                        val point = PointObject(
                            id = this.getInt("point_id"),
                            name = this.getString("point_name"),
                            logo = this.getString("point_logo"),
                            coordinates = parseJsonToMap(this.getString("coordinates")),
                            language = language
                        )

                        scope.launch {
                            pointDao.insert(point)
                        }
                    }
                }

                onSuccess()
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