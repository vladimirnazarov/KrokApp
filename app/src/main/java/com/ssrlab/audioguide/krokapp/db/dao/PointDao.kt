package com.ssrlab.audioguide.krokapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssrlab.audioguide.krokapp.db.objects.PointObject

@Dao
interface PointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: PointObject)

    @Query("SELECT * FROM point_table")
    fun getPoints() : List<PointObject>

    @Query("SELECT * FROM point_table WHERE id IN (:ids)")
    fun getPoints(ids: List<Int>) : List<PointObject>
}