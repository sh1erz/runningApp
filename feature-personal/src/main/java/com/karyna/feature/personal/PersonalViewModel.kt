package com.karyna.feature.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.karyna.core.data.RunningRepository
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.StringFormatter
import com.karyna.feature.core.utils.base.BaseViewModel
import com.karyna.feature.personal.list.PersonalItem
import com.karyna.feature.personal.list.PersonalItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

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
            val user = (repository.getUser(userId) as? com.karyna.core.data.Result.Success)?.value ?: return@launch
            val userItem = PersonalItem(PersonalItemType.USER, user)
            val items = mutableListOf(userItem)
            val runs = repository.getRuns(userId)
            if (runs is com.karyna.core.data.Result.Success) {
                items.add(PersonalItem(PersonalItemType.LIST_TITLE, StringFormatter.from(R.string.your_runs)))
                items.addAll(runs.value.map { PersonalItem(PersonalItemType.RUN_ITEM, it) })
            }
            _personalItems.value = items
        }
    }

    fun navigateToRunDetails(run: Run) {
        chosenRun = run
        navigate(PersonalFragmentDirections.actionPersonalToRunDetails())
    }

}