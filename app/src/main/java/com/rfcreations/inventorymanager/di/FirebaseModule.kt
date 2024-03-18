package com.rfcreations.inventorymanager.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rfcreations.inventorymanager.repository.authrepository.AuthRepository
import com.rfcreations.inventorymanager.repository.authrepository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule{

    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun providesFirebaseFireStore() = FirebaseFirestore.getInstance()

    @Provides
    fun providesFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ):AuthRepository {
       return AuthRepositoryImpl(firebaseAuth, firebaseFirestore)
    }
}