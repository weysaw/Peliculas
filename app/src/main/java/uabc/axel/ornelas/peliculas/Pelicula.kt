package uabc.axel.ornelas.peliculas

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Describe las cualidades de una pelicula
 */
@Parcelize
data class Pelicula(
    val nombre: String,
    val genero: String,
    val rating: Float,
    val comentario: String,
    val fechaVista: Long,
    val añoPelicula: Int,
    val imagen: Uri
): Parcelable {
    override fun toString(): String {
        return "nombre: $nombre\ngenero: $genero\nrating: $rating\ncomentario: $comentario" +
                "\nfechaVista: ${Date(fechaVista)}\naño pelicula: $añoPelicula\nImagen: $imagen\n"
    }

}