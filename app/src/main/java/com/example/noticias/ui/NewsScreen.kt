package com.example.noticias.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.noticias.model.Noticia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel, apiKey: String) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val filtros by viewModel.filtros.collectAsStateWithLifecycle()

    // Estado simple para controlar navegación: false = Lista, true = Configuración
    var showConfig by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.cargarNoticias(apiKey)
    }

    if (showConfig) {
        ConfigScreen(
            currentFilters = filtros,
            onApplyFilters = { nuevosFiltros ->
                viewModel.actualizarFiltros(nuevosFiltros)
                viewModel.cargarNoticias(apiKey) // Recargar con los nuevos filtros
            },
            onBack = { showConfig = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Noticias") },
                    actions = {
                        IconButton(onClick = { showConfig = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Configurar")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.cargarNoticias(apiKey) }) {
                    Icon(Icons.Default.Refresh, "Refrescar")
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (val currentEstado = estado) {
                    is EstadoNoticias.Cargando -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is EstadoNoticias.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = currentEstado.mensaje,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.cargarNoticias(apiKey) }) {
                                Text("Reintentar")
                            }
                        }
                    }

                    is EstadoNoticias.Exito -> {
                        if (currentEstado.noticias.isEmpty()) {
                            Text(
                                "No se encontraron noticias con estos criterios.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn {
                                items(currentEstado.noticias) { noticia ->
                                    NoticiaItem(noticia = noticia)
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun NoticiaItem(noticia: Noticia) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            if (!noticia.urlToImage.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(noticia.urlToImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de la noticia",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = noticia.title ?: "Sin título",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = noticia.description ?: "Sin descripción disponible.",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
