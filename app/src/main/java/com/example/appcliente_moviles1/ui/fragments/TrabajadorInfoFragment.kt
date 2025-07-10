package com.example.appcliente_moviles1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appcliente_moviles1.R
import com.example.appcliente_moviles1.databinding.FragmentTrabajadorInfoBinding
import com.example.appcliente_moviles1.models.Trabajador
import com.example.appcliente_moviles1.models.Review
import com.example.appcliente_moviles1.ui.adapters.ReviewAdapter
import com.example.appcliente_moviles1.viewmodels.TrabajadorInfoViewModel


class TrabajadorInfoFragment : Fragment() {

    private var _binding: FragmentTrabajadorInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TrabajadorInfoViewModel by viewModels()
    private val args: TrabajadorInfoFragmentArgs by navArgs() // <- SafeArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrabajadorInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trabajadorId = args.trabajadorId

        // Configura RecyclerView de reviews
        binding.rvReviews.layoutManager = LinearLayoutManager(requireContext())


        viewModel.trabajador.observe(viewLifecycleOwner) { trabajador ->
            if (trabajador != null) {
                bindTrabajador(trabajador)
            } else {
                Toast.makeText(requireContext(), "Trabajador no encontrado", Toast.LENGTH_SHORT).show()

            }
        }


        viewModel.cargarTrabajador(requireContext(), trabajadorId)

        binding.btnContactar.setOnClickListener {
            val trabajador = viewModel.trabajador.value
            val categoriaId = trabajador?.categories?.firstOrNull()?.id
            if (trabajador != null && categoriaId != null) {
                viewModel.obtenerCitaExistente(requireContext(), trabajador.id, categoriaId) { citaExistente ->
                    if (citaExistente != null) {
                        val action = TrabajadorInfoFragmentDirections
                            .actionTrabajadorInfoFragmentToChatFragment(
                                citaId = citaExistente.id,
                                trabajadorId = trabajador.id
                            )
                        findNavController().navigate(action)
                    } else {
                        // Si no existe, crea la cita y navega
                        viewModel.crearCita(requireContext(), trabajador.id, categoriaId) { cita ->
                            if (cita != null) {
                                val action = TrabajadorInfoFragmentDirections
                                    .actionTrabajadorInfoFragmentToChatFragment(
                                        citaId = cita.id,
                                        trabajadorId = trabajador.id
                                    )
                                findNavController().navigate(action)
                            } else {
                                Toast.makeText(requireContext(), "No se pudo crear la cita", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Faltan datos del trabajador o la categoría", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindTrabajador(trabajador: Trabajador) {
        // Nombre completo
        binding.lblNombreInfo.text = "${trabajador.user.name} ${trabajador.user.last_name}"

        // Calificación promedio
        val rating = trabajador.average_rating?.toDoubleOrNull() ?: 0.0
        binding.lblPromedioInfo.text = "Calificación: %.1f".format(rating)

        // Trabajos completados
        binding.lblTrabajosCompletos.text = "Trabajos completados: ${trabajador.reviews_count}"

        // Imagen de perfil con Glide
        if (!trabajador.picture_url.isNullOrBlank() && trabajador.picture_url != "null") {
            Glide.with(binding.fotoTrabajadorInfo.context)
                .load(trabajador.picture_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.fotoTrabajadorInfo)
        } else {
            binding.fotoTrabajadorInfo.setImageResource(R.drawable.ic_launcher_foreground)
        }

        // Categorías
        val categorias = trabajador.categories?.joinToString(", ") { it.name } ?: "Sin categorías"
        binding.lblTrabajadorCategorias.text = "Categorías: $categorias"

        // Reviews
        val reviews: List<Review> = trabajador.reviews ?: emptyList()
        binding.rvReviews.adapter = ReviewAdapter(reviews)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
