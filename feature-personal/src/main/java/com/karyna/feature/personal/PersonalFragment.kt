package com.karyna.feature.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.karyna.feature.core.utils.base.BaseFragment
import com.karyna.feature.personal.databinding.FragmentPersonalBinding
import com.karyna.feature.personal.list.PersonalAdapter
import com.karyna.feature.personal.list.PersonalItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalFragment : BaseFragment<FragmentPersonalBinding, PersonalViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPersonalBinding =
        { layoutInflater, viewGroup, b -> FragmentPersonalBinding.inflate(layoutInflater, viewGroup, b) }
    override val viewModel: PersonalViewModel by navGraphViewModels(R.id.personal_nav_graph) { defaultViewModelProviderFactory }
    private val adapter by lazy {
        PersonalAdapter(
            onSettingsClick = viewModel::navigateToSettings,
            onRunClick = viewModel::navigateToRunDetails
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadListInfo()
        viewModel.personalItems.observe(viewLifecycleOwner, ::populateList)
    }

    private fun populateList(items: List<PersonalItem>) {
        binding.rvPersonal.adapter = adapter.apply { submitList(items) }
    }
}