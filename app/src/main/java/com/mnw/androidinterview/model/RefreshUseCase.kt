package com.mnw.androidinterview.model


import android.util.Log
import kotlinx.coroutines.*
import javax.inject.Inject


class RefreshUseCase constructor(
    private val repo: BookRepo,
    private val networkState: NetworkStateModel,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    @Inject constructor(
        repo: BookRepo,
        networkState: NetworkStateModel,
    ) : this(repo, networkState, Dispatchers.Default)


    suspend operator fun invoke() {
        withContext(dispatcher) {

            try {

                networkState.requestState(NetworkState.REFRESHING)

                val refreshListJob = launch {
                    repo.refreshAll()
                }

                refreshListJob.join()


                networkState.requestState(NetworkState.NO_ACTIVITY)

            } catch (ex: Exception) {
                ex.printStackTrace()
                networkState.requestState(NetworkState.ERROR, ex.localizedMessage)


            }

        }
    }
}