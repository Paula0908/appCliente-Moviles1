package com.example.appcliente_moviles1.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente_moviles1.databinding.CitaItemBinding
import com.example.appcliente_moviles1.models.CitaResponse
import androidx.core.graphics.toColorInt

class CitasAdapter(
    private var citas: List<CitaResponse>,
    private val onItemClick: (CitaResponse) -> Unit
) : RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    inner class CitaViewHolder(val binding: CitaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cita: CitaResponse) {
            // Mostrar nombre completo del worker
            val nombre = cita.worker?.user?.let { "${it.name} ${it.profile.last_name}" } ?: "Trabajador"
            binding.lblNombreTrabajador.text = nombre

            // Mostrar fecha y hora TwT
            binding.lblFecha.text = cita.appointment_date ?: "--/--/----"
            binding.textView4.text = cita.appointment_time ?: "--:--"

            // Mostrar estado concretado y no :p
            binding.lblEstadoCita.text = when (cita.status) {
                1 -> "Concretada :3"
                2 -> "Aceptada :D"
                3 -> "Completada :)"
                else -> "No concretada :P"
            }
            binding.lblEstadoCita.setTextColor(when (cita.status) {
                1 -> "#6d23db".toColorInt() // Concretada 6d23db
                2 -> "#23a9db".toColorInt() // Aceptada 23a9db
                3 -> "#00c01b".toColorInt() // Completada 00c01b
                else -> "#FF5252".toColorInt() // No concretada FF5252
            })
            binding.lblEstadoCita.setBackgroundColor(when (cita.status) {
                1 -> "#146d23db".toColorInt()// Concretada 146d23db
                2 -> "#1423a9db".toColorInt()// Aceptada 1423a9db
                3 -> "#1400c01b".toColorInt()// Completada 1400c01b
                else -> "#14FF5252".toColorInt()// No concretada 14FF5252
            })



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
