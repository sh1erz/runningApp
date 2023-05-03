package com.karyna.feature.personal.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.karyna.feature.core.utils.base.BaseFragment
import com.karyna.feature.personal.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding =
        { layoutInflater, viewGroup, b -> FragmentSettingsBinding.inflate(layoutInflater, viewGroup, b) }
    override val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadSettingsInfo()
        setupListeners()
        viewModel.initWeight.observe(viewLifecycleOwner) {
            it?.let { binding.etWeightSetting.setText(it.toString()) }
        }
    }

    private fun setupListeners() = with(binding) {
        etWeightSetting.addTextChangedListener(afterTextChanged = {
            viewModel.setWeight(it?.toString())
        })
        ivBack.setOnClickListener { viewModel.navigateBack() }
        btnSave.setOnClickListener { viewModel.saveWeight() }
        btnSignOut.setOnClickListener { viewModel.signOut() }
    }
}