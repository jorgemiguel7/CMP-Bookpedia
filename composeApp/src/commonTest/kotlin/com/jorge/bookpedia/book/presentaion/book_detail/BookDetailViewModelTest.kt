package com.jorge.bookpedia.book.presentaion.book_detail

import androidx.lifecycle.SavedStateHandle
import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.book.domain.repository.BookRepository
import com.jorge.bookpedia.book.presentation.book_detail.BookDetailAction
import com.jorge.bookpedia.book.presentation.book_detail.BookDetailViewModel
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

//class BookDetailViewModelTest {
//
//    private val bookRepository = mockk<BookRepository>()
//    private val savedStateHandle = mockk<SavedStateHandle>()
//    private val viewModel = BookDetailViewModel(bookRepository, savedStateHandle)
//
//    @Test
//    fun `given valid action When onAction is called with OnSelectedBookChange Then state is updated`() =
//        runTest {
//            val expectedBook = getBook()
//
//            viewModel.onAction(BookDetailAction.OnSelectedBookChange(expectedBook))
//
//            val result = viewModel.state.first()
//            assertEquals(expectedBook, result.book)
//        }
//
//    private fun getBook() = Book(
//        id = "1",
//        title = "Kotlin in Action",
//        imageUrl = "",
//        authors = emptyList(),
//        description = null,
//        languages = emptyList(),
//        firstPublishYear = "0",
//        averageRating = 0.0,
//        ratingCount = 0,
//        numPages = 0,
//        numEditions = 0
//    )
//}