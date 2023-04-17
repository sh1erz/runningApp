package com.karyna.feature.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.karyna.core.domain.run.OrderingMode
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.base.BaseFragment
import com.karyna.feature.social.databinding.FragmentSocialBinding
import com.karyna.feature.social.list.FilteringMode
import com.karyna.feature.social.list.RunsAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.karyna.feature.core.R as RCore

@AndroidEntryPoint
class SocialFragment : BaseFragment<FragmentSocialBinding, SocialViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSocialBinding =
        { layoutInflater, viewGroup, b -> FragmentSocialBinding.inflate(layoutInflater, viewGroup, b) }
    override val viewModel: SocialViewModel by viewModels()
    private val adapter by lazy { RunsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTimeFilter()
        setListeners()
        viewModel.loadTopRuns()

        viewModel.topRuns.observe(viewLifecycleOwner, ::populateList)
        viewModel.orderingMode.observe(viewLifecycleOwner, ::setOrderingButtonsState)
    }

    private fun populateList(items: List<Run>) {
        binding.rvPersonal.adapter = adapter.apply { submitList(items) }
    }

    private fun setListeners() {
        with(binding) {
            btnDistance.setOnClickListener { viewModel.updateOrderingMode(OrderingMode.BY_DISTANCE) }
            btnDuration.setOnClickListener { viewModel.updateOrderingMode(OrderingMode.BY_DURATION) }

            spinnerTimeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val filteringMode = FilteringMode.values().getOrNull(position) ?: FilteringMode.TODAY
                    viewModel.updateFilteringMode(filteringMode)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    private fun setOrderingButtonsState(orderingMode: OrderingMode) {
        val distanceSelected = when (orderingMode) {
            OrderingMode.BY_DISTANCE -> true
            OrderingMode.BY_DURATION -> false
        }
        toggleButtons(distanceSelected)
    }

    private fun toggleButtons(distanceSelected: Boolean) {
        with(binding) {
            val selectedColor = root.context.getColor(RCore.color.primary500)
            val selectedTextColor = root.context.getColor(RCore.color.base100)
            val unselectedColor = root.context.getColor(android.R.color.transparent)
            val unselectedTextColor = root.context.getColor(RCore.color.primary500)
            if (distanceSelected) {
                btnDistance.setBackgroundColor(selectedColor)
                btnDistance.setTextColor(selectedTextColor)
                btnDuration.setBackgroundColor(unselectedColor)
                btnDuration.setTextColor(unselectedTextColor)
            } else {
                btnDistance.setBackgroundColor(unselectedColor)
                btnDistance.setTextColor(unselectedTextColor)
                btnDuration.setBackgroundColor(selectedColor)
                btnDuration.setTextColor(selectedTextColor)
            }
        }
    }

    private fun initTimeFilter() {
        with(binding) {
            val items = FilteringMode.values().map { root.context.getString(it.titleRes) }
            val adapter = ArrayAdapter(root.context, android.R.layout.simple_spinner_item, items)
            spinnerTimeFilter.adapter = adapter

//            this,
//            R.array.planets_array,
//            android.R.layout.simple_spinner_item
//            ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
        }
    }

}