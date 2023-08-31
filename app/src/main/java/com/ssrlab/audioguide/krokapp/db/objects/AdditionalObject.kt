package com.ssrlab.audioguide.krokapp.db.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "additional_table")
data class AdditionalObject(

    @PrimaryKey
    val id: Int,
    val description: String,
    val audio: String,
    val images: Map<String, String>,
    val language: String
)