package com.bruno13palhano.core.network

import com.bruno13palhano.core.network.model.CategoryNet
import retrofit2.http.GET

internal interface Service {

    @GET("categories/all")
    suspend fun getAllCategories(): List<CategoryNet>
}