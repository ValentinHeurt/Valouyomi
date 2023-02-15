package com.example.valouyomi.domain.models

data class Manga(
    val authors: List<String>,
    val chapters: List<Chapter>,
    val description: String,
    val genres: List<String>,
    val imageURL: String,
    val lastUpdate: String,
    val rating: String,
    val status: String,
    val title: String,
    val views: String
)