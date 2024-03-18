package com.rfcreations.inventorymanager.di

import com.rfcreations.inventorymanager.database.InventoryManagerAppDataBase
import com.rfcreations.inventorymanager.database.inventorydb.InventoryDao
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InventoryModule {

    @Provides
    @Singleton
    fun providesInventoryDao(inventoryManagerAppDataBase: InventoryManagerAppDataBase): InventoryDao {
        return inventoryManagerAppDataBase.inventoryDao()
    }

    @Provides
    @Singleton
    fun providesInventoryRepository(inventoryDao: InventoryDao): InventoryRepository {
        return InventoryRepositoryImpl(inventoryDao)
    }
}