package com.mnw.androidinterview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnw.androidinterview.model.BookRepo
import com.mnw.androidinterview.model.RefreshUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SavedListViewModel @Inject constructor(
    repo: BookRepo
): ViewModel() {

    val bookList = repo.saved


}

