package com.rfcreations.inventorymanager.repository.authrepository

import com.google.firebase.auth.AuthResult
import com.rfcreations.inventorymanager.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email:String,password:String): Flow<Resource<AuthResult>>
    suspend fun register(email: String, password: String, name: String): Flow<Resource<AuthResult>>
}