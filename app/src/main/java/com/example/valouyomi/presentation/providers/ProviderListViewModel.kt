package com.example.valouyomi.presentation.providers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.valouyomi.common.MangaProvider
import com.example.valouyomi.domain.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProviderListViewModel @Inject constructor() : ViewModel() {

    val providers = MangaProvider.values()

    init {

    }

}