package com.tyler.hu.fetchhomework

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class OnlineDataViewModel: ViewModel() {

    private val _isLoading = MutableStateFlow(true)

    suspend fun fetchData(){
        OnlineDataRepository().getItems()
    }
}