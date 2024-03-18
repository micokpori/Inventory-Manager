package com.rfcreations.inventorymanager.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rfcreations.inventorymanager.database.InventoryManagerAppDataBase
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.repository.soldItemsRepository.SoldItemsRepository
import com.rfcreations.inventorymanager.repository.userpreferencerepository.UserPreferenceRepository
import com.rfcreations.inventorymanager.utils.BackUpManager
import com.rfcreations.inventorymanager.utils.Constants
import com.rfcreations.inventorymanager.utils.RestoreBackUpManager
import com.rfcreations.inventorymanager.utils.ThemeUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesInventoryManagerAppDataBase(@ApplicationContext context: Context): InventoryManagerAppDataBase {
        return Room.databaseBuilder(
            context, InventoryManagerAppDataBase::class.java, Constants.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun providesThemeUiState(
        userPreferenceRepository: UserPreferenceRepository
    ): ThemeUiState = ThemeUiState(userPreferenceRepository)

    @Provides
    fun providesBackUpManager(
        inventoryRepository: InventoryRepository,
        soldItemsRepository: SoldItemsRepository,
        firebaseStorage: FirebaseStorage,
        firebaseAuth: FirebaseAuth,
        @ApplicationContext context: Context,
    ): BackUpManager = BackUpManager(
        inventoryRepository, soldItemsRepository,
        firebaseStorage, firebaseAuth, context as Application
    )

    @Provides
    fun providesRestoreManager(
        inventoryRepository: InventoryRepository,
        soldItemsRepository: SoldItemsRepository,
        firebaseStorage: FirebaseStorage,
        firebaseAuth: FirebaseAuth,
        @ApplicationContext context: Context,
    ): RestoreBackUpManager = RestoreBackUpManager(
        inventoryRepository, soldItemsRepository,
        firebaseStorage, firebaseAuth, context as Application
    )

}