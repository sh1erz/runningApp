package com.karyna.feature.core.utils.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow

fun flowRepeatEvery(delay: Long): Flow<Unit> =
    flow {
        while (true) {
            emit(Unit)
            delay(delay)
        }
    }.cancellable()