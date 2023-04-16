package com.karyna.feature.personal.run

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.karyna.feature.core.utils.base.BaseFragment
import com.karyna.feature.personal.PersonalViewModel
import com.karyna.feature.personal.databinding.FragmentRunDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunDetailsFragment : BaseFragment<FragmentRunDetailsBinding, PersonalViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRunDetailsBinding =
        { layoutInflater, viewGroup, b -> FragmentRunDetailsBinding.inflate(layoutInflater, viewGroup, b) }
    override val viewModel: PersonalViewModel by viewModels()


}