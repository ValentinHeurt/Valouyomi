package com.example.valouyomi.domain.models

data class Manga(
    val authors: List<String> = emptyList(),
    var chapters: List<Chapter> = emptyList(),
    val description: String = "",
    val genres: List<String> = emptyList(),
    val imageURL: String = "",
    val lastUpdate: String = "",
    val rating: String = "",
    val status: String = "",
    val title: String = "",
    val views: String = ""
)