package com.example.valouyomi.data.repositoryImpl

import com.example.valouyomi.common.Resource
import com.example.valouyomi.domain.models.MangaThumbnail
import com.example.valouyomi.domain.repository.MangaRepository
import com.example.valouyomi.data.apis.ManganatoApi
import com.example.valouyomi.domain.models.Manga
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Headers
import org.apache.commons.text.StringEscapeUtils
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named


// Important : A voir quand je vais implémenter une nouvelle api, voir si vraiment besoin d'une impl par api
class MangaRepositoryImplManganato @Inject constructor(
    val api: ManganatoApi
) : MangaRepository {

    val sort = mapOf<String,String>( "A-Z" to "az"  ,  "Newest" to "newest" ,  "Most viewed" to "topview" ,  "Latest updates" to "Latest updates" )

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
            val textSearchEspaced = textSearch?.replace("\n", "")
            val mangaThumbnails = api.searchManga(includedGenres, excludedGenres, textSearchEspaced, orderBy, status, page)
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

    override fun getSortMap(): Map<String,String> {
        return sort
    }

    override fun getManga(url: String): Flow<Resource<Manga>> = flow {
        try {
            emit(Resource.Loading())
            val manga = api.getManga(url)
            emit(Resource.Success(manga))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    override fun getPages(url: String): Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading())
            val pages = api.getPages(url)
            emit(Resource.Success(pages))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    override fun getHeaders(): Headers{
        val header = Headers.Builder()
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:107.0) Gecko/20100101 Firefox/107.0")
            .add("Accept", "image/avif,image/webp,*/*")
            .add("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3")
            .add("Referer", "https://chapmanganato.com/")
            .build()
        return header
    }

}