package uabc.axel.ornelas.peliculas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorRecycler(private val localDataSet: ArrayList<Pelicula>) : RecyclerView.Adapter<AdaptadorRecycler.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fila_pelicula, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nombre.text = localDataSet[position].nombre
        holder.genero.text = localDataSet[position].genero
        holder.rating.text = localDataSet[position].rating.toString()

    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombreText)
        val genero: TextView = itemView.findViewById(R.id.generoText)
        val rating: TextView = itemView.findViewById(R.id.ratingText)
    }

}