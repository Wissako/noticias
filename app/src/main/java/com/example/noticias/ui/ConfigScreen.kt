package com.example.noticias.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    currentFilters: NewsFilterState,
    onApplyFilters: (NewsFilterState) -> Unit,
    onBack: () -> Unit
) {
    // Estados locales para el formulario
    var source by remember { mutableStateOf(currentFilters.selectedSource) }
    var country by remember { mutableStateOf(currentFilters.selectedCountry) }
    var category by remember { mutableStateOf(currentFilters.selectedCategory) }
    var query by remember { mutableStateOf(currentFilters.query) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurar Consulta") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Opción A: Buscar por Fuente (ID)", style = MaterialTheme.typography.titleMedium)
            Text("Ejemplos: techcrunch, bbc-news, el-mundo. Si usas esto, se ignora país y categoría.", style = MaterialTheme.typography.bodySmall)

            OutlinedTextField(
                value = source,
                onValueChange = { source = it },
                label = { Text("Source ID") },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider()

            Text("Opción B: Filtros Generales", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("País (ej: us, es, fr)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = source.isBlank() // Deshabilitar si hay fuente escrita
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría (ej: technology, sports)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = source.isBlank()
            )

            HorizontalDivider()

            Text("Búsqueda libre", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Palabra clave (q)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onApplyFilters(
                        NewsFilterState(
                            selectedSource = source.trim(),
                            selectedCountry = country.trim(),
                            selectedCategory = category.trim(),
                            query = query.trim()
                        )
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar y Buscar")
            }
        }
    }
}