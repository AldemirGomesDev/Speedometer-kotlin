package com.app.velocimetro.data.persistence

import android.location.Location
import androidx.room.*
import io.reactivex.rxjava3.core.Flowable

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: Location): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: List<Location>): List<Long>

    @Query("SELECT * FROM location WHERE id = :id")
    fun selectById(id: Long): Flowable<Location>

    @Query("SELECT * FROM location")
    fun selectAll(): Flowable<List<Location>>

    @Update
    fun update(location: Location): Int

    @Query("DELETE FROM location WHERE id = :id")
    fun deleteById(id: Long): Int

    @Query("DELETE FROM location")
    fun deleteAll(): Int
}