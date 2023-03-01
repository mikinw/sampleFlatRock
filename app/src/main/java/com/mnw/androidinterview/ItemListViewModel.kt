package com.mnw.androidinterview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnw.androidinterview.model.Book
import com.mnw.androidinterview.net.BooksApi
import com.mnw.androidinterview.net.EndpointClient
import com.mnw.androidinterview.placeholder.PlaceholderContent
import kotlinx.coroutines.launch


class ItemListViewModel: ViewModel() {

    private val _bookList = MutableLiveData<List<Book>>()
    val bookList: LiveData<List<Book>>
        get() = _bookList


    fun getBookList() {
        val retrofit = EndpointClient.getInstance()
        val apiInterface = retrofit.create(BooksApi::class.java)
        viewModelScope.launch {
            try {
                val response = apiInterface.searchBooks("mongo")
                if (response.isSuccessful) {

                    PlaceholderContent.clear()
                    _bookList.value = response.body()
                        ?.bookList
                        ?.map { book ->
                            Book(book.thumbnail.substring(0,4), book.title)
                        }
                        ?.toList()

                } else {
                    Log.e("Error", response.errorBody().toString())
                }
            } catch (Ex:Exception){
                Log.e("Error", Ex.localizedMessage)
            }
        }
    }
}
