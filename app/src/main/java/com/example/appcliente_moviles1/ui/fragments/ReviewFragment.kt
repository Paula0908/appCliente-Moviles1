package com.example.appcliente_moviles1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.appcliente_moviles1.databinding.FragmentReviewBinding
import com.example.appcliente_moviles1.repositories.ApiRepository
import kotlinx.coroutines.launch

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private val args: ReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnEnviarReview.setOnClickListener {
            val rating = binding.ratingBar.rating.toInt()
            val comentario = binding.inputComentario.text.toString().ifBlank { null }
            val citaId = args.citaId
            val isDone = if (binding.checkCompletado.isChecked) 1 else 0

            if (rating == 0) {
                Toast.makeText(requireContext(), "Selecciona una puntuación porfa :3", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val ok = ApiRepository.agregarReview(
                    requireContext(),
                    citaId = citaId,
                    rating = rating,
                    comment = comentario,
                    isDone = isDone
                )
                if (ok) {
                    Toast.makeText(requireContext(), "¡Reseña enviada :D!", Toast.LENGTH_SHORT).show()
                    val action = ReviewFragmentDirections.actionReviewFragmentToCategoriasFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Error al enviar la reseña :(", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
