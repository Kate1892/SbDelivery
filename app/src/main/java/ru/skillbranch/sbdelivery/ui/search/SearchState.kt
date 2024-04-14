package ru.skillbranch.sbdelivery.ui.search

import ru.skillbranch.sbdelivery.core.adapter.ProductItemState
import ru.skillbranch.sbdelivery.ui.main.MainState

//data class SearchState(val items: List<ProductItemState>)

sealed class SearchState() {
    data object Loading : SearchState()
    data class Error(val errorDescription: String) : SearchState()
    data class Result(
        val items: List<ProductItemState>
    ) : SearchState()
}