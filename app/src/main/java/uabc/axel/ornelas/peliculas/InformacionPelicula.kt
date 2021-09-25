package uabc.axel.ornelas.peliculas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uabc.axel.ornelas.peliculas.databinding.ActivityInformacionPeliculaBinding
import java.util.*

class InformacionPelicula : AppCompatActivity() {

    private lateinit var binding: ActivityInformacionPeliculaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformacionPeliculaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val peli = intent.getParcelableExtra<Pelicula>("pelicula") as Pelicula
        with(binding) {
            with(peli) {
                imageView.setImageURI(imagen)
                nombreInfo.text = nombre
                generoInfo.text = genero
                ratingInfo.text = rating.toString()
                comentarioInfo.text = comentario
                fechaVistaInfo.text = Date(fechaVista).toString()
                anioPeliculaInfo.text = añoPelicula.toString()
            }

        }

    }
}