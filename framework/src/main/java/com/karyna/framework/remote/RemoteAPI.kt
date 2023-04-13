package com.karyna.framework.remote

import com.karyna.framework.dto.RemoteLocation
import retrofit2.http.GET
import retrofit2.http.Url

interface RemoteAPI {
    @GET
    suspend fun getLocation(@Url url: String): RemoteLocation

}