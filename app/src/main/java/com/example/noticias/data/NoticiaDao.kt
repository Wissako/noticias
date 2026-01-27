package com.example.noticias.data

import androidx.room.*
import com.example.noticias.model.Noticia
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticiaDao {
    @Query("SELECT * FROM noticia ORDER BY publishedAt DESC")
    fun obtenerTodas(): Flow<List<Noticia>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(noticias: List<Noticia>)
    @Query("DELETE FROM noticia")
    suspend fun borrarTodas()
}