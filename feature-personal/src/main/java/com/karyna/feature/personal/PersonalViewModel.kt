package com.karyna.feature.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.karyna.core.data.RunningRepository
import com.karyna.core.domain.User
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.StringFormatter
import com.karyna.feature.core.utils.base.BaseViewModel
import com.karyna.feature.personal.list.PersonalItem
import com.karyna.feature.personal.list.PersonalItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(private val repository: RunningRepository, private val user: User) :
    BaseViewModel() {

    val personalItems: LiveData<List<PersonalItem>> get() = _personalItems
    private val _personalItems = MutableLiveData<List<PersonalItem>>(emptyList())

    var chosenRun: Run? = null
        private set

    fun loadListInfo() {
        viewModelScope.launch {
            val userItem = PersonalItem(PersonalItemType.USER, user)
            val items = mutableListOf(userItem)
            val runs = repository.getRuns(user.id)
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