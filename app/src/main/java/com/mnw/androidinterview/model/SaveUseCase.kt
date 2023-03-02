package com.mnw.androidinterview.model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SaveUseCase constructor(
    private val repo: BookRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default

    ) {
    @Inject
    constructor(
        repo: BookRepo,
    ) : this(repo, Dispatchers.Default)

    suspend operator fun invoke(book: Book) {
        withContext(dispatcher) {

            repo.save(book)
        }

    }

}