package com.jorge.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.jorge.bookpedia.book.data.mappers.toBook
import com.jorge.bookpedia.book.data.mappers.toBookEntity
import com.jorge.bookpedia.book.data.source.local.dao.FavoriteBookDao
import com.jorge.bookpedia.book.data.source.local.entity.BookEntity
import com.jorge.bookpedia.book.data.source.remote.datasource.RemoteBookDataSource
import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.core.domain.DataError
import com.jorge.bookpedia.core.domain.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BookRepositoryImplTest {

    private val remoteDataSource = mockk<RemoteBookDataSource>()
    private val favoriteBookDao = mockk<FavoriteBookDao>()
    private val bookRepository = BookRepositoryImpl(remoteDataSource, favoriteBookDao)

    @Test
    fun `given successful response When searchBooks is called Then returns list of books`() =
        runTest {
            val query = "kotlin"
            val expectedBooks = listOf(getBook())
            coEvery { remoteDataSource.searchBooks(query) } returns Result.Success(expectedBooks)

            val result = bookRepository.searchBooks(query)

            assertIs<Result.Success<List<Book>>>(result)
            assertEquals(expectedBooks, result.data)
        }

    @Test
    fun `given failure response When searchBooks is called Then returns error`() = runTest {
        val query = "kotlin"
        val expectedError = DataError.Remote.UNKNOWN

        coEvery { remoteDataSource.searchBooks(query) } returns Result.Error(expectedError)

        val result = bookRepository.searchBooks(query)

        assertIs<Result.Error<DataError.Remote>>(result)
        assertEquals(expectedError, result.error)
    }

    @Test
    fun `given book in local database When getBookDescription is called Then returns book description from local`() =
        runTest {
            val bookId = "1"
            val expectedDescription = "A book about Kotlin"
            val bookEntity = getBookEntity().copy(description = expectedDescription)

            coEvery { favoriteBookDao.getFavoriteBook(bookId) } returns bookEntity

            val result = bookRepository.getBookDescription(bookId)

            assertIs<Result.Success<String?>>(result)
            assertEquals(expectedDescription, result.data)
        }

    @Test
    fun `given book not in local database When getBookDescription is called Then returns book description from remote`() =
        runTest {
            val bookId = "1"
            val expectedDescription = "A book about Kotlin"

            coEvery { favoriteBookDao.getFavoriteBook(bookId) } returns null
            coEvery { remoteDataSource.getBookDetails(bookId) } returns Result.Success(
                expectedDescription
            )

            val result = bookRepository.getBookDescription(bookId)

            assertIs<Result.Success<String?>>(result)
            assertEquals(expectedDescription, result.data)
        }

    @Test
    fun `given favorite books in database When getFavoriteBooks is called Then returns list of favorite books`() =
        runTest {
            val bookEntities = listOf(getBookEntity(), getBookEntity().copy(id = "2"))
            val expectedBooks = bookEntities.map { it.toBook() }

            coEvery { favoriteBookDao.getFavoriteBooks() } returns flowOf(bookEntities)

            val result = bookRepository.getFavoriteBooks().first()

            assertEquals(expectedBooks, result)
        }

    @Test
    fun `given book is favorite When isBookFavorite is called Then returns true`() = runTest {
        val bookId = "1"
        val bookEntities = listOf(getBookEntity().copy(id = bookId))

        coEvery { favoriteBookDao.getFavoriteBooks() } returns flowOf(bookEntities)

        val result = bookRepository.isBookFavorite(bookId).first()

        assertEquals(true, result)
    }

    @Test
    fun `given book is not favorite When isBookFavorite is called Then returns false`() = runTest {
        val bookId = "1"
        val bookEntities = listOf(getBookEntity().copy(id = "2"))

        coEvery { favoriteBookDao.getFavoriteBooks() } returns flowOf(bookEntities)

        val result = bookRepository.isBookFavorite(bookId).first()

        assertEquals(false, result)
    }

    @Test
    fun `given valid book When markAsFavorite is called Then returns success`() = runTest {
        val book = getBook()
        coEvery { favoriteBookDao.upsert(book.toBookEntity()) } returns Unit

        val result = bookRepository.markAsFavorite(book)

        assertIs<Result.Success<Unit>>(result)
    }

    @Test
    fun `given SQLiteException When markAsFavorite is called Then returns disk full error`() =
        runTest {
            val book = getBook()
            coEvery { favoriteBookDao.upsert(book.toBookEntity()) } throws SQLiteException("")

            val result = bookRepository.markAsFavorite(book)

            assertIs<Result.Error<DataError.Local>>(result)
            assertEquals(DataError.Local.DISK_FULL, result.error)
        }

    @Test
    fun `given valid book id When deleteFromFavorites is called Then book is deleted from favorites`() =
        runTest {
            val bookId = "1"
            coEvery { favoriteBookDao.deleteFavoriteBook(bookId) } returns Unit

            bookRepository.deleteFromFavorites(bookId)

            coVerify { favoriteBookDao.deleteFavoriteBook(bookId) }
        }

    private fun getBook() = Book(
        id = "1",
        title = "Kotlin in Action",
        imageUrl = "",
        authors = emptyList(),
        description = null,
        languages = emptyList(),
        firstPublishYear = "0",
        averageRating = 0.0,
        ratingCount = 0,
        numPages = 0,
        numEditions = 0
    )

    private fun getBookEntity() = BookEntity(
        id = "1",
        title = "Kotlin in Action",
        imageUrl = "",
        authors = emptyList(),
        description = null,
        languages = emptyList(),
        firstPublishYear = "0",
        ratingsAverage = 0.0,
        ratingsCount = 0,
        numEditions = 0,
        numPagesMedian = 0
    )
}