package com.example.appcliente_moviles1.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appcliente_moviles1.databinding.FragmentFechaHoraBinding
import com.example.appcliente_moviles1.ui.viewmodels.FechaHoraViewModel
import java.util.*

class FechaHoraFragment : Fragment() {

    private var _binding: FragmentFechaHoraBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FechaHoraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFechaHoraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtiene los argumentos (usando Safe Args)
        val args = FechaHoraFragmentArgs.fromBundle(requireArguments())
        val latitud = args.latitud.toString()
        val longitud = args.longitud.toString()
        val citaId = args.citaId

        val calendar = Calendar.getInstance()

        // Observa los cambios en la fecha/hora/resultados
        viewModel.fecha.observe(viewLifecycleOwner) { fecha ->
            if (fecha != null) binding.btnSeleccionarFecha.text = fecha
        }
        viewModel.hora.observe(viewLifecycleOwner) { hora ->
            if (hora != null) binding.btnSeleccionarHora.text = hora
        }
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnConcretarCita.isEnabled = !isLoading
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.citaResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Toast.makeText(requireContext(), "¡Cita concretada!", Toast.LENGTH_SHORT).show()
                // Navegar a la pantalla de citas
                val action = FechaHoraFragmentDirections
                    .actionFechaHoraFragmentToMisCitasFragment()
                findNavController().navigate(action)

            }
        }

        // Seleccionar fecha
        binding.btnSeleccionarFecha.setOnClickListener {
            val hoy = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val fecha = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                    viewModel.setFecha(fecha)
                    calendar.set(year, month, dayOfMonth)
                },
                hoy.get(Calendar.YEAR),
                hoy.get(Calendar.MONTH),
                hoy.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Seleccionar hora
        binding.btnSeleccionarHora.setOnClickListener {
            val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
            val minutoActual = calendar.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val hora = "%02d:%02d".format(hourOfDay, minute) // Formato 24h
                    viewModel.setHora(hora)
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                },
                horaActual,
                minutoActual,
                true // true para 24h, false para 12h
            )
            timePicker.show()
        }

        // Botón concretar cita
        binding.btnConcretarCita.setOnClickListener {
            val fecha = viewModel.fecha.value
            val hora = viewModel.hora.value
            if (fecha.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Seleccione una fecha", Toast.LENGTH_SHORT).show()
            } else if (hora.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Seleccione una hora", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.concretarCita(
                    requireContext(),
                    citaId,
                    fecha,
                    hora,
                    latitud,
                    longitud
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
