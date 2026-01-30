package com.example.noticias.data

import com.example.noticias.model.Noticia
import com.example.noticias.network.NewsApiService
import kotlinx.coroutines.flow.first

class NewsRepository(
    private val apiService: NewsApiService,
    private val noticiaDao: NoticiaDao
) {
    // Añadimos los argumentos con valores por defecto
    suspend fun obtenerNoticias(
        apiKey: String,
        sources: String? = null,
        country: String? = null,
        category: String? = null,
        query: String? = null
    ): List<Noticia> {
        return try {
            // Si se especifican sources, ignoramos country y category
            val finalCountry = if (!sources.isNullOrBlank()) null else country
            val finalCategory = if (!sources.isNullOrBlank()) null else category

            // dato por defecto si no hay ni sources ni country
            val effectiveCountry = if (sources.isNullOrBlank() && finalCountry == null) "us" else finalCountry

            val response = apiService.getTopHeadlines(
                apiKey = apiKey,
                sources = sources?.ifBlank { null },
                country = effectiveCountry,
                category = finalCategory?.ifBlank { null },
                query = query?.ifBlank { null }
            )

            if (response.status != "ok" || response.articles.isEmpty()) {
                if (response.status == "error") throw Exception("Error API")
            }

            val noticiasSanitizadas = response.articles.map { noticia ->
                noticia.copy(
                    url = noticia.url.trim(),
                    urlToImage = noticia.urlToImage?.trim()
                )
            }

            noticiaDao.borrarTodas()
            noticiaDao.insertar(noticiasSanitizadas)
            noticiasSanitizadas

        } catch (e: Exception) {
            val cached = noticiaDao.obtenerTodas().first()
            if (cached.isNotEmpty()) {
                cached
            } else {
                throw e
            }
        }
    }
}