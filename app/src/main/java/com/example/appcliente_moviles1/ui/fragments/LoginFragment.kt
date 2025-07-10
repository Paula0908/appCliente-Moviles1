package com.example.appcliente_moviles1.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appcliente_moviles1.R
import com.example.appcliente_moviles1.databinding.FragmentLoginBinding
import com.example.appcliente_moviles1.ui.viewmodels.LoginViewModel
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import com.example.appcliente_moviles1.utils.getToken
import com.example.appcliente_moviles1.utils.saveToken
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CHEQUEA SI YA HAY TOKEN GUARDADO Y SALTA LOGIN
        viewLifecycleOwner.lifecycleScope.launch {
            val token = getToken(requireContext())
            if (!token.isNullOrBlank()) {
                findNavController().navigate(R.id.action_LoginFragment_CategoriasFragment)
                return@launch
            }
        }

        // Observa los resultados del login
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result == "ok") {
                val token = loginViewModel.accessToken.value
                if (!token.isNullOrBlank()) {
                    lifecycleScope.launch {
                        saveToken(requireContext(), token)
                        findNavController().navigate(R.id.action_LoginFragment_CategoriasFragment)
                    }
                }
                // Oculta teclado, limpia campos y mensaje
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                binding.inputEmail.setText("")
                binding.inputPassword.setText("")
                Toast.makeText(requireContext(), "Login Exitoso.", Toast.LENGTH_SHORT).show()
            } else {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            }
        }

        // Acciones del botón de login
        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text?.toString()?.trim() ?: ""
            val password = binding.inputPassword.text?.toString()?.trim() ?: ""
            loginViewModel.login(email, password)
        }
        binding.btnRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegistroFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
