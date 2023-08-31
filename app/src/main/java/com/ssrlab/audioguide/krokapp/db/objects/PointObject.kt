package com.ssrlab.audioguide.krokapp.db.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point_table")
data class PointObject(

    @PrimaryKey
    val id: Int,
    val name: String,
    val logo: String,
    val coordinates: Map<String, String>,
    val language: String
)