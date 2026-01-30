package com.example.noticias.ui

data class NewsFilterState(
    // Filtros de noticias
    val selectedSource: String = "",
    // Código de país por defecto "us"
    val selectedCountry: String = "us",
    // Categoría por defecto "general"
    val selectedCategory: String = "",
    val query: String = "" // Búsqueda libre
)