package com.alizaidi.regioninfo.api

import com.alizaidi.regioninfo.db.models.Country
import retrofit2.Response
import retrofit2.http.GET

interface countryapi {

    @GET("/v3.1/region/asia")
    suspend fun getTodos(): Response<List<Country>>

}