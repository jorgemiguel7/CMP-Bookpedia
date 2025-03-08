package com.jorge.bookpedia.book.presentaion.book_list

import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.book.domain.repository.BookRepository
import com.jorge.bookpedia.book.presentation.book_list.BookListAction
import com.jorge.bookpedia.book.presentation.book_list.BookListEffect
import com.jorge.bookpedia.book.presentation.book_list.BookListViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BookListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val bookRepository: BookRepository = mockk()
    private val viewModel = BookListViewModel(bookRepository)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid action When onAction is called with OnBookClick Then NavigateToBookDetail effect is emitted`() =
        runTest {
            val result = async(testDispatcher) { viewModel.effects.firstOrNull() }
            viewModel.onAction(BookListAction.OnBookClick(getBook()))

            assertEquals(BookListEffect.NavigateToBookDetail(getBook()), result.await())
        }

    @Test
    fun `given valid action When onAction is called with OnSearchQueryChange Then state is updated with new query`() =
        runTest {
            val query = "Kotlin"

            viewModel.onAction(BookListAction.OnSearchQueryChange(query))

            assertEquals(query, viewModel.state.value.searchQuery)
        }

    @Test
    fun `given valid action When onAction is called with OnTabSelected Then state is updated with new tab index`() =
        runTest {
            val index = 0

            viewModel.onAction(BookListAction.OnTabSelected(index))

            assertEquals(index, viewModel.state.value.selectedTabIndex)
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