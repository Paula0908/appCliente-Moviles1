package com.example.appcliente_moviles1.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appcliente_moviles1.databinding.FragmentChatBinding
import com.example.appcliente_moviles1.ui.adapters.ChatAdapter
import com.example.appcliente_moviles1.ui.viewmodels.ChatViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class ChatFragment : Fragment() {
    private var pollingJob: Job? = null

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()

    private val args: ChatFragmentArgs by navArgs()
    private var adapter: ChatAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val citaId = args.citaId
        val trabajadorId = args.trabajadorId


        viewModel.cargarTrabajador(requireContext(), trabajadorId)

        viewModel.trabajador.observe(viewLifecycleOwner) { trabajador ->
            if (trabajador != null) {
                binding.lblUserChat.text = "${trabajador.user.name} ${trabajador.user.profile.last_name}"
                if (!trabajador.picture_url.isNullOrBlank() && trabajador.picture_url != "null") {
                    Glide.with(binding.fotoUser.context)
                        .load(trabajador.picture_url)
                        .into(binding.fotoUser)
                } else {
                    binding.fotoUser.setImageResource(android.R.drawable.sym_def_app_icon)
                }
            } else {
                binding.lblUserChat.text = "Trabajador no encontrado"
                binding.fotoUser.setImageResource(android.R.drawable.sym_def_app_icon)
            }
        }
        // cambio que se puede hacer mejor pero ya lo hice asi :
        val idParaChat = viewModel.trabajador.value?.user?.id
        adapter = ChatAdapter(emptyList(), idParaChat ?: 0)
        binding.rvMensajes.adapter = adapter
        binding.rvMensajes.layoutManager = LinearLayoutManager(requireContext())

        viewModel.cargarMensajes(requireContext(), citaId)
        viewModel.mensajes.observe(viewLifecycleOwner) { mensajes ->
            adapter?.updateMensajes(mensajes, viewModel.trabajador.value?.user?.id ?: trabajadorId)
            if (mensajes.isNotEmpty()) {
                binding.rvMensajes.scrollToPosition(mensajes.size - 1)
            }
        }

        binding.btnSendMensaje.setOnClickListener {
            val texto = binding.inputMensaje.text.toString().trim()
            if (texto.isNotEmpty()) {
                viewModel.enviarMensaje(requireContext(), citaId, texto, viewModel.trabajador.value?.user?.id ?: trabajadorId)
                binding.inputMensaje.text.clear()
            }
        }
        binding.btnConcretarCita.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Concretar cita")
                .setMessage("¿Está seguro que desea concretar una cita?")
                .setPositiveButton("Sí") { dialog, _ ->
                    val action = ChatFragmentDirections
                        .actionChatFragmentToMapsFragment(
                            citaId = citaId
                        )
                    findNavController().navigate(action)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        startPollingMensajes(citaId)
    }
    private fun startPollingMensajes(citaId: Int) {
        pollingJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                viewModel.cargarMensajes(requireContext(), citaId)
                delay(2000) // 2 segundos
            }
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
