package com.karyna.feature.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.karyna.core.data.RunningRepository
import com.karyna.core.domain.User
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

    fun loadListInfo() {
        viewModelScope.launch {
            val runs = repository.getRuns(user.id)

            val userItem = PersonalItem(PersonalItemType.USER, user)
            _personalItems.value = listOf(userItem)
        }
    }

}