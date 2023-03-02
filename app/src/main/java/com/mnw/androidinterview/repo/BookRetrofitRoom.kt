package com.mnw.androidinterview.repo


import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.mnw.androidinterview.db.BookDao
import com.mnw.androidinterview.db.BookRaw
import com.mnw.androidinterview.db.SavedBookDao
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
    private val savedBookDao: SavedBookDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): BookRepo {

    @Inject constructor(
        booksApi: BooksApi,
        bookDao: BookDao,
        savedBookDao: SavedBookDao,
    ) : this(booksApi, bookDao, savedBookDao, Dispatchers.IO)


    override val books: LiveData<List<Book>> = Transformations.map(bookDao.getAll()) {
        it.map { raw -> raw.asDomainModel() }
    }
    override val saved: LiveData<List<Book>> = Transformations.map(bookDao.getAllSaved()) {
        it.map { raw -> raw.asDomainModel() }
    }



    override suspend fun refreshAll(query: String) {
        withContext(dispatcher) {

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

        }

    }

    override suspend fun getDetails(id: String): Book {

        return withContext(dispatcher) {

            val response = booksApi.getDetails(id)

            if (response.isSuccessful) {
                val details = response.body()
                    ?: throw NetworkErrorException("Can't get detail for $id")

                val bookRaw = details.toDatabaseEntity()

                bookDao.update(bookRaw)

                bookRaw.asDomainModel()

            } else {

                Log.e("ASD", "could not fetch api: ${response.errorBody().toString()}")
                throw NetworkErrorException(response.errorBody().toString())

            }


        }
    }
}
