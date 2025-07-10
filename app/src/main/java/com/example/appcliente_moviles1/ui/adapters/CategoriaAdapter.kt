package com.example.appcliente_moviles1.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcliente_moviles1.databinding.CategoriaItemBinding
import com.example.appcliente_moviles1.models.Categoria

class CategoriaAdapter(
    private var categorias: List<Categoria>,
    private val onClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    inner class CategoriaViewHolder(val binding: CategoriaItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoria: Categoria) {
            binding.lblCategoria.text = categoria.name
            binding.root.setOnClickListener {
                onClick(categoria)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val binding = CategoriaItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoriaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        holder.bind(categorias[position])
    }

    override fun getItemCount(): Int = categorias.size

    fun updateCategorias(newCategorias: List<Categoria>) {
        categorias = newCategorias
        notifyDataSetChanged()
    }
}
