package com.jorge.bookpedia.book.presentaion.book_list

import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.book.domain.repository.BookRepository
import com.jorge.bookpedia.book.presentation.book_list.BookListAction
import com.jorge.bookpedia.book.presentation.book_list.BookListEffect
import com.jorge.bookpedia.book.presentation.book_list.BookListViewModel
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BookListViewModelTest {

    private val bookRepository: BookRepository = mockk()
    private val bookListViewModel = BookListViewModel(bookRepository)


    @Test
    fun `given valid action When onAction is called with OnBookClick Then NavigateToBookDetail effect is emitted`() =
        runTest {
            val book = getBook()

            val result = async { bookListViewModel.effects.firstOrNull() }
            bookListViewModel.onAction(BookListAction.OnBookClick(book))

            assertEquals(BookListEffect.NavigateToBookDetail(book), result.await())
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
}