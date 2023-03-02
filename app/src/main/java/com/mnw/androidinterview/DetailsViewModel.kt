package com.mnw.androidinterview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnw.androidinterview.model.Book
import com.mnw.androidinterview.model.DetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getDetailsUseCase: DetailsUseCase,
): ViewModel() {

    private val _details = MutableLiveData<Book?>(null)
    val item : LiveData<Book?> = _details

    fun setItemId(id: String) {

        viewModelScope.launch {
            getDetailsUseCase(id, WeakReference(_details))
        }
    }

}