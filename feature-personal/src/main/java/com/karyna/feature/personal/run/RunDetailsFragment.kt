package com.karyna.feature.personal.run

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.navigation.navGraphViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.base.BaseFragment
import com.karyna.feature.core.utils.utils.extensions.color
import com.karyna.feature.core.utils.utils.extensions.string
import com.karyna.feature.personal.PersonalViewModel
import com.karyna.feature.personal.R
import com.karyna.feature.personal.databinding.FragmentRunDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates
import com.karyna.feature.core.R as RCore
import com.karyna.feature.core.utils.utils.DateUtils as CoreDateUtils

@AndroidEntryPoint
class RunDetailsFragment : BaseFragment<FragmentRunDetailsBinding, PersonalViewModel>(), OnMapReadyCallback {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRunDetailsBinding =
        { layoutInflater, viewGroup, b -> FragmentRunDetailsBinding.inflate(layoutInflater, viewGroup, b) }
    override val viewModel: PersonalViewModel by navGraphViewModels(R.id.personal_nav_graph) { defaultViewModelProviderFactory }
    private var googleMap: GoogleMap by Delegates.notNull()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        setupListeners()
        showRunData(requireNotNull(viewModel.chosenRun))
    }

    override fun onMapReady(gm: GoogleMap) {
        googleMap = gm
        val coordinates = requireNotNull(viewModel.chosenRun).coordinates.map { LatLng(it.lat, it.lng) }
        drawRoute(coordinates)
    }

    private fun setupListeners() {
        with(binding) {
            ivBack.setOnClickListener { viewModel.navigateBack() }
            ivDeleteRun.setOnClickListener { viewModel.deleteRun() }
        }
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions().apply {
            color(binding.color(RCore.color.primary700))
            width(POLYLINE_STROKE_WIDTH_PX)
            startCap(ButtCap())
            endCap(RoundCap())
        }
        googleMap.clear()

        polylineOptions.points.addAll(locations)

        val polyline = googleMap.addPolyline(polylineOptions)
        moveToBounds(polyline)
    }

    private fun moveToBounds(polyline: Polyline) {
        val builder = LatLngBounds.Builder()
        for (latLng in polyline.points) {
            builder.include(latLng)
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), POLYLINE_PADDING))
        if (polyline.points.any { !googleMap.projection.visibleRegion.latLngBounds.contains(it) }) {
            googleMap.moveCamera(CameraUpdateFactory.zoomOut())
        }
    }

    private fun showRunData(run: Run) = with(binding) {
        tvLocation.text = getString(R.string.location_formatted, run.location.city, run.location.country)
        tvDistance.text = string(R.string.distance_formatted, string(RCore.string.n_meters, run.distanceMeters))
        tvDuration.text = string(R.string.duration_formatted, DateUtils.formatElapsedTime(run.durationS))
        tvPace.text = string(R.string.pace_formatted, run.paceMetersInS)
        tvDate.text = CoreDateUtils.formatIsoDate(run.date)
        if (run.calories != null) {
            tvCalories.text = string(R.string.calories_formatted, run.calories!!)
        } else {
            tvCalories.isInvisible = true
        }
    }

    private companion object {
        const val POLYLINE_PADDING = 0
        const val POLYLINE_STROKE_WIDTH_PX = 12f
    }
}