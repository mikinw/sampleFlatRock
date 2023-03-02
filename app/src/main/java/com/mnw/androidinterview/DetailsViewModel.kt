package com.mnw.androidinterview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnw.androidinterview.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getDetailsUseCase: DetailsUseCase,
    private val saveUseCase: SaveUseCase,
    private val unsaveUseCase: UnsaveUseCase,
    repo: BookRepo
): ViewModel() {

    private val _details = MutableLiveData<Book?>(null)
    val item : LiveData<Book?> = _details

    val savedList = repo.saved


    fun setItemId(id: String) {
        viewModelScope.launch {
            getDetailsUseCase(id, WeakReference(_details))
        }
    }

    fun save() {
        viewModelScope.launch {
            item.value?.let {
                saveUseCase(it)
            }
        }
    }

    fun unsave() {
        viewModelScope.launch {
            item.value?.let {
                unsaveUseCase(it)
            }
        }
    }

}