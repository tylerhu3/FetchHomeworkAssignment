package com.tyler.hu.fetchhomework

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnlineDataViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    // Map of item listId to fetched items with the same listId
    private val _items =
        MutableStateFlow<Map<Int, List<OnlineDataRepository.FetchItem>>>(emptyMap())
    private val _error = MutableStateFlow<String?>(null)
    val isLoading = _isLoading.asStateFlow()
    val fetchedItems = _items.asStateFlow()
    val error = _error.asStateFlow()

    suspend fun fetchData() {
        _error.value = null
        _isLoading.value = true
        try {
            _items.value = OnlineDataRepository().getItems()
        } catch (e: Exception) {
            _error.value = "Error: ${e::class.qualifiedName} ${e.message} \n\n Pull down to Retry"
        } finally {
            _isLoading.value = false
        }
    }
}