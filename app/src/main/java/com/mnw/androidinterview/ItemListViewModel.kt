package com.mnw.androidinterview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnw.androidinterview.model.BookRepo
import com.mnw.androidinterview.model.RefreshUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val refreshUseCase: RefreshUseCase,
    repo: BookRepo
): ViewModel() {

    val bookList = repo.books

    var query: String = ""

    fun refresh(): Boolean {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                refreshUseCase(query)
            }
            return true
        }
        return false
    }

    fun setQueryString(query: String?) {
        if (!query.isNullOrBlank()) {
            this.query = query

            refresh()
        }
    }

}
