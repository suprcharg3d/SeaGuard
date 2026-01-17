package com.example.seaguard.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Laporan(
    @PrimaryKey(autoGenerate = true)
    var uid: Int? = null,

    @ColumnInfo(name = "lokasi_pantai")
    var lokasi: String?,

    @ColumnInfo(name = "jenis_sampah")
    var jenis: String?,

    @ColumnInfo(name = "deskripsi")
    var deskripsi: String?,

    @ColumnInfo(name = "dilaporkan_oleh")
    var dilaporkanOleh: String?,

    // 🆕 FOTO (URI STRING)
    @ColumnInfo(name = "photo_path")
    var photoPath: String?,

    // 🆕 GPS
    @ColumnInfo(name = "latitude")
    var latitude: Double?,

    @ColumnInfo(name = "longitude")
    var longitude: Double?
)
