package com.karyna.feature.core.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.karyna.feature.core.R
import com.karyna.feature.core.utils.navigation.NavigationCommand
import com.karyna.feature.core.utils.utils.extensions.color

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    private var _binding: ViewBinding? = null

    protected abstract val viewModel: VM

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = requireNotNull(_binding as VB)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewModel.navigation.observe(viewLifecycleOwner) {
            if (it != null) {
                handleNavigation(it)
            }
        }
        viewModel.snackBarMsg.observe(viewLifecycleOwner) { info ->
            Snackbar.make(requireContext(), binding.root, info.message, Snackbar.LENGTH_LONG).apply {
                view.setBackgroundColor(binding.color(if (info.isPositive) R.color.primary700 else R.color.negative300))
                val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView.setTextColor(binding.color(R.color.base100))

                if (info.action != null) {
                    setAction(info.actionTitle) { info.action.invoke() }
                }
            }.show()
        }
    }

    private fun handleNavigation(navCommand: NavigationCommand) {
        when (navCommand) {
            is NavigationCommand.ToDirection -> findNavController().navigate(navCommand.directions)
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

}