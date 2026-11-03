package com.sanvalero.rentcarrd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "brand")
    val brand: String = "",

    @ColumnInfo(name = "model")
    val model: String = "",

    @ColumnInfo(name = "year")
    val year: Int = 0,

    @ColumnInfo(name = "color")
    val color: String = "",

    @ColumnInfo(name = "category")
    val category: String = "",

    @ColumnInfo(name = "transmission")
    val transmission: String = "",

    @ColumnInfo(name = "passengers")
    val passengers: Int = 4,

    @ColumnInfo(name = "fuel_type")
    val fuelType: String = "",

    @ColumnInfo(name = "daily_rate")
    val dailyRate: Double = 0.0,

    @ColumnInfo(name = "status")
    val status: String = "AVAILABLE",

    @ColumnInfo(name = "image_uri")
    val imageUri: String = ""
)