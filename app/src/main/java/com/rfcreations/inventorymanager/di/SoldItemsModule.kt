package com.rfcreations.inventorymanager.di

import com.rfcreations.inventorymanager.database.InventoryManagerAppDataBase
import com.rfcreations.inventorymanager.database.salesdb.SalesDao
import com.rfcreations.inventorymanager.repository.soldItemsRepository.SoldItemsRepository
import com.rfcreations.inventorymanager.repository.soldItemsRepository.SoldItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object SoldItemsModule {

    @Provides
    @Singleton
    fun providesSoldItemsDao(inventoryManagerAppDataBase: InventoryManagerAppDataBase): SalesDao {
        return inventoryManagerAppDataBase.salesDao()
    }

    @Provides
    @Singleton
    fun providesSoldItemsRepositoryImpl(salesDao: SalesDao): SoldItemsRepository {
        return SoldItemsRepositoryImpl(salesDao)
    }
}