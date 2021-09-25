package uabc.axel.ornelas.peliculas

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*


/**
 *
 * Referencias: https://handyopinion.com/show-alert-dialog-with-an-input-field-edittext-in-android-kotlin/
 *
 */
class MainActivity(
    private var peliculas: ArrayList<Pelicula> = arrayListOf()
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
        //Dirección del archivo
        val archivo = "android.resource://$packageName/drawable/"
        val convertidor = SimpleDateFormat("dd-MM-yyyy hh:mm")
        //Datos que se agregan para inicializar el arreglo
        peliculas.add(
            Pelicula(
                "Mona Chida",
                "Comedia",
                5.0f,
                "Esta bien",
                convertidor.parse("25-12-2005 12:00").time,
                2005,
                Uri.parse(archivo + R.drawable.miku)
            )
        )
        peliculas.add(
            Pelicula(
                "Goku Manotazo",
                "Terror",
                3.5f,
                "Te pega",
                convertidor.parse("12-05-2012 13:00").time,
                2011,
                Uri.parse(archivo + R.drawable.manotazo)
            )
        )
        peliculas.add(
            Pelicula(
                "La Roca Patas",
                "Acción",
                2.0f,
                "PATAS",
                convertidor.parse("20-11-2008 14:00").time,
                2006,
                Uri.parse(archivo + R.drawable.patas)
            )
        )
        peliculas.add(
            Pelicula(
                "La Rubia perrona",
                "Comedia",
                4.6f,
                "Esta perron",
                convertidor.parse("30-12-2016 12:00").time,
                2016,
                Uri.parse(archivo + R.drawable.rubia)
            )
        )
    }

    /**
     * Se encarga de registrar la pelicula en el arreglo iniciando la otra actividad
     */
    fun registrar(view: View) {
        val intent = Intent(applicationContext, RegistrarPelicula::class.java)
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
        Toast.makeText(this, "La cantidad de registros es ${peliculas.size}", Toast.LENGTH_SHORT)
            .show()
        val intent = Intent(applicationContext, MostrarPeliculas::class.java)
        intent.putParcelableArrayListExtra("peliculas", peliculas)
        startActivity(intent)
    }

    /**
     * Busca las peliculas mayores a 4.5 y los muestra
     */
    fun buscarFavoritos(v: View) {
        //Filtra los elementos que tengan 4.5f para arriba
        val favoritos = peliculas.filter {
            it.rating > 4.5f
        } as ArrayList<Pelicula>

        val intent = Intent(this, MostrarPeliculas::class.java)
        intent.putExtra("peliculas", favoritos)
        startActivity(intent)
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
                //Muestra la info de la peli si la encuentra
                if (pelicula != null) {
                    val intent = Intent(this, InformacionPelicula::class.java)
                    intent.putExtra("pelicula", pelicula)
                    startActivity(intent)
                } else {
                    val texto = "No se ha encontrado el titulo de esa pelicula"
                    Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
                }

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
                val texto =
                    if (borrado) "Pelicula eliminada con exito" else "Pelicula no encontrada"
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