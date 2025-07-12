package com.example.appcliente_moviles1.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcliente_moviles1.R
import com.example.appcliente_moviles1.databinding.TrabajadorItemBinding
import com.example.appcliente_moviles1.models.Trabajador

class TrabajadorAdapter(
    private var trabajadores: List<Trabajador>,
    private val onClick: (Trabajador) -> Unit
) : RecyclerView.Adapter<TrabajadorAdapter.TrabajadorViewHolder>() {

    inner class TrabajadorViewHolder(val binding: TrabajadorItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrabajadorViewHolder {
        val binding = TrabajadorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrabajadorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrabajadorViewHolder, position: Int) {
        val trabajador = trabajadores[position]
        holder.binding.apply {
            lblTrabajadorName.text = "${trabajador.user.name} ${trabajador.user.profile.last_name}"
            if (trabajador.average_rating != null)
                lblPromedio.text = "${trabajador.average_rating} ★"
            else
                lblPromedio.text = "0 ★"
            lblReviews.text =  "${trabajador.reviews_count.toString()} Trabajos Completados"

            if (!trabajador.picture_url.isNullOrEmpty() && trabajador.picture_url != "null") {
                Glide.with(fotoTrabajador.context)
                    .load(trabajador.picture_url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(fotoTrabajador)
            } else {
                fotoTrabajador.setImageResource(R.drawable.ic_launcher_foreground)
            }
            root.setOnClickListener { onClick(trabajador) }
        }
    }

    override fun getItemCount() = trabajadores.size

    fun updateTrabajadores(nuevaLista: List<Trabajador>) {
        trabajadores = nuevaLista
        notifyDataSetChanged()
    }
}
