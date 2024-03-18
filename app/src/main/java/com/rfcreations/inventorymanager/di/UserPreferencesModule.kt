package com.rfcreations.inventorymanager.di

import android.content.Context
import android.content.SharedPreferences
import com.rfcreations.inventorymanager.repository.userpreferencerepository.UserPreferenceRepository
import com.rfcreations.inventorymanager.repository.userpreferencerepository.UserPreferenceRepositoryImpl
import com.rfcreations.inventorymanager.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesModule {
    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val prefName = Constants.PrefKeys.PREF_NAME
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesUserPreferenceRepository(sharedPreferences: SharedPreferences): UserPreferenceRepository {
        return UserPreferenceRepositoryImpl(sharedPreferences)
    }
}