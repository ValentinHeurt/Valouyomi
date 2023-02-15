package com.example.valouyomi.di

import android.app.Application
import com.example.valouyomi.common.Constants
import com.example.valouyomi.domain.repository.MangaRepository
import com.example.valouyomi.data.repositoryImpl.MangaRepositoryImplManganato
import com.example.valouyomi.data.apis.ManganatoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideManganatoService(app: Application): ManganatoApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ManganatoApi::class.java)
    }
    // Récupérer les repository en fonction du module choisis par l'user
    @Provides
    @Singleton
    @Named("ManganatoRepository")
    fun provideManganatoRepository(api : ManganatoApi) : MangaRepository {
        return MangaRepositoryImplManganato(api)
    }


}