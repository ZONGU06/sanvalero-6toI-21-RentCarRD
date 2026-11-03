package com.sanvalero.rentcarrd.di

import android.content.Context
import com.sanvalero.rentcarrd.data.RentCarRDDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): RentCarRDDatabase {
        return RentCarRDDatabase.getInstance(context)
    }
}