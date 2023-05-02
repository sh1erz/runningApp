package com.karyna.feature.personal.settings

import androidx.lifecycle.viewModelScope
import com.karyna.core.data.RunningRepository
import com.karyna.feature.core.utils.SingleLiveEvent
import com.karyna.feature.core.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: RunningRepository,
    @Named("userId") private val userId: String
) : BaseViewModel() {
    val initWeight = SingleLiveEvent<Float?>()
    private var inputWeight: String? = null

    fun loadSettingsInfo() {
        viewModelScope.launch {
            val userResponse = repository.getUser(userId)
            if (userResponse.isSuccess) {
                initWeight.value = userResponse.getOrThrow().weight
            }
        }
    }

    fun setWeight(input: String?) {
        inputWeight = input
    }

    fun saveWeight() {
        viewModelScope.launch { repository.setWeight(userId, inputWeight?.toFloatOrNull()) }
    }
}