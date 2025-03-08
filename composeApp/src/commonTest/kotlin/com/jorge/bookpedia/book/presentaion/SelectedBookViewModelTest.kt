package com.jorge.bookpedia.book.presentaion

import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.book.presentation.SelectedBookViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SelectedBookViewModelTest {

    private val viewModel = SelectedBookViewModel()

    @Test
    fun `given book is selected When onSelectBook is called Then selectedBook is updated`() =
        runTest {
            val expectedBook = Book(
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

            viewModel.onSelectBook(expectedBook)

            val result = viewModel.selectedBook.first()
            assertEquals(expectedBook, result)
        }

    @Test
    fun `given null book is selected When onSelectBook is called Then selectedBook is null`() =
        runTest {
            viewModel.onSelectBook(null)

            val result = viewModel.selectedBook.first()
            assertEquals(null, result)
        }
}