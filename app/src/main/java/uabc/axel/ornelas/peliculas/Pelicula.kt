package uabc.axel.ornelas.peliculas

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Describe las cualidades de una pelicula
 */
@Parcelize
class Pelicula(
    val nombre: String,
    val genero: String,
    val rating: Float,
    val comentario: String,
    val fechaVista: Date,
    val añoPelicula: Int,
    val imagen: Bitmap
): Parcelable {
    override fun toString(): String {
        return "nombre: $nombre\ngenero: $genero\nrating: $rating\ncomentario: $comentario" +
                "\nfechaVista: $fechaVista\naño pelicula: $añoPelicula\nImagen: $imagen\n"
    }
}