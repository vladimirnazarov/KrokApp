package com.ssrlab.audioguide.krokapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssrlab.audioguide.krokapp.db.dao.AdditionalDao
import com.ssrlab.audioguide.krokapp.db.dao.CityDao
import com.ssrlab.audioguide.krokapp.db.dao.FavouriteDao
import com.ssrlab.audioguide.krokapp.db.dao.PointDao
import com.ssrlab.audioguide.krokapp.db.objects.AdditionalObject
import com.ssrlab.audioguide.krokapp.db.objects.CityObject
import com.ssrlab.audioguide.krokapp.db.objects.FavouriteObject
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import com.ssrlab.audioguide.krokapp.helpers.Converters

@Database(entities = [CityObject::class, PointObject::class, FavouriteObject::class, AdditionalObject::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseClient : RoomDatabase() {
    abstract fun cityDao() : CityDao
    abstract fun pointDao() : PointDao
    abstract fun favouriteDao() : FavouriteDao
    abstract fun additionalDao() : AdditionalDao
}