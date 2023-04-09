package com.karyna.framework.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface RemoteAPI {
    @GET
//    http://maps.google.com/maps/geo?q=40.714224,-73.961452&output=json&oe=utf8&sensor=true_or_false&key=your_api_key
    fun getLocation(@Url url: String)


}