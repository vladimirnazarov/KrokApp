package com.ssrlab.audioguide.krokapp.helpers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromStringToMap(value: String) : Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun toStringFromMap(map: Map<String, String>) : String = Gson().toJson(map)

    @TypeConverter
    fun fromStringToList(value: String) : List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toStringFromList(list: List<Int>) : String = Gson().toJson(list)
}