package com.leaird.fetchem.model.remote

import com.leaird.fetchem.model.db.PuppyItem
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("hiring.json")
    suspend fun getPuppyItems(): Response<List<PuppyItem>>
}