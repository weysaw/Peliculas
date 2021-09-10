package uabc.axel.ornelas.semana4controlesdeentrada

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*
import kotlin.collections.ArrayList

class MainActivity(
    var peliculas: ArrayList<Pelicula> = ArrayList()
) : AppCompatActivity(){

    private val resultado = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (Activity.RESULT_OK != it.resultCode) {
            Toast.makeText(
                this,
                "Se cancelo el registro", Toast.LENGTH_SHORT
            ).show()
            return@registerForActivityResult
        }
        val pelicula = it.data?.getParcelableExtra<Pelicula>("pelicula")
        if (pelicula != null)
            peliculas.add(pelicula)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun registrar(view: android.view.View) {
        val intent = Intent(applicationContext, RegistrarPelicula::class.java)
        intent.putExtra("peliculas", peliculas)
        resultado.launch(intent)
    }

    fun consultar(view: android.view.View) {
        //Codigo para abrir la otra clase
        println(peliculas)
        Toast.makeText(this, "La cantidad de registros es ${peliculas.size}", Toast.LENGTH_SHORT).show()
    }
}