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

    init {
        getBookList()
    }

    val bookList = repo.books

    fun getBookList() {
        viewModelScope.launch {
            refreshUseCase()
        }
    }
}
