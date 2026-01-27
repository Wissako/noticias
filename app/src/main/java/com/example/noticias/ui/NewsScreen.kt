package com.example.noticias.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext

@Suppress("unused")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(viewModel: NewsViewModel, apiKey: String) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.cargarNoticias(apiKey)
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Noticias Tech") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.cargarNoticias(apiKey) }) {
                Icon(Icons.Default.Refresh, "Refrescar")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val currentEstado = estado) { // Capture the state in a local variable
                is EstadoNoticias.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EstadoNoticias.Error -> {
                    // Inside this block, currentEstado is smart-cast to EstadoNoticias.Error
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(currentEstado.mensaje)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarNoticias(apiKey) }) {
                            Text("Reintentar")
                        }
                    }
                }
                is EstadoNoticias.Exito -> {
                    // Inside this block, currentEstado is smart-cast to EstadoNoticias.Exito
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

@Composable
fun NoticiaItem(noticia: com.example.noticias.model.Noticia) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Imagen: usar AsyncImage de Coil para cargar desde URL
        if (!noticia.urlToImage.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(noticia.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de la noticia",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
        }
        // Texto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = noticia.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = noticia.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
