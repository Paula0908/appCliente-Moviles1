package com.example.appcliente_moviles1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcliente_moviles1.R
import com.example.appcliente_moviles1.databinding.FragmentMisCitasBinding
import com.example.appcliente_moviles1.ui.adapters.CitasAdapter
import com.example.appcliente_moviles1.ui.viewmodels.MisCitasViewModel
import com.example.appcliente_moviles1.utils.clearToken
import kotlinx.coroutines.launch

class MisCitasFragment : Fragment() {
    private var _binding: FragmentMisCitasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MisCitasViewModel by viewModels()
    private lateinit var adapter: CitasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisCitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
        adapter = CitasAdapter(emptyList()) { cita ->
            Toast.makeText(requireContext(), "ID de la cita: ${cita.id}", Toast.LENGTH_SHORT).show()
            if (cita.status == 3) {
                // Mostrar dialog
                val builder = android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Cita completada")
                    .setMessage("¿Qué deseas hacer?")
                    .setPositiveButton("Ir al chat") { dialog, _ ->
                        // Navega al chat de esta cita
                        val action = MisCitasFragmentDirections
                            .actionMisCitasFragmentToChatFragment(
                                citaId = cita.id,
                                trabajadorId = cita.worker_id
                            )
                        findNavController().navigate(action)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Agregar reseña") { dialog, _ ->
                        // Navega a la pantalla de reseña, ajústalo a tu fragment real
                        val action = MisCitasFragmentDirections
                            .actionMisCitasFragmentToReviewFragment(
                                citaId = cita.id
                             )
                        findNavController().navigate(action)
                        dialog.dismiss()
                    }
                    .setNeutralButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.show()
            } else {
                val action = MisCitasFragmentDirections
                    .actionMisCitasFragmentToChatFragment(
                        citaId = cita.id,
                        trabajadorId = cita.worker_id
                    )
                findNavController().navigate(action)
            }
        }

        binding.rvMisCitas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMisCitas.adapter = adapter

        viewModel.misCitas.observe(viewLifecycleOwner) { citas ->
            val filtradas = citas
                .groupBy { it.worker_id to it.category }
                .mapNotNull { (_, lista) -> lista.maxByOrNull { it.id } }
                .sortedByDescending { it.id }
            adapter.updateCitas(filtradas)
        }

        viewModel.getMisCitas(requireContext())
    }
    private fun cerrarSesion() {
        lifecycleScope.launch {
            clearToken(requireContext())
            findNavController().navigate(
                R.id.action_MisCitasFragment_to_LoginFragment
            )
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
