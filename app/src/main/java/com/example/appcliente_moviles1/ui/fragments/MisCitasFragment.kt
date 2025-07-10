package com.example.appcliente_moviles1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcliente_moviles1.databinding.FragmentMisCitasBinding
import com.example.appcliente_moviles1.ui.adapters.CitasAdapter
import com.example.appcliente_moviles1.ui.viewmodels.MisCitasViewModel

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

        adapter = CitasAdapter(emptyList()) { cita ->
            Toast.makeText(requireContext(), "ID de la cita: ${cita.id}", Toast.LENGTH_SHORT).show()
            val action = MisCitasFragmentDirections
                .actionMisCitasFragmentToChatFragment(
                    citaId = cita.id,
                    trabajadorId = cita.worker_id
                )
            findNavController().navigate(action)
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
