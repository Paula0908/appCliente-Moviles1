package com.example.appcliente_moviles1.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente_moviles1.databinding.CitaItemBinding
import com.example.appcliente_moviles1.models.CitaResponse

class CitasAdapter(
    private var citas: List<CitaResponse>,
    private val onItemClick: (CitaResponse) -> Unit
) : RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    inner class CitaViewHolder(val binding: CitaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cita: CitaResponse) {
            // Mostrar nombre completo del worker
            val nombre = cita.worker?.user?.let { "${it.name} ${it.last_name}" } ?: "Trabajador"
            binding.lblNombreTrabajador.text = nombre

            // Mostrar fecha y hora TwT
            binding.lblFecha.text = cita.appointment_date ?: "--/--/----"
            binding.textView4.text = cita.appointment_time ?: "--:--"

            // Mostrar estado concretado y no :p
            binding.lblEstadoCita.text = when (cita.status) {
                1 -> "Concretada :3"
                else -> "No concretada :P"
            }

            // Listener para la cita
            binding.root.setOnClickListener {
                onItemClick(cita)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = CitaItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        holder.bind(citas[position])
    }

    override fun getItemCount() = citas.size

    fun updateCitas(nuevas: List<CitaResponse>) {
        citas = nuevas
        notifyDataSetChanged()
    }
}
