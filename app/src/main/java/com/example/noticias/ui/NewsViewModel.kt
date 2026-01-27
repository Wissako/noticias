package com.example.noticias.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noticias.data.NewsRepository
import com.example.noticias.data.NewsDatabase
import com.example.noticias.model.Noticia
import com.example.noticias.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val estado: StateFlow<EstadoNoticias> = _estado
    fun cargarNoticias(apiKey: String) {
        viewModelScope.launch {
            _estado.value = EstadoNoticias.Cargando
            try {
                val noticias = repository.obtenerNoticias(apiKey)
                _estado.value = EstadoNoticias.Exito(noticias)
            } catch (e: Exception) {
                _estado.value = EstadoNoticias.Error("Error: ${e.message ?: "Desconocido"}")
            }
        }
    }
}