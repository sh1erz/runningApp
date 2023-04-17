package com.karyna.feature.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.karyna.core.data.RunningRepository
import com.karyna.core.domain.run.OrderingMode
import com.karyna.core.domain.run.Run
import com.karyna.feature.core.utils.base.BaseViewModel
import com.karyna.feature.core.utils.utils.DateUtils.toIsoDate
import com.karyna.feature.social.list.FilteringMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(private val repository: RunningRepository) : BaseViewModel() {

    val orderingMode: LiveData<OrderingMode> get() = _orderingMode
    val topRuns: LiveData<List<Run>> get() = _topRuns
    private var filteringMode: FilteringMode = FilteringMode.TODAY

    private val _orderingMode: MutableLiveData<OrderingMode> = MutableLiveData(OrderingMode.BY_DISTANCE)
    private val _topRuns: MutableLiveData<List<Run>> = MutableLiveData()

    fun loadTopRuns() {
        loadTopRuns(requireNotNull(_orderingMode.value), filteringMode)
    }

    fun updateOrderingMode(orderingMode: OrderingMode) {
        if (_orderingMode.value != orderingMode) {
            _orderingMode.value = orderingMode
            loadTopRuns(orderingMode, filteringMode)
        }
    }

    fun updateFilteringMode(filteringMode: FilteringMode) {
        if (this.filteringMode != filteringMode) {
            this.filteringMode = filteringMode
            loadTopRuns(requireNotNull(_orderingMode.value), filteringMode)
        }
    }

    private fun loadTopRuns(orderingMode: OrderingMode, filteringMode: FilteringMode) {
        val dateRange = calculateDateRange(filteringMode)

        viewModelScope.launch {
            val topRuns = repository.getTopRuns(
                amount = TOP_AMOUNT,
                ordering = orderingMode,
                isoDateFrom = dateRange.first.toIsoDate(),
                isoDateToExcl = dateRange.second.toIsoDate()
            )
            if (topRuns is com.karyna.core.data.Result.Success) {
                _topRuns.postValue(requireNotNull(topRuns.value))
            }
        }
    }

    private fun calculateDateRange(filteringMode: FilteringMode): Pair<OffsetDateTime, OffsetDateTime> {
        val today = OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)

        return when (filteringMode) {
            FilteringMode.TODAY -> today to today.plusDays(1)
            FilteringMode.MONTH -> today.with(TemporalAdjusters.firstDayOfMonth()) to today.with(TemporalAdjusters.firstDayOfNextMonth())
            FilteringMode.YEAR -> today.with(TemporalAdjusters.firstDayOfYear()) to today.with(TemporalAdjusters.firstDayOfNextYear())
        }
    }

    private companion object {
        const val TOP_AMOUNT = 10
    }
}