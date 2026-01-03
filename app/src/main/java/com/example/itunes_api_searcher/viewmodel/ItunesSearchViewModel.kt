package com.example.itunes_api_searcher.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunes_api_searcher.data.model.ItunesItemMetaData
import com.example.itunes_api_searcher.data.repository.ItunesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ItunesSearchViewModel(
    private val repository: ItunesRepository = ItunesRepository()
) : ViewModel() {

    companion object {
        private const val TAG = "ItunesSearchVM"
    }

    private val _selectedItem = MutableStateFlow<ItunesItemMetaData?>(null)
    val selectedItem: StateFlow<ItunesItemMetaData?> = _selectedItem

    private val _detailsError = MutableStateFlow<String?>(null)
    val detailsError: StateFlow<String?> = _detailsError

    private val _results = MutableStateFlow<List<ItunesItemMetaData>>(emptyList())
    val results: StateFlow<List<ItunesItemMetaData>> = _results

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun search(term: String, limit: Int) {
        Log.d(TAG, "Search triggered: term=$term limit=$limit")

        if (term.isBlank()) return

        viewModelScope.launch {
            Log.d(TAG, "Starting search coroutine")

            _isLoading.value = true
            _error.value = null

            try {
                val results = repository.search(term, limit)
                Log.d(TAG, "Search success, results=${results}, size: ${results.size}")
                _results.value = results
            } catch (e: Exception) {
                Log.e(TAG, "Search failed", e)
                _error.value = e.message
                _results.value = emptyList()
            } finally {
                _isLoading.value = false
                Log.d(TAG, "Search finished")
            }
        }
    }

    fun selectItem(item: ItunesItemMetaData?) {
        if (item == null) {
            _detailsError.value = "Failed to load item details."
            _selectedItem.value = null
        } else {
            _detailsError.value = null
            _selectedItem.value = item
        }
    }

    fun clearSelectedItem() {
        _selectedItem.value = null
        _detailsError.value = null
    }
}
