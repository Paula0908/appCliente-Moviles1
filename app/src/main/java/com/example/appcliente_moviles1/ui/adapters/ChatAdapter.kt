package com.example.appcliente_moviles1.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente_moviles1.databinding.MensajeEnviadoItemBinding
import com.example.appcliente_moviles1.databinding.MensajeRecibidoItemBinding
import com.example.appcliente_moviles1.models.chat.ChatMessage

class ChatAdapter(
    private var mensajes: List<ChatMessage>,
    private var trabajadorId: Int // lo pasas desde el fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ENVIADO = 1
        private const val VIEW_TYPE_RECIBIDO = 2
    }

    override fun getItemViewType(position: Int): Int {
        // Si el receiver_id es el del trabajador, entonces es ENVIADO POR TI (el cliente)
        // Si no, es RECIBIDO (te lo manda el trabajador)
        return if (mensajes[position].receiver_id == trabajadorId) VIEW_TYPE_ENVIADO else VIEW_TYPE_RECIBIDO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ENVIADO) {
            val binding = MensajeEnviadoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            EnviadoViewHolder(binding)
        } else {
            val binding = MensajeRecibidoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            RecibidoViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = mensajes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensaje = mensajes[position]
        if (holder is EnviadoViewHolder) {
            holder.binding.txtMensaje.text = mensaje.message
        } else if (holder is RecibidoViewHolder) {
            holder.binding.txtMensaje.text = mensaje.message
        }
    }

    fun updateMensajes(nuevaLista: List<ChatMessage>, trabajadorId: Int) {
        this.mensajes = nuevaLista
        this.trabajadorId = trabajadorId
        notifyDataSetChanged()
    }

    inner class EnviadoViewHolder(val binding: MensajeEnviadoItemBinding) : RecyclerView.ViewHolder(binding.root)
    inner class RecibidoViewHolder(val binding: MensajeRecibidoItemBinding) : RecyclerView.ViewHolder(binding.root)
}
