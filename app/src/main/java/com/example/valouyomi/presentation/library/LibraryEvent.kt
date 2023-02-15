package com.example.valouyomi.presentation.library

sealed class LibraryEvent {

    data class Exemple(val ex: String): LibraryEvent()

}
