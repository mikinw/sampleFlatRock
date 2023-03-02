package com.mnw.androidinterview.model

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject


class DetailsUseCase constructor(
    private val repo: BookRepo,
    private val networkState: NetworkStateModel,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    @Inject constructor(
        repo: BookRepo,
        networkState: NetworkStateModel,
    ) : this(repo, networkState, Dispatchers.Default)


    suspend operator fun invoke(id: String, postToUi: WeakReference<MutableLiveData<Book?>>) {
        withContext(dispatcher) {

            val cached = repo.books.value?.find { book -> book.id == id }

            postToUi.get()?.postValue(cached)

            try {

                networkState.requestState(NetworkState.REFRESHING)

                val details = repo.getDetails(id)

                postToUi.get()?.postValue(details)

                networkState.requestState(NetworkState.NO_ACTIVITY)

            } catch (ex: Exception) {
                ex.printStackTrace()
                networkState.requestState(NetworkState.ERROR, ex.localizedMessage)


            }

        }
    }

    private fun hasNoDetails(book: Book?): Boolean {
        if (book == null) { return true }
        return book.authors == null && book.publisher == null && book.rating == null && book.year == null && book.description == null
    }
}
