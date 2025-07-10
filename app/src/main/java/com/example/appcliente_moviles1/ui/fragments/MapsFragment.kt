package com.example.appcliente_moviles1.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.appcliente_moviles1.R
import com.example.appcliente_moviles1.databinding.FragmentMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null

    // Modern permission launcher
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            moveCameraToUserLocation()
        } else {
            moveCameraToDefault()
        }
    }

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        checkLocationPermissionAndMove()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.btnConfirmarUbicacion.setOnClickListener {
            val map = googleMap ?: return@setOnClickListener
            val center = map.cameraPosition.target
            Toast.makeText(
                requireContext(),
                "Lat: ${center.latitude}, Lng: ${center.longitude}",
                Toast.LENGTH_LONG
            ).show()
            // Aquí haces lo que quieras con la coordenada.
        }
    }

    private fun checkLocationPermissionAndMove() {
        val context = requireContext()
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                moveCameraToUserLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(context, "Se necesita permiso de ubicación para mostrar tu posición.", Toast.LENGTH_LONG).show()
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun moveCameraToUserLocation() {
        val context = requireContext()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 17f))
                    // Si quieres que el usuario vea su punto azul, puedes activar esto:
                    googleMap?.isMyLocationEnabled = true
                } else {
                    moveCameraToDefault()
                }
            }.addOnFailureListener {
                moveCameraToDefault()
            }
        } catch (e: SecurityException) {
            moveCameraToDefault()
        }
    }

    private fun moveCameraToDefault() {
        // Ejemplo: SC, Bolivia
        val bolivia = LatLng(-17.783327, -63.182140)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(bolivia, 14f))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
