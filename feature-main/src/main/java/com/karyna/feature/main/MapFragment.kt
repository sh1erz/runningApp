package com.karyna.feature.main

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.karyna.feature.main.databinding.FragmentMapBinding
import com.karyna.feature.main.map.PermissionsManager
import com.karyna.feature.main.map.RunInfo
import com.karyna.feature.main.services.RunningForegroundService
import kotlin.properties.Delegates

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()
    private var googleMap: GoogleMap by Delegates.notNull()
    private var permissionsManager by Delegates.notNull<PermissionsManager>()

    private var runningService: RunningForegroundService? = null
    private var isBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            (service as? RunningForegroundService.RunningBinder)?.let {
                runningService = it.getService()
                runningService?.runInfo?.observe(viewLifecycleOwner, ::updateRunInfo)
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            runningService = null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = requireActivity() as AppCompatActivity
        permissionsManager = PermissionsManager(activity) { checkPermission(googleMap) }
        viewModel.init(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewLifecycleOwner.lifecycle.addObserver(viewModel)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        setupMap()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        permissionsManager.requestUserLocation()
        with(viewModel) {
            currentLocation.observe(viewLifecycleOwner) {
                if (it != null && it != googleMap.cameraPosition.target) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, MAP_ZOOM))
                }
            }
        }
    }

    private fun setClickListeners() {
        with(binding) {
            btnStart.setOnClickListener { toggleRunButton() }
        }
    }

    private fun toggleRunButton() {
        val btnRes: Int
        val startRes = R.string.start
        if (binding.btnStart.text == getString(startRes)) {
            startService()
            viewModel.stopTracking()
            btnRes = R.string.finish_run
        } else {
            btnRes = R.string.start
            stopService()
        }
        binding.btnStart.text = getString(btnRes)
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun startService() {
        val intent = Intent(requireContext(), RunningForegroundService::class.java).also { intent ->
            isBound = requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        requireActivity().startForegroundService(intent)
    }

    private fun stopService() {
        if (isBound) {
            requireContext().unbindService(connection)
            isBound = false
        }
        runningService?.finishRun()
        runningService?.runInfo?.removeObservers(viewLifecycleOwner)
        googleMap.clear()
        runningService = null
        viewModel.trackCurrentLocation()
    }

    private fun updateRunInfo(runInfo: RunInfo) {
        with(binding) {
            tvDistance.text = runInfo.formattedDistance
            tvPace.text = runInfo.formattedPace
            drawRoute(runInfo.userPath)
        }
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions()
        //todo: map: too much overdraws

        googleMap.clear()

        val points = polylineOptions.points
        points.addAll(locations)

        googleMap.addPolyline(polylineOptions)
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
        viewModel.trackCurrentLocation()
    }

    private companion object {
        const val MAP_ZOOM = 14f
    }
}