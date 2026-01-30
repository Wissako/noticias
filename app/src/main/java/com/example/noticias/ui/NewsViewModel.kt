package com.example.noticias.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noticias.data.NewsDatabase
import com.example.noticias.data.NewsRepository
import com.example.noticias.model.Noticia
import com.example.noticias.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface EstadoNoticias {
    object Cargando : EstadoNoticias
    data class Exito(val noticias: List<Noticia>) : EstadoNoticias
    data class Error(val mensaje: String) : EstadoNoticias
}

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = NewsDatabase.getDatabase(application)
    private val repository = NewsRepository(RetrofitClient.apiService, db.noticiaDao())

    private val _estado = MutableStateFlow<EstadoNoticias>(EstadoNoticias.Cargando)
    val estado: StateFlow<EstadoNoticias> = _estado.asStateFlow()

    // Estado para los filtros
    private val _filtros = MutableStateFlow(NewsFilterState())
    val filtros: StateFlow<NewsFilterState> = _filtros.asStateFlow()

    fun actualizarFiltros(nuevosFiltros: NewsFilterState) {
        _filtros.value = nuevosFiltros
    }

    fun cargarNoticias(apiKey: String) {
        viewModelScope.launch {
            _estado.value = EstadoNoticias.Cargando
            try {
                val f = _filtros.value
                val noticias = repository.obtenerNoticias(
                    apiKey = apiKey,
                    sources = f.selectedSource,
                    country = f.selectedCountry,
                    category = f.selectedCategory,
                    query = f.query
                )
                _estado.value = EstadoNoticias.Exito(noticias)
            } catch (e: Exception) {
                _estado.value = EstadoNoticias.Error("Error: ${e.message ?: "Desconocido"}")
            }
        }
    }
}