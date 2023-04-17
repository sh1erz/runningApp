package com.karyna.framework.remote

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karyna.core.data.Result
import com.karyna.core.data.datasources.RemoteRunDataSource
import com.karyna.core.data.datasources.RemoteUserDataSource
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort
import com.karyna.core.domain.User
import com.karyna.core.domain.run.OrderingMode
import com.karyna.core.domain.run.Run
import com.karyna.core.domain.run.RunInput
import com.karyna.framework.BuildConfig
import com.karyna.framework.dto.RemoteRun
import com.karyna.framework.mappers.isoToDate
import com.karyna.framework.mappers.remoteRunInputToDomain
import com.karyna.framework.mappers.runInputToDto
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
        val runInput = db.collection(RUNS_COLLECTION).document(id).get().await().toObject(RemoteRun::class.java)
        runInput?.let { Result.Success(remoteRunInputToDomain(id, it)) }
            ?: Result.Failure(NullPointerException("RunInput for document $id was null"))
    } catch (ex: FirebaseFirestoreException) {
        Timber.e(ex)
        Result.Failure(ex)
    }

    override suspend fun getRuns(userId: String): Result<List<Run>> = try {
        val query = db.collection(RUNS_COLLECTION).whereEqualTo("userId", userId)
            .orderBy(DATE_FIELD, Query.Direction.DESCENDING).get().await()
        val ids = query.documents.map { it.id }
        val remoteRuns = query.toObjects(RemoteRun::class.java)
        val runs = ids.zip(remoteRuns).map { remoteRunInputToDomain(it.first, it.second) }
        Result.Success(runs)
    } catch (ex: FirebaseFirestoreException) {
        Timber.e(ex)
        Result.Failure(ex)
    }

    override suspend fun getTopRuns(
        amount: Int,
        ordering: OrderingMode,
        isoDateFrom: String,
        isoDateTo: String
    ): Result<List<Run>> = try {
        val dateFrom = isoToDate(isoDateFrom)
        val dateTo = isoToDate(isoDateTo)
        val query = db.collection(RUNS_COLLECTION).whereGreaterThanOrEqualTo(DATE_FIELD, dateFrom)
            .whereLessThan(DATE_FIELD, dateTo).get().await()

        val ids = query.documents.map { it.id }
        val remoteRuns = query.toObjects(RemoteRun::class.java)

        val runs = ids.zip(remoteRuns).map { remoteRunInputToDomain(it.first, it.second) }
        val orderedRuns = when (ordering) {
            OrderingMode.BY_DISTANCE -> runs.sortedBy { it.distanceMeters }
            OrderingMode.BY_DURATION -> runs.sortedBy { it.durationS }
        }
        Result.Success(orderedRuns.take(amount))
    } catch (ex: Exception) {
        Timber.e(ex)
        Result.Failure(ex)
    }

    override suspend fun saveRun(runInput: RunInput): Result<String> = try {
        val remoteRunInput = runInputToDto(runInput)
        val docId = db.collection(RUNS_COLLECTION).add(remoteRunInput).await().id
        Result.Success(docId)
    } catch (ex: FirebaseFirestoreException) {
        Timber.e(ex)
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
        const val DATE_FIELD = "date"
    }
}