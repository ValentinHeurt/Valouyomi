package com.example.valouyomi.data.apis

import com.example.valouyomi.domain.models.MangaThumbnail
import retrofit2.http.GET

interface ManganatoApi {

    @GET("/search")
    suspend fun searchManga(): List<MangaThumbnail>

    @GET("/genres")
    suspend fun getGenres(): List<String>
}