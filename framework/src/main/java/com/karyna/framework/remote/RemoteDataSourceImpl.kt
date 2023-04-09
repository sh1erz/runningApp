package com.karyna.framework.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karyna.core.data.Result
import com.karyna.core.data.datasources.RemoteUserDataSource
import com.karyna.core.domain.User
import timber.log.Timber
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor() : RemoteUserDataSource {
    private val db by lazy { Firebase.firestore }
    override fun getUser(userEmail: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun addUser(user: User) {
        db.collection(USERS_COLLECTION)
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                1
                Timber.i("User $user saved in document ${user.id}")
            }
            .addOnFailureListener { e ->
                Timber.w("Error adding document", e)
            }
    }

    private companion object {
        const val USERS_COLLECTION = "users"
    }
}