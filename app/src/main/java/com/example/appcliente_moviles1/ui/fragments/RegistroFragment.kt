package com.example.appcliente_moviles1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appcliente_moviles1.R
import com.example.appcliente_moviles1.databinding.FragmentRegistroBinding
import com.example.appcliente_moviles1.ui.viewmodels.RegistroViewModel


class RegistroFragment : Fragment() {

    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    private val registroViewModel: RegistroViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registroViewModel.registroResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Limpia los campos
                binding.inputNombre.setText("")
                binding.inputApellido.setText("")
                binding.inputEmail.setText("")
                binding.inputPassword.setText("")

                // Oculta el teclado Uwu
                val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)

                Toast.makeText(requireContext(), "Registro exitoso. Inicia sesión.", Toast.LENGTH_SHORT).show()

                // Navega al login
                findNavController().navigate(R.id.action_RegistroFragment_to_LoginFragment)

            } else {
                // Oculta el teclado Uwu
                val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)

                Toast.makeText(requireContext(), "Error en el registro.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegistrarse.setOnClickListener {
            val nombre = binding.inputNombre.text?.toString()?.trim() ?: ""
            val apellido = binding.inputApellido.text?.toString()?.trim() ?: ""
            val email = binding.inputEmail.text?.toString()?.trim() ?: ""
            val password = binding.inputPassword.text?.toString()?.trim() ?: ""
            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
                // Oculta el teclado
                val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                // Muestra Toast
                Toast.makeText(requireContext(), "Completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registroViewModel.registrar(nombre, apellido, email, password)
        }


        binding.btnIniciarSesion.setOnClickListener {
            findNavController().navigate(R.id.action_RegistroFragment_to_LoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
