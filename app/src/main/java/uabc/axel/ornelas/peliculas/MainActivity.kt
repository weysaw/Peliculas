package uabc.axel.ornelas.peliculas

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * Referencias: https://handyopinion.com/show-alert-dialog-with-an-input-field-edittext-in-android-kotlin/
 *
 */
class MainActivity(
    var peliculas: ArrayList<Pelicula> = arrayListOf()
) : AppCompatActivity() {
    //Callback que se ejecuta cuando se acaba la activadad de registrar Pelicula
    private val resultado =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //Si no se termina correctamente
            if (Activity.RESULT_OK != it.resultCode) {
                //Muestra el mensaje del registro
                Toast.makeText(
                    this,
                    "Se cancelo el registro", Toast.LENGTH_SHORT
                ).show()
                return@registerForActivityResult
            }
            //Obtiene la pelicula del intent
            val pelicula = it.data?.getParcelableExtra<Pelicula>("peliculas")
            //Registra la pelicula si la devuelve correctamente
            if (pelicula != null)
                agregarPelicula(pelicula)
        }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val convertidor = SimpleDateFormat("dd-MM-yyyy hh:mm")
        //Datos que se agregan para inicializar el arreglo
        peliculas.add(
            Pelicula(
                "Mona Chida",
                "Comedia",
                5.0f,
                "Esta bien",
                convertidor.parse("25-12-2005 12:00"),
                2005,
                BitmapFactory.decodeResource(resources, R.drawable.miku)
            )
        )
        peliculas.add(
            Pelicula(
                "Goku Manotazo",
                "Terror",
                3.5f,
                "Te pega",
                convertidor.parse("12-05-2012 13:00"),
                2011,
                BitmapFactory.decodeResource(resources, R.drawable.manotazo)
            )
        )
        peliculas.add(
            Pelicula(
                "La Roca Patas",
                "Acción",
                2.0f,
                "PATAS",
                convertidor.parse("20-11-2008 14:00"),
                2006,
                BitmapFactory.decodeResource(resources, R.drawable.patas)
            )
        )
        peliculas.add(
            Pelicula(
                "La Rubia perrona",
                "Comedia",
                4.6f,
                "Esta perron",
                convertidor.parse("30-12-2016 12:00"),
                2016,
                BitmapFactory.decodeResource(resources, R.drawable.rubia)
            )
        )
    }

    /**
     * Se encarga de registrar la pelicula en el arreglo iniciando la otra actividad
     */
    fun registrar(view: View) {
        val intent = Intent(this, RegistrarPelicula::class.java)
        resultado.launch(intent)
    }

    /**
     * Agrega la pelicula validando si hay un dato repetido
     */
    private fun agregarPelicula(pelicula: Pelicula) {
        val toast =
            Toast.makeText(this, "No se agrego porque el nombre esta repetido", Toast.LENGTH_SHORT)
        val peliculaRepetida = peliculas.find { peli ->
            peli.nombre == pelicula.nombre
        }
        //Si peliculaRepetida existe significa que el elemnto esta repetido
        if (peliculaRepetida != null) {
            toast.show()
            return
        }
        peliculas.add(pelicula)
        toast.setText("Pelicula registrada con exito")
        toast.show()
    }

    /**
     * Consulta el numero de elementos que hay en el arreglo
     */
    fun consultar(view: View) {
        //Codigo para abrir la otra clase
        println(peliculas)
        Toast.makeText(this, "La cantidad de registros es ${peliculas.size}", Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * Busca las peliculas mayores a 4.5 y los muestra
     */
    fun buscarFavoritos(v: View) {
        //Filtra los elementos que tengan 4.5f para arriba
        val favoritos = peliculas.filter {
            it.rating > 4.5f
        }
        println(favoritos)
    }

    /**
     * Muestra la información de la pelicula indicada
     */
    fun buscarPorTitulo(v: View) {
        //El campo de texto para poner en el dialog
        val campoTitulo = obtenerEditText("Titulo Pelicula")

        //Creo con la configuracion adecuado y muestro el dialogo
        AlertDialog.Builder(this)
            .setTitle("Buscar por titulo")
            .setMessage("Ingrese el tiulo de la pelicula a buscar")
            .setPositiveButton("OK") { _, _ ->
                val tituloPeli = campoTitulo.text.toString()
                val pelicula = peliculas.find {
                    //Compara el titulo ignorando el tamaño
                    it.nombre.equals(tituloPeli, true)
                }
                val toast = Toast.makeText(
                    this,
                    "No se ha encontrado el titulo de esa pelicula",
                    Toast.LENGTH_SHORT
                )
                //Muesta la info de la peli si la encuentra
                if (pelicula != null)
                    toast.setText(pelicula?.toString())
                toast.show()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setView(campoTitulo)
            .create().show()
    }

    /**
     * Elimina la información de la pelicula indicada
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun eliminarPorTitulo(v: View) {
        //El campo de texto para poner en el dialog
        val campoTitulo: EditText = obtenerEditText("Titulo pelicula")

        //Creo con la configuracion adecuado y muestro el dialogo
        AlertDialog.Builder(this)
            .setTitle("Eliminar por titulo")
            .setMessage("Ingrese el tiulo de la pelicula a eliminar")
            .setPositiveButton("OK") { _, _ ->
                val tituloPeli = campoTitulo.text.toString()
                val borrado = peliculas.removeIf {
                    //Compara el titulo ignorando el tamaño
                    it.nombre.equals(tituloPeli, true)
                }
                val texto = if (borrado) "Pelicula eliminada con exito" else "Pelicula no encontrada"
                Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setView(campoTitulo)
            .create().show()
    }

    /**
     * Obtiene un editText se usa para ingresar al dialog
     */
    private fun obtenerEditText(texto: String): EditText {
        val editText = EditText(this)
        editText.hint = texto
        editText.inputType = InputType.TYPE_CLASS_TEXT
        return editText
    }

}