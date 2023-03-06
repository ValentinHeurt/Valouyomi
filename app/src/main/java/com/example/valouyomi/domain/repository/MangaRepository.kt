package com.example.valouyomi.domain.repository

import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.MangaThumbnail
import kotlinx.coroutines.flow.Flow

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
}