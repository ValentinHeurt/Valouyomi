package com.example.valouyomi.data.apis

import com.example.valouyomi.domain.models.MangaThumbnail
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ManganatoApi {

    @GET("/search")
    suspend fun searchManga(
        @Query("ig") ig: List<String>?,
        @Query("eg") eg: List<String>?,
        @Header("textSearch") textSearch: String?,
        @Header("orderBy") orderBy: String?,
        @Header("status") status: String?,
        @Header("page") page: String
    ): List<MangaThumbnail>

    @GET("/genres")
    suspend fun getGenres(): List<String>

    @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:107.0) Gecko/20100101 Firefox/107.0",
              "Accept: image/avif,image/webp,*/*",
              "Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3",
              "Referer: https://chapmanganato.com/")
    @GET("/mangaPages")
    suspend fun getMangaPages(
        @Header("url") url: String
    ): List<String>

}