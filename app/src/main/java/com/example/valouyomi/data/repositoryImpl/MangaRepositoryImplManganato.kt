package com.example.valouyomi.data.repositoryImpl

import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.MangaThumbnail
import com.example.valouyomi.domain.repository.MangaRepository
import com.example.valouyomi.data.apis.ManganatoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named


// Important : A voir quand je vais implémenter une nouvelle api, voir si vraiment besoin d'une impl par api
class MangaRepositoryImplManganato @Inject constructor(
    val api: ManganatoApi
) : MangaRepository {

    override suspend fun searchManga(): Flow<Resource<List<MangaThumbnail>>> = flow {
        try {
            emit(Resource.Loading())
            val mangaThumbnails = api.searchManga()
            emit(Resource.Success(mangaThumbnails))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    override suspend fun getGenres(): Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading())
            val genres = api.getGenres()
            emit(Resource.Success(genres))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

}