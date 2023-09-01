package com.ssrlab.audioguide.krokapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssrlab.audioguide.krokapp.db.objects.CityObject

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityObject)

    @Query("SELECT * FROM city_table")
    fun getCities() : List<CityObject>
}