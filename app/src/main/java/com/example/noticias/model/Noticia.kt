package com.example.noticias.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "noticia")
data class Noticia(
    @PrimaryKey val url: String,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?
)