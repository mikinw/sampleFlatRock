package com.mnw.androidinterview.model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MoreUseCase constructor(
    private val repo: BookRepo,
    private val networkState: NetworkStateModel,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    @Inject
    constructor(
        repo: BookRepo,
        networkState: NetworkStateModel,
    ) : this(repo, networkState, Dispatchers.Default)


    suspend operator fun invoke(query: String) {
        withContext(dispatcher) {

            try {

                networkState.requestState(NetworkState.REFRESHING)

                repo.loadMore(query)

                networkState.requestState(NetworkState.NO_ACTIVITY)

            } catch (ex: Exception) {
                ex.printStackTrace()
                networkState.requestState(NetworkState.ERROR, ex.localizedMessage)


            }

        }
    }
}