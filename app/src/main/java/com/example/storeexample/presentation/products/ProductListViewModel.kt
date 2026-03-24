package com.example.storeexample.presentation.products

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    val favoriteIds: StateFlow<Set<Int>> = repository.getFavorites()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private var skip = 0
    private var total = Int.MAX_VALUE
    private var isLoadingMore = false

    init {
        loadProducts()
    }

    fun loadProducts() {
        skip = 0
        total = Int.MAX_VALUE
        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading
            try {
                val response = repository.getProducts(limit = PAGE_SIZE, skip = 0)
                skip = PAGE_SIZE
                total = response.total
                _uiState.value = ProductListUiState.Success(
                    products = response.products,
                    hasMore = skip < total
                )
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun loadNextPage() {
        if (isLoadingMore || skip >= total) return
        val currentState = _uiState.value as? ProductListUiState.Success ?: return
        isLoadingMore = true
        _uiState.value = currentState.copy(isLoadingMore = true)
        viewModelScope.launch {
            try {
                val response = repository.getProducts(limit = PAGE_SIZE, skip = skip)
                skip += PAGE_SIZE
                isLoadingMore = false
                _uiState.value = ProductListUiState.Success(
                    products = currentState.products + response.products,
                    hasMore = skip < total
                )
            } catch (e: Exception) {
                isLoadingMore = false
                _uiState.value = currentState.copy(isLoadingMore = false)
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            val fav = FavoriteProduct(
                product.id, product.title, product.category,
                product.price, product.rating, product.thumbnail
            )
            if (product.id in favoriteIds.value) {
                repository.removeFavorite(fav)
            } else {
                repository.addFavorite(fav)
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}

sealed class ProductListUiState {
    object Loading : ProductListUiState()
    data class Success(
        val products: List<Product>,
        val isLoadingMore: Boolean = false,
        val hasMore: Boolean = true
    ) : ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
}
