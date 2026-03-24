package com.example.storeexample.presentation.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeexample.data.local.entity.FavoriteProduct
import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: Int = savedStateHandle["productId"]!!

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = repository.isFavorite(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            try {
                val product = repository.getProduct(productId)
                _uiState.value = ProductDetailUiState.Success(product)
            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun toggleFavorite() {
        val state = _uiState.value as? ProductDetailUiState.Success ?: return
        val p = state.product
        viewModelScope.launch {
            if (isFavorite.value) {
                repository.removeFavorite(FavoriteProduct(p.id, p.title, p.category, p.price, p.rating, p.thumbnail))
            } else {
                repository.addFavorite(FavoriteProduct(p.id, p.title, p.category, p.price, p.rating, p.thumbnail))
            }
        }
    }
}

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}
