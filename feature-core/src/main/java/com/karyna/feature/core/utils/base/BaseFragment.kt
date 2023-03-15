package com.karyna.feature.core.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.karyna.feature.core.utils.navigation.NavigationCommand

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    private var _binding: ViewBinding? = null

    protected abstract val viewModel: VM

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = requireNotNull(_binding as VB)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeNavigation() {
        viewModel.navigation.observe(viewLifecycleOwner) {
            if (it != null) {
                handleNavigation(it)
            }
        }
    }

    private fun handleNavigation(navCommand: NavigationCommand) {
        when (navCommand) {
            is NavigationCommand.ToDirection -> findNavController().navigate(navCommand.directions)
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

}