package com.example.storeexample.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading
            try {
                val response = repository.getProducts(limit = 20, skip = 0)
                _uiState.value = ProductListUiState.Success(response.products)
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}

sealed class ProductListUiState {
    object Loading : ProductListUiState()
    data class Success(val products: List<Product>) : ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
}
