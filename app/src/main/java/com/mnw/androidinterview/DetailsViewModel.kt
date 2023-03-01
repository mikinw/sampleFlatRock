package com.mnw.androidinterview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mnw.androidinterview.model.Book
import com.mnw.androidinterview.model.BookRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: BookRepo
): ViewModel() {

    private val _details = MutableLiveData<Book?>(null)
    val item : LiveData<Book?> = _details

    fun setItemId(id: String) {
        val foundBook = repo.books.value?.find { book -> book.id == id }
        _details.postValue(foundBook)
    }

}