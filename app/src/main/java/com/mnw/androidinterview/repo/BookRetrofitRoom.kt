package com.mnw.androidinterview.repo


import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mnw.androidinterview.db.BookDao
import com.mnw.androidinterview.db.BookRaw
import com.mnw.androidinterview.model.Book
import com.mnw.androidinterview.model.BookRepo
import com.mnw.androidinterview.net.BookData
import com.mnw.androidinterview.net.BooksApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private fun BookData.toDatabaseEntity(): BookRaw {
    return BookRaw(this.id, this.title, this.authors, this.publisher, this.rating, this.year?.let { Integer.parseInt(it) }, this.description, this.thumbnail)
}

private fun BookRaw.asDomainModel(): Book {
    return Book(this.id, this.title, this.authors, this.publisher, this.rating, this.year, this.description, this.thumbnail)
}

@Singleton
class BookRetrofitRoom constructor(
    private val booksApi: BooksApi,
    private val bookDao: BookDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): BookRepo {

    @Inject constructor(
        booksApi: BooksApi,
        bookDao: BookDao,
    ) : this(booksApi, bookDao, Dispatchers.IO)


    override val books: LiveData<List<Book>> = Transformations.map(bookDao.getAll()) {
        it.map { raw -> raw.asDomainModel() }.toList()
    }

    override suspend fun refreshAll(query: String) {
        withContext(dispatcher) {

            Log.i("ASD", "refreshAll start")


            val response = booksApi.searchBooks(query)

            if (response.isSuccessful) {

                response.body()?.let { body ->
                    val freshIds = ArrayList<String>()

                    body.bookList

                        ?.map { book ->
                            freshIds.add(book.id)
                            book.toDatabaseEntity()
                        }
                        ?.toList()
                        ?.let {
                            bookDao.insertAll(it)
                        }

                    bookDao.deleteExcept(freshIds)
                }


            } else {

                Log.e("ASD", "could not fetch api: ${response.errorBody().toString()}")
                throw NetworkErrorException(response.errorBody().toString())


            }

            Log.i("ASD", "refreshAll end")
        }

    }

    override suspend fun getDetails(id: String) {

        withContext(dispatcher) {

            val response = booksApi.getDetails(id)

            if (response.isSuccessful) {
                val details = response.body()
                    ?: throw NetworkErrorException("Can't get detail for $id")

                val bookRaw = details.toDatabaseEntity()

                bookDao.insert(bookRaw)

            } else {

                Log.e("ASD", "could not fetch api: ${response.errorBody().toString()}")
                throw NetworkErrorException(response.errorBody().toString())

            }


        }
    }
}
