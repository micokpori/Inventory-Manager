package com.rfcreations.inventorymanager.repository.authrepository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rfcreations.inventorymanager.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading)
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(data = result))
        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }
    }

    override suspend fun register(email: String, password: String, name: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading)
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            // Create a new user with a first and last name
            val user = hashMapOf(
                "uid" to result.user?.uid,
                "name" to name,
                "timeOfAccountCreation" to Calendar.getInstance().timeInMillis,
                "enabled" to true,
            )
            // Add a new document with a generated ID
            result.user?.let {
                firebaseFirestore.collection("users")
                    .document(it.uid)
                    .set(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Success", "DocumentSnapshot added with ID: $documentReference")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Failure", "Error adding document", e)
                    }
            }
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}
