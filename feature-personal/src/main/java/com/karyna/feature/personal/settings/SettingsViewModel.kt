package com.karyna.feature.personal.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.karyna.core.data.RunningRepository
import com.karyna.feature.core.utils.SingleLiveEvent
import com.karyna.feature.core.utils.StringFormatter
import com.karyna.feature.core.utils.base.BaseViewModel
import com.karyna.feature.core.utils.base.SnackBarInfo
import com.karyna.feature.personal.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import com.karyna.feature.core.R as RCore

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: RunningRepository,
    @Named("userId") private val userId: String
) : BaseViewModel() {
    val initWeight = SingleLiveEvent<Float?>()
    val isSaveEnabled: LiveData<Boolean> get() = _isSaveEnabled
    private val _isSaveEnabled = MutableLiveData<Boolean>()
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
        _isSaveEnabled.value = initWeight.value != input?.toFloatOrNull()
    }

    fun saveWeight() {
        viewModelScope.launch {
            try {
                val weight = inputWeight?.run { if (isEmpty()) null else trim().toFloat() }
                val result = repository.setWeight(userId, weight)
                if (result.isFailure) {
                    snackBarMsg.value = SnackBarInfo(
                        StringFormatter.from(RCore.string.something_went_wrong),
                        isPositive = false,
                        actionTitle = StringFormatter.from(RCore.string.retry),
                        action = { setWeight(inputWeight) })
                } else {
                    snackBarMsg.value = SnackBarInfo(
                        StringFormatter.from(RCore.string.changes_accepted),
                        isPositive = true
                    )
                }
            } catch (ex: NumberFormatException) {
                snackBarMsg.value = SnackBarInfo(StringFormatter.from(R.string.incorrect_value), isPositive = false)
            }
        }
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}