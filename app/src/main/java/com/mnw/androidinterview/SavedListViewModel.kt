package com.mnw.androidinterview

import androidx.lifecycle.ViewModel
import com.mnw.androidinterview.model.BookRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SavedListViewModel @Inject constructor(
    repo: BookRepo
): ViewModel() {

    val bookList = repo.saved


}

