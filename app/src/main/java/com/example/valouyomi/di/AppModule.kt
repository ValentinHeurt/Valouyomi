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
import javax.inject.Qualifier
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
    // Ajouter des named quand il y en aura plusieurs
    @Provides
    @Singleton
    @Named(Constants.MANGANATO)
    fun provideManganatoRepository(api : ManganatoApi) : MangaRepository {
        return MangaRepositoryImplManganato(api)
    }
    @Provides
    @Singleton
    @Named("Test")
    fun provideTestRepository(api : ManganatoApi) : MangaRepository {
        return MangaRepositoryImplManganato(api)
    }

    @Provides
    fun provideMangaRepositoryMap(
        @Named(Constants.MANGANATO) manganatoRepository: MangaRepository,
        @Named("Test") testRepository: MangaRepository
    ): Map<String, MangaRepository>{
        val map = mutableMapOf<String,MangaRepository>()
        map[Constants.MANGANATO] = manganatoRepository
        map["Test"] = testRepository
        return map
    }

}