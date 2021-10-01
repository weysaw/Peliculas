package uabc.axel.ornelas.peliculas

import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uabc.axel.ornelas.peliculas.databinding.ActivityRegistrarPeliculaBinding
import java.lang.Exception
import java.net.URLEncoder
import java.util.*

class RegistrarPelicula : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarPeliculaBinding
    private lateinit var imagen: Uri

    private val resultado = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        //Coloca la dirección de la imagen
        binding.imagen.setImageURI(it)
        imagen = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagen =
            Uri.parse("android.resource://$packageName/drawable/" + R.drawable.ic_launcher_background)
        binding = ActivityRegistrarPeliculaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Crea lista de generos
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
    fun registrar(v: View) {
        try {
            val intent = Intent()
            //Obtiene todos los datos
            val nombre: String = binding.nombrePelicula.text.toString()
            val genero: String = binding.lista.selectedItem.toString()
            val rating: Float = binding.puntuacion.rating
            val comentario: String = binding.comentario.text.toString()
            val convertidor = SimpleDateFormat("dd/MM/yyyy hh:mm")
            val anio: Int = binding.anio.text.toString().toInt()
            val fecha: Long =
                convertidor.parse(
                    "${binding.fecha.text} ${binding.hora.text}"
                ).time
            //Garantiza que se pueda abrir la imagen en otra actividad
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            applicationContext.contentResolver.takePersistableUriPermission(imagen, takeFlags)
            //Manda los datos a un objeto de pelicula
            val pelicula = Pelicula(nombre, genero, rating, comentario, fecha, anio, imagen)
            if (rating < 2) {
                val titulo = "$nombre con rating menor a 2"
                val texto = "Fue una perdida de tiempo :("
                mostrarNotificacion(titulo, texto, false, "")
            } else if (rating >= 4) {
                val titulo = "$nombre con rating mayor o igual 4.5"
                val texto = "Presiona para ver su trailer en el buscador"
                mostrarNotificacion(titulo, texto, true, "$nombre trailer")
            }

            //Pone la pelicula en el intent y dice que to.do resultdo ok
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
     * Se muestra la notificación en la pantalla del dispositivo conteniendo info relevante de la peli
     *
     * https://developer.android.com/training/notify-user/build-notification?hl=es-419
     * https://stackoverflow.com/questions/4800575/start-google-search-query-from-activity-android
     */
    private fun mostrarNotificacion(titulo: String,contenido: String, enlace: Boolean, url: String) {
        //Es el id del canal
        val canalID = "PeliculasId"
        //Verifica la versión del sdk
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Es el nombre del canal
            val nombreCanal = "Notificacion"
            val descriptionText = "Este va a hacer la notificacion"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(canalID, nombreCanal, importance).apply {
                description = descriptionText
            }
            // Registra el canal al sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        // Se crea la notificacion y se establecen los parametros
        val notificacion = NotificationCompat.Builder(applicationContext, canalID)
            .setSmallIcon(R.drawable.interrogacion_miku)
            .setContentTitle(titulo)
            .setContentText(contenido)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        if (enlace) {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, url)
            val requestID = 1
            val pendingIntent = PendingIntent.getActivity(applicationContext, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            notificacion.contentIntent = pendingIntent
        }
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, notificacion)
        }
    }

    /**
     * Abre el explorador de archivos para
     */
    fun cambiarImagen(view: View) {
        try {
            resultado.launch(arrayOf("image/*"))
        } catch (e: Exception) {
            Toast.makeText(this, "Error en la dirección de la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Obtiene la fecha y la pone en el boton
     */
    fun obtenerFecha(v: View) {
        val fechaActual = Calendar.getInstance()

        DatePickerDialog(this,
            { _, año, mes, dia ->
                //Se agrega el 0 si es menor a 10 para que se cumpla el formato
                var mesCorregido = mes.toString()
                var diaCorregido = dia.toString()
                if (mes < 10)
                    mesCorregido = "0$mesCorregido"
                if (dia < 10)
                    diaCorregido = "0$diaCorregido"

                val texto = "$diaCorregido/$mesCorregido/$año"
                binding.fecha.text = texto
            },
            fechaActual.get(Calendar.YEAR),
            fechaActual.get(Calendar.MONTH),
            fechaActual.get(Calendar.DAY_OF_MONTH)).show()
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