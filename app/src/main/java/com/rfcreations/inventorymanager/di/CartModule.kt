package com.rfcreations.inventorymanager.di

import android.content.Context
import androidx.room.Room
import com.rfcreations.inventorymanager.database.CartInMemoryDataBase
import com.rfcreations.inventorymanager.database.cartdb.CartDao
import com.rfcreations.inventorymanager.repository.cartrepository.CartRepository
import com.rfcreations.inventorymanager.repository.cartrepository.CartRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    @Singleton
    fun providesCartInMemoryDataBase(@ApplicationContext context: Context): CartInMemoryDataBase {
        return Room.inMemoryDatabaseBuilder(context, CartInMemoryDataBase::class.java)
            .build()
    }

    @Provides
    @Singleton
    fun providesCartDao(cartInMemoryDataBase: CartInMemoryDataBase): CartDao {
        return cartInMemoryDataBase.cartDao()
    }

    @Provides
    @Singleton
    fun providesCartRepository(cartDao: CartDao): CartRepository {
        return CartRepositoryImpl(cartDao)
    }
}