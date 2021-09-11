package uabc.axel.ornelas.peliculas

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.drawToBitmap
import uabc.axel.ornelas.peliculas.databinding.ActivityRegistrarPeliculaBinding
import java.lang.Exception
import java.util.*
import kotlin.math.min

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

    /**
     * Registra los datos y se los devuelve como objeto a la clase principal
     */
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

            intent.putExtra("peliculas", pelicula)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Ingrese los datos correctamente", Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Abre el explorador de archivos para
     */
    fun cambiarImagen(view: View) {
        try {
            resultado.launch("image/*")
        } catch (e: Exception) {
            Toast.makeText(this, "Error en la dirección de la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Obtiene la fecha y la pone en el boton
     */
    fun obtenerFecha(view: View) {
        val fechaActual = Calendar.getInstance()
        
        DatePickerDialog(this, { _, año, mes, dia ->
            //Se agrega el 0 si es menor a 10 para que se cumpla el formato
            var mesCorregido = mes.toString()
            var diaCorregido = dia.toString()
            if (mes < 10)
                mesCorregido = "0$mesCorregido"
            if (dia < 10)
                diaCorregido = "0$diaCorregido"

            val texto = "$diaCorregido/$mesCorregido/$año"
            binding.fecha.text = texto
        }, fechaActual.get(Calendar.YEAR), fechaActual.get(Calendar.MONTH), fechaActual.get(Calendar.DAY_OF_MONTH)).show()
    }

    /**
     * Obtiene la hora y la pone en el boton
     */
    fun obtenerHora(view: View) {
        TimePickerDialog(this, { _, hora, minutos ->
            //Se agrega el 0 si es menor a 10 para que se cumpla el formato
            var horaCorregido = hora.toString()
            var minCorregido = minutos.toString()
            if (hora < 10)
                horaCorregido = "0$horaCorregido"
            if (minutos < 10)
                minCorregido = "0$minCorregido"
            val texto = "$horaCorregido:$minCorregido"
            binding.hora.text = texto
        }, 12, 0, true)
            .show()
    }
}