package com.ssrlab.audioguide.krokapp.db.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_table")
data class FavouriteObject(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pointId: Int
)