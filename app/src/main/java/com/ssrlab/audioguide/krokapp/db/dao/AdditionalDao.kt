package com.ssrlab.audioguide.krokapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssrlab.audioguide.krokapp.db.objects.AdditionalObject

@Dao
interface AdditionalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(additionalObject: AdditionalObject)

    @Query("SELECT * FROM additional_table WHERE id = :id")
    fun getAdditionalInfo(id: Int) : AdditionalObject?
}