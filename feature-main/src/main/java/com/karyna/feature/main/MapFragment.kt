package com.karyna.feature.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.karyna.feature.main.databinding.FragmentMapBinding
import com.karyna.feature.main.map.PermissionsManager
import com.karyna.feature.main.map.Ui
import kotlin.properties.Delegates

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()
    private var googleMap: GoogleMap by Delegates.notNull()
    private var permissionsManager by Delegates.notNull<PermissionsManager>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = requireActivity() as AppCompatActivity
        permissionsManager = PermissionsManager(activity) { checkPermission(googleMap) }
        viewModel.init(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().lifecycle.addObserver(viewModel)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        //setup map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        with(viewModel) {
            ui.observe(viewLifecycleOwner, ::updateUi)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        permissionsManager.requestUserLocation()
    }

    private fun setClickListeners() {
        with(binding) {
            btnStart.setOnClickListener { viewModel.toggleTrackingLocation() }
        }
    }

    private fun updateUi(ui: Ui) {
        with(binding) {
            ui.currentLocation?.let {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 14f))
            }
            tvDistance.text = ui.formattedDistance
            tvPace.text = ui.formattedPace
        }
    }

    private fun checkPermission(googleMap: GoogleMap) {
        if (!googleMap.isMyLocationEnabled &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }
        //load location
        viewModel.getUserLocation()
    }
}