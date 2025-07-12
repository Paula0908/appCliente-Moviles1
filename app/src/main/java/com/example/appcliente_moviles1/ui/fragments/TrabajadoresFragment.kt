package com.example.appcliente_moviles1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.navArgs
import com.example.appcliente_moviles1.databinding.FragmentTrabajadoresBinding
import com.example.appcliente_moviles1.models.Trabajador
import com.example.appcliente_moviles1.ui.adapters.TrabajadorAdapter
import com.example.appcliente_moviles1.ui.viewmodels.TrabajadoresViewModel

class TrabajadoresFragment : Fragment() {
    private var _binding: FragmentTrabajadoresBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TrabajadoresViewModel by viewModels()
    private val args: TrabajadoresFragmentArgs by navArgs()
    private lateinit var adapter: TrabajadorAdapter

    // Guarda la lista original para filtrar
    private var trabajadoresOriginales: List<Trabajador> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrabajadoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrabajadorAdapter(emptyList()) { trabajadorSeleccionado ->
            val action = TrabajadoresFragmentDirections
                .actionTrabajadoresFragmentToTrabajadorInfoFragment(trabajadorSeleccionado.id)
            findNavController().navigate(action)
        }
        binding.rvTrabajadores.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTrabajadores.adapter = adapter

        // Viewmodel
        viewModel.trabajadores.observe(viewLifecycleOwner) { lista ->
            trabajadoresOriginales = lista
            adapter.updateTrabajadores(lista)
        }

        viewModel.cargarTrabajadoresPorCategoria(requireContext(), args.categoriaId)

        // Buscador
        binding.btnBuscar2.setOnClickListener {
            val busqueda = binding.trabajadoresBuscador.text?.toString()?.trim() ?: ""
            val filtrados = if (busqueda.isBlank()) {
                trabajadoresOriginales
            } else {
                trabajadoresOriginales.filter {
                    (it.user.name ?: "").contains(busqueda, ignoreCase = true) ||
                            (it.user.profile.last_name ?: "").contains(busqueda, ignoreCase = true)
                }

            }
            adapter.updateTrabajadores(filtrados)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

