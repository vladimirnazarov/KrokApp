package com.ssrlab.audioguide.krokapp.db.dao

import androidx.room.*
import com.ssrlab.audioguide.krokapp.db.objects.FavouriteObject

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favouriteObject: FavouriteObject)

    @Query("SELECT * FROM favourite_table")
    fun getFavourites() : List<FavouriteObject>

    @Query("SELECT * FROM favourite_table WHERE pointId = :id")
    fun getFavourite(id: Int) : FavouriteObject?

    @Delete
    fun delete(favouriteObject: FavouriteObject)
}