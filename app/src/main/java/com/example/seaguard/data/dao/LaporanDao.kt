package com.example.seaguard.data.dao

import androidx.room.*
import com.example.seaguard.data.entity.Laporan

@Dao
interface LaporanDao {
    @Query("SELECT * FROM laporan")
    fun getAll(): List<Laporan>

    @Insert
    fun insertAll(vararg laporan: Laporan)

    @Delete
    fun delete(laporan: Laporan)

    @Query("SELECT * FROM laporan WHERE uid = :uid")
    fun get(uid: Int): Laporan

    @Update
    fun update(laporan: Laporan)
}