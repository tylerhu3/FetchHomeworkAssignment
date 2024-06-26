package com.tyler.hu.fetchhomework

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnlineDataViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(true)

    // Map of item listId to fetched items with the same listId
    private val _items =
        MutableStateFlow<Map<Int, List<OnlineDataRepository.FetchItem>>>(emptyMap())

    private val _error = MutableStateFlow(false)
    private val _dataRetrieved = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    val fetchedItems = _items.asStateFlow()
    val error = _error.asStateFlow()
    val dataAlreadyRetrieved = _dataRetrieved.asStateFlow()

    suspend fun fetchData() {
        _error.value = false
        _isLoading.value = true
        try {
            _items.value = OnlineDataRepository().getItems()
            _dataRetrieved.value = true
        } catch (e: Exception) {
            _dataRetrieved.value = false
            _error.value = true
        } finally {
            _isLoading.value = false
        }
    }
}