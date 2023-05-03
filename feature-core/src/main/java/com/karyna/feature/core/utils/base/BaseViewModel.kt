package com.karyna.feature.core.utils.base

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.karyna.feature.core.utils.SingleLiveEvent
import com.karyna.feature.core.utils.navigation.NavigationCommand

abstract class BaseViewModel : ViewModel() {

    val navigation: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val snackBarMsg: SingleLiveEvent<SnackBarInfo> = SingleLiveEvent()

    fun navigate(navDirections: NavDirections) {
        navigation.value = NavigationCommand.ToDirection(navDirections)
    }

    fun navigateBack() {
        navigation.value = NavigationCommand.Back
    }

}