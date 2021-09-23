package uabc.axel.ornelas.peliculas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import uabc.axel.ornelas.peliculas.databinding.ActivityMostrarPeliculasBinding

class MostrarPeliculas : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarPeliculasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarPeliculasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pelis: ArrayList<Pelicula> = intent.getParcelableArrayListExtra<Pelicula>("peliculas") as ArrayList<Pelicula>
        binding.peliculasRecycler.layoutManager = LinearLayoutManager(this)
        val adapter = AdaptadorRecycler(pelis)

        binding.peliculasRecycler.adapter = adapter
    }


}