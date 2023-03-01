package com.mnw.androidinterview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnw.androidinterview.model.BookRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val repo: BookRepo
): ViewModel() {

    init {
        getBookList()
    }

    val bookList = repo.books

    fun getBookList() {
        viewModelScope.launch {
            repo.refreshAll()
        }
    }
}
