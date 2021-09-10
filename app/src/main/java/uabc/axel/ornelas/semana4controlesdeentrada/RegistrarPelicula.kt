package uabc.axel.ornelas.semana4controlesdeentrada

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.drawToBitmap
import uabc.axel.ornelas.semana4controlesdeentrada.databinding.ActivityRegistrarPeliculaBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class RegistrarPelicula : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarPeliculaBinding

    private val resultado = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imagen.setImageURI(it)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarPeliculaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ArrayAdapter.createFromResource(
            this,
            R.array.genero_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.lista.adapter = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun registrar(view: View) {
        val intent = Intent()
        try {
            val nombre: String = binding.nombrePelicula.text.toString()
            val genero: String = binding.lista.selectedItem.toString()
            val rating: Float = binding.puntuacion.rating
            val comentario: String = binding.comentario.text.toString()
            val convertidor = SimpleDateFormat("dd/MM/yyyy hh:mm")
            val anio: Int = binding.anio.text.toString().toInt()
            var imagen: Bitmap = binding.imagen.drawToBitmap()
            val fecha: Date =
                convertidor.parse(
                    "${binding.fecha.text} ${binding.hora.text}"
                )
            val pelicula = Pelicula(nombre, genero, rating, comentario, fecha, anio, imagen)

            intent.putExtra("pelicula", pelicula)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Ingrese los datos correctamente", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun cambiarImagen(view: View) {
        try {
            resultado.launch("image/*")
        } catch(e: Exception) {
            Toast.makeText(this, "Error en la dirección de la imagen", Toast.LENGTH_SHORT).show()
        }
    }
}