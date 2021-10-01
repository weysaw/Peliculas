package uabc.axel.ornelas.peliculas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorRecycler(private val localDataSet: ArrayList<Pelicula>) : RecyclerView.Adapter<AdaptadorRecycler.ViewHolder>() {

    //Tal vez se deba poner protected
    lateinit var onClickListener: View.OnClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        //Indica el estilo que debe tener el recycler
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fila_pelicula, parent, false)
        view.setOnClickListener(onClickListener)
        //Devuelve la vista creada
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Coloca la información de las peliculas a los campos de texto
        holder.nombre.text = localDataSet[position].nombre
        holder.genero.text = localDataSet[position].genero
        holder.rating.text = localDataSet[position].rating.toString()
        holder.imagen.setImageURI(localDataSet[position].imagen)
    }

    /**
     * Tamaño de los datos
     */
    override fun getItemCount(): Int {
        return localDataSet.size
    }

    /**
     * Clase interna para localizar los datos que se necesitan para colocar la info
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombreText)
        val genero: TextView = itemView.findViewById(R.id.generoText)
        val rating: TextView = itemView.findViewById(R.id.ratingText)
        val imagen: ImageView = itemView.findViewById(R.id.imagenFila)

    }
}