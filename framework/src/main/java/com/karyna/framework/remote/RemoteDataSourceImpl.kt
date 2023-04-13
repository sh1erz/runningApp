package com.karyna.framework.remote

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karyna.core.data.Result
import com.karyna.core.data.datasources.RemoteRunDataSource
import com.karyna.core.data.datasources.RemoteUserDataSource
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput
import com.karyna.framework.BuildConfig
import com.karyna.framework.mappers.runInputToDomain
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val googleMapsGeoApi: RemoteAPI) : RemoteUserDataSource,
    RemoteRunDataSource {
    private val db by lazy { Firebase.firestore }
    override suspend fun getUser(userEmail: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(user: User) {
        db.collection(USERS_COLLECTION)
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                Timber.i("User $user saved in document ${user.id}")
            }
            .addOnFailureListener { e ->
                Timber.w("Error adding document", e)
            }

    }

    override suspend fun getRun(id: String): Result<Run> = try {
        val runInput = db.collection(RUNS_COLLECTION).document(id).get().await().toObject(RunInput::class.java)
        runInput?.let { Result.Success(runInputToDomain(id, it)) }
            ?: Result.Failure(NullPointerException("RunInput for document $id was null"))
    } catch (ex: FirebaseFirestoreException) {
        Result.Failure(ex)
    }

    override suspend fun saveRun(runInput: RunInput): Result<String> = try {
        val docId = db.collection(RUNS_COLLECTION).add(runInput).await().id
        Result.Success(docId)
    } catch (ex: FirebaseFirestoreException) {
        Result.Failure(ex)
    }

    override suspend fun getLocationShort(latLng: LatLng): Result<LocationShort> = try {
        val location = googleMapsGeoApi.getLocation(getGeocodingUrl(latLng))
        val city =
            location.results.first { it.types.contains("locality") }.address_components.first { it.types.contains("locality") }.long_name
        val country =
            location.results.first { it.types.contains("country") }.address_components.first { it.types.contains("country") }.long_name
        Result.Success(LocationShort(country = country, city = city))
    } catch (ex: Exception) {
        Timber.e(ex)
        Result.Failure()
    }

    private fun getGeocodingUrl(latLng: LatLng) =
        "https://maps.googleapis.com/maps/api/geocode/json?latlng=${latLng.lat},${latLng.lng}&result_type=country|locality&key=${BuildConfig.MAPS_API_KEY}"

    private companion object {
        const val USERS_COLLECTION = "users"
        const val RUNS_COLLECTION = "runs"
    }
}