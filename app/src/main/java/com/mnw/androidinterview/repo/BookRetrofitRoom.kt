package com.mnw.androidinterview.repo


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mnw.androidinterview.db.BookDao
import com.mnw.androidinterview.db.BookRaw
import com.mnw.androidinterview.model.Book
import com.mnw.androidinterview.model.BookRepo
import com.mnw.androidinterview.net.BookData
import com.mnw.androidinterview.net.BooksApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private fun BookData.toDatabaseEntity(): BookRaw {
    return BookRaw(this.id, this.title, null, null, null, null, null, thumbnail = this.thumbnail)
}

private fun BookRaw.asDomainModel(): Book {
    return Book(this.id, this.title, this.authors, this.publisher, this.rating, this.year, this.description, null)
}

class BookRetrofitRoom @Inject constructor(
    private val booksApi: BooksApi,
    private val bookDao: BookDao,
): BookRepo {

    override val books: LiveData<List<Book>> = Transformations.map(bookDao.getAll()) {
        it.map { raw -> raw.asDomainModel() }.toList()
    }

    override suspend fun refreshAll() {
        withContext(Dispatchers.IO) {
            try {
                val response = booksApi.searchBooks("mongo")

                if (response.isSuccessful) {
                    response.body()?.bookList
                        ?.map { book ->
                            book.toDatabaseEntity()
                        }
                        ?.toList()
                        ?.let {
                            bookDao.insertAll(it)
                        }

                } else {

                    Log.e("ASD", "could not fetch api: ${response.errorBody().toString()}")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()

            }
        }

    }
}
