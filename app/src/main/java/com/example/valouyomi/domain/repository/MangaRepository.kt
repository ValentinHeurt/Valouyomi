package com.example.valouyomi.domain.repository

import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.Manga
import com.example.valouyomi.domain.models.MangaThumbnail
import kotlinx.coroutines.flow.Flow
import okhttp3.Headers

interface MangaRepository {

    fun searchManga(
        includedGenres: List<String>? = null,
        excludedGenres: List<String>? = null,
        textSearch: String? = null,
        orderBy: String? = null,
        status: String? = null,
        page: String = "1"
    ): Flow<Resource<List<MangaThumbnail>>>

    fun getGenres(): Flow<Resource<List<String>>>

    fun getSortMap(): Map<String, String>

    fun getManga(url: String): Flow<Resource<Manga>>

    fun getPages(url: String): Flow<Resource<List<String>>>
    fun getHeaders(): Headers
}