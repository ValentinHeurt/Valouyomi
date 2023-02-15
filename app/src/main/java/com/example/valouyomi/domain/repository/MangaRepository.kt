package com.example.valouyomi.domain.repository

import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.MangaThumbnail
import kotlinx.coroutines.flow.Flow

interface MangaRepository {

    suspend fun searchManga(): Flow<Resource<List<MangaThumbnail>>>

    suspend fun getGenres(): Flow<Resource<List<String>>>
}