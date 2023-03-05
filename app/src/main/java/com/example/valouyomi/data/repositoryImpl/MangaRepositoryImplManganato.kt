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

    override fun searchManga(
        includedGenres: List<String>?,
        excludedGenres: List<String>?,
        textSearch: String?,
        orderBy: String?,
        status: String?,
        page: String
    ): Flow<Resource<List<MangaThumbnail>>> = flow {
        try {
            emit(Resource.Loading())
            val mangaThumbnails = api.searchManga(includedGenres, excludedGenres, textSearch, orderBy, status, page)
            emit(Resource.Success(mangaThumbnails))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    override fun getGenres(): Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading())
            val genres = api.getGenres()
            val genresNames = genres.map { it.genre }
            emit(Resource.Success(genresNames))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

}