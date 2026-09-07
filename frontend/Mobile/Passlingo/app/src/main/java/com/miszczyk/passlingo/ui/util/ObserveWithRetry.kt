package com.miszczyk.passlingo.ui.util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.retry

fun <T> Flow<T>.observeWithRetry(
    scope: CoroutineScope,
    retries: Long = 3,
    delayMillis: Long = 1000,
    onError: (Throwable) -> Unit,
    onEachAction: (T) -> Unit
) = this.onEach { value ->
    onEachAction(value)
}.retry(retries = retries) { _ ->
    delay(delayMillis)
    true
}.catch{ e ->
    onError(e)
}.launchIn(scope)