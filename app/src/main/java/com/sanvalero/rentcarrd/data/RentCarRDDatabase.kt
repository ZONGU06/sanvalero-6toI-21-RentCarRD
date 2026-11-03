package com.sanvalero.rentcarrd.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanvalero.rentcarrd.data.entities.ContractEntity
import com.sanvalero.rentcarrd.data.entities.MaintenanceServiceEntity
import com.sanvalero.rentcarrd.data.entities.PaymentEntity
import com.sanvalero.rentcarrd.data.entities.SystemConfigEntity
import com.sanvalero.rentcarrd.data.entities.UserEntity
import com.sanvalero.rentcarrd.data.entities.VehicleEntity

@Database(entities = [
    UserEntity::class,
    VehicleEntity::class,
    ContractEntity::class,
    MaintenanceServiceEntity::class,
    PaymentEntity::class,
    SystemConfigEntity::class
], version = 1, exportSchema = false)
abstract class RentCarRDDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun contractDao(): ContractDao
    abstract fun maintenanceServiceDao(): MaintenanceServiceDao
    abstract fun paymentDao(): PaymentDao
    abstract fun systemConfigDao(): SystemConfigDao

    // Singleton de la base de datos
    companion object {
        @Volatile
        private var INSTANCE: RentCarRDDatabase? = null

        fun getInstance(context: android.content.Context): RentCarRDDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    RentCarRDDatabase::class.java,
                    "rentcarrd-db"
                ).build()
                INSTANCE = db
                db
            }
        }
    }
}