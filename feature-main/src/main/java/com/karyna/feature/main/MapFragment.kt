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
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.karyna.feature.core.utils.base.BaseFragment
import com.karyna.feature.core.utils.utils.extensions.color
import com.karyna.feature.main.databinding.FragmentMapBinding
import com.karyna.feature.main.map.PermissionsManager
import com.karyna.feature.main.map.RunUiInfo
import com.karyna.feature.main.services.RunningForegroundService
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import com.karyna.feature.core.R as RCore

class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(), OnMapReadyCallback {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMapBinding =
        { layoutInflater, viewGroup, b -> FragmentMapBinding.inflate(layoutInflater, viewGroup, b) }
    override val viewModel: MapViewModel by viewModels()

    private var googleMap: GoogleMap by Delegates.notNull()
    private var permissionsManager by Delegates.notNull<PermissionsManager>()

    private var runningService: RunningForegroundService? = null
    private var serviceListenerJob: Job? = null
    private var isBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            (service as? RunningForegroundService.RunningBinder)?.let {
                runningService = it.getService()
                serviceListenerJob = viewLifecycleOwner.lifecycleScope.launch {
                    ensureActive()
                    repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        runningService?.uiState?.collect(::updateRunInfo)
                    }
                }
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            runningService = null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = requireActivity() as AppCompatActivity
        permissionsManager = PermissionsManager(this) { checkPermission(googleMap) }
        viewModel.init(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewLifecycleOwner.lifecycle.addObserver(viewModel)
        return super.onCreateView(inflater, container, savedInstanceState)
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
        googleMap.setMinZoomPreference(MIN_MAP_ZOOM)
        permissionsManager.requestUserLocation()
        with(viewModel) {
            currentLocation.observe(viewLifecycleOwner) {
                if (it != null && it != googleMap.cameraPosition.target) {
                    if (googleMap.cameraPosition.zoom != MIN_MAP_ZOOM) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(it))
                    } else {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, MAP_ZOOM))
                    }
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
            startRun()
            btnRes = R.string.finish_run
        } else {
            btnRes = R.string.start
            stopService()
            binding.cvInfo.isVisible = false
        }
        binding.btnStart.text = getString(btnRes)
    }

    private fun startRun() {
        startService()
        viewModel.stopTracking()
        binding.cvInfo.isVisible = true
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
        serviceListenerJob?.cancel()
        googleMap.clear()
        runningService = null
        viewModel.trackCurrentLocation()
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions().apply {
            color(binding.color(RCore.color.primary700))
            width(POLYLINE_STROKE_WIDTH_PX)
            startCap(RoundCap())
            endCap(RoundCap())
        }
        //todo: map: handle overdraws

        googleMap.clear()

        polylineOptions.points.addAll(locations)
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

    private fun updateRunInfo(runUiInfo: RunUiInfo) {
        with(binding) {
            tvDistanceValue.text = runUiInfo.formattedDistance
            tvPaceValue.text = runUiInfo.formattedPace
            tvDurationValue.text = runUiInfo.duration
            setCalories(runUiInfo.caloriesBurned)
            drawRoute(runUiInfo.userPath)
        }
    }

    private fun setCalories(calories: String?) = with(binding) {
        val show = calories != null
        tvCalories.isVisible = show
        tvCaloriesValue.isVisible = show
        if (show) {
            tvCaloriesValue.text = calories
        }
    }

    private companion object {
        const val MAP_ZOOM = 18f
        const val MIN_MAP_ZOOM = 16f
        const val POLYLINE_STROKE_WIDTH_PX = 14f
    }
}