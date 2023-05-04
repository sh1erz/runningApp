package com.karyna.feature.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.karyna.core.data.RunningRepository
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.StringFormatter
import com.karyna.feature.core.utils.base.BaseViewModel
import com.karyna.feature.core.utils.base.SnackBarInfo
import com.karyna.feature.personal.list.PersonalItem
import com.karyna.feature.personal.list.PersonalItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import com.karyna.feature.core.R as RCore

@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val repository: RunningRepository,
    @Named("userId") private val userId: String
) :
    BaseViewModel() {

    val personalItems: LiveData<List<PersonalItem>> get() = _personalItems
    private val _personalItems = MutableLiveData<List<PersonalItem>>(emptyList())

    var chosenRun: Run? = null
        private set

    fun loadListInfo() {
        viewModelScope.launch {
            val user = repository.getUser(userId).getOrNull() ?: return@launch
            val userItem = PersonalItem(PersonalItemType.USER, user)
            val items = mutableListOf(userItem)
            val runs = repository.getRuns(userId)
            if (runs.isSuccess) {
                items.add(PersonalItem(PersonalItemType.LIST_TITLE, StringFormatter.from(R.string.your_runs)))
                items.addAll(runs.getOrThrow().map { PersonalItem(PersonalItemType.RUN_ITEM, it) })
            }
            _personalItems.value = items
        }
    }

    fun deleteRun() {
        viewModelScope.launch {
            chosenRun?.let {
                val result = repository.deleteRun(it.id)
                if (result.isSuccess) {
                    snackBarMsg.value = SnackBarInfo(
                        message = StringFormatter.from(RCore.string.changes_accepted),
                        isPositive = true
                    )
                    navigateBack()
                } else {
                    snackBarMsg.value = SnackBarInfo(
                        StringFormatter.from(RCore.string.something_went_wrong),
                        isPositive = false,
                        actionTitle = StringFormatter.from(RCore.string.retry),
                        action = { deleteRun() }
                    )
                }
            }
        }
    }

    fun navigateToRunDetails(run: Run) {
        chosenRun = run
        navigate(PersonalFragmentDirections.actionPersonalToRunDetails())
    }

    fun navigateToSettings() {
        navigate(PersonalFragmentDirections.actionPersonalToSettings())
    }

}