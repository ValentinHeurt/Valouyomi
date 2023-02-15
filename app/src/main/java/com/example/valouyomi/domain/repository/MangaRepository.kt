package com.example.valouyomi.domain.repository

import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.MangaThumbnail
import kotlinx.coroutines.flow.Flow

interface MangaRepository {

    fun searchManga(): Flow<Resource<List<MangaThumbnail>>>

    fun getGenres(): Flow<Resource<List<String>>>
}