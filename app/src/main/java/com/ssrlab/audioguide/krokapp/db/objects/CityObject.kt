package com.ssrlab.audioguide.krokapp.db.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class CityObject(

    @PrimaryKey
    val id: Int,
    val name: String,
    val logo: String,
    val points: List<Int>,
    val language: String
)