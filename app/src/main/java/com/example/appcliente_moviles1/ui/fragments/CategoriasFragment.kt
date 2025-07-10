package com.example.appcliente_moviles1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcliente_moviles1.databinding.FragmentCategoriasBinding
import com.example.appcliente_moviles1.models.Categoria
import com.example.appcliente_moviles1.ui.adapters.CategoriaAdapter
import com.example.appcliente_moviles1.ui.viewmodels.CategoriasViewModel

class CategoriasFragment : Fragment() {

    private var _binding: FragmentCategoriasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriasViewModel by viewModels()
    private lateinit var adapter: CategoriaAdapter
    private var categoriasOriginales: List<Categoria> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoriaAdapter(emptyList()) { categoriaSeleccionada ->
            val action = CategoriasFragmentDirections
                .actionCategoriasFragmentToTrabajadoresFragment(categoriaSeleccionada.id)
            findNavController().navigate(action)
        }
        binding.rvCategorias.adapter = adapter
        binding.rvCategorias.layoutManager = LinearLayoutManager(requireContext())

        // Viewmodel
        viewModel.categorias.observe(viewLifecycleOwner) { lista ->
            Toast.makeText(requireContext(), "Cantidad: ${lista.size}", Toast.LENGTH_SHORT).show()
            categoriasOriginales = lista.ifEmpty {
                listOf(
                    Categoria(1, "categoria 1 :3"),
                    Categoria(2, "categoria 2 :p")
                )
            }
            adapter.updateCategorias(categoriasOriginales)
        }

        viewModel.cargarCategorias(requireContext())

        // btn mis citas
        binding.btnMisCitas.setOnClickListener {
            findNavController().navigate(
                CategoriasFragmentDirections.actionCategoriasFragmentToMisCitasFragment()
            )
        }

        // Buscador
        binding.btnBuscar.setOnClickListener {
            val busqueda = binding.categoriasBuscador.text?.toString()?.trim() ?: ""
            val filtradas = if (busqueda.isBlank()) {
                categoriasOriginales
            } else {
                categoriasOriginales.filter {
                    it.name.contains(busqueda, ignoreCase = true)
                }
            }
            adapter.updateCategorias(filtradas)
            Toast.makeText(requireContext(), "Filtradas: ${filtradas.size}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
