package com.mnw.androidinterview.repo


import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mnw.androidinterview.db.BookDao
import com.mnw.androidinterview.db.BookRaw
import com.mnw.androidinterview.db.SavedBookDao
import com.mnw.androidinterview.db.SavedBookRaw
import com.mnw.androidinterview.model.Book
import com.mnw.androidinterview.model.BookRepo
import com.mnw.androidinterview.net.BookData
import com.mnw.androidinterview.net.Books
import com.mnw.androidinterview.net.BooksApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

private fun BookData.toDatabaseEntity(): BookRaw {
    return BookRaw(this.id, this.title, this.authors, this.publisher, this.rating, this.year?.let { Integer.parseInt(it) }, this.description, this.thumbnail)
}

private fun BookRaw.asDomainModel(): Book {
    return Book(this.id, this.title, this.authors, this.publisher, this.rating, this.year, this.description, this.thumbnail)
}
private fun SavedBookRaw.asDomainModel(): Book {
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

    override val saved: LiveData<List<Book>> = Transformations.map(savedBookDao.getAll()) {
        it.map { raw -> raw.asDomainModel() }
    }


    override suspend fun save(book: Book) {
        savedBookDao.insert(SavedBookRaw(book))
    }

    override suspend fun unsave(book: Book) {
        savedBookDao.delete(SavedBookRaw(book))
    }

    override suspend fun loadMore(query: String) {
        withContext(dispatcher) {

            val nextPage = (books.value?.size ?: 10) / 10

            val response = booksApi.searchBooks(query, nextPage + 1)

            if (response.isSuccessful) {

                processResponse(response)

            } else {

                Log.e("ASD", "could not fetch api: ${response.errorBody().toString()}")
                throw NetworkErrorException(response.errorBody().toString())


            }

        }
    }

    private fun processResponse(response: Response<Books>) {
        response.body()?.let { body ->

            body.bookList

                ?.map { book ->
                    book.toDatabaseEntity()
                }
                ?.toList()
                ?.let {
                    bookDao.insertAll(it)
                }

        }
    }

    override suspend fun refreshAll(query: String) {
        withContext(dispatcher) {

            val response = booksApi.searchBooks(query)

            if (response.isSuccessful) {

                bookDao.deleteExcept(emptyList())

                processResponse(response)


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

