package uabc.axel.ornelas.peliculas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import uabc.axel.ornelas.peliculas.databinding.ActivityMostrarPeliculasBinding

class MostrarPeliculas : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarPeliculasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarPeliculasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Se obtiene los datos de la pelicula
        val pelis: ArrayList<Pelicula> =
            intent.getParcelableArrayListExtra<Pelicula>("peliculas") as ArrayList<Pelicula>
        // Se le indica el layout al reclycler
        binding.peliculasRecycler.layoutManager = LinearLayoutManager(this)
        // Se crea un adaptador con el arreglo
        val adapter = AdaptadorRecycler(pelis)
        // Se colocan las peliculas en el adaptador
        binding.peliculasRecycler.adapter = adapter
        //Acción que se realiza cuando se presiona una pelicula
        adapter.onClickListener = View.OnClickListener { v ->
            val pos: Int = binding.peliculasRecycler.getChildAdapterPosition(v)
            val intent = Intent(this, InformacionPelicula::class.java)
            intent.putExtra("pelicula", pelis[pos])
            startActivity(intent)
        }
    }

    /**
     * Opciones del menu que cambian los cuadros
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.opcion_vista, menu)
        return true
    }

    /**
     * Se indican las opciones para modificar la vista del menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            //Se cambia lineal la vista
            R.id.lineal -> {
                binding.peliculasRecycler.layoutManager = LinearLayoutManager(this)
                true
            }
            //Se cambia a cuadros la vista
            R.id.cuadros -> {
                binding.peliculasRecycler.layoutManager = GridLayoutManager(this, 2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}