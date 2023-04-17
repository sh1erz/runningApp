package com.karyna.feature.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.base.BaseFragment
import com.karyna.feature.social.databinding.FragmentSocialBinding
import com.karyna.feature.social.list.RunsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFragment : BaseFragment<FragmentSocialBinding, SocialViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSocialBinding =
        { layoutInflater, viewGroup, b -> FragmentSocialBinding.inflate(layoutInflater, viewGroup, b) }
    override val viewModel: SocialViewModel by viewModels()
    private val adapter by lazy { RunsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadTopRuns()
        viewModel.topRuns.observe(viewLifecycleOwner, ::populateList)
    }

    private fun populateList(items: List<Run>) {
        binding.rvPersonal.adapter = adapter.apply { submitList(items) }
    }

}