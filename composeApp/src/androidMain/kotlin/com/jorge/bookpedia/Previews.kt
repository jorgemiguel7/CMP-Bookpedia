package com.jorge.bookpedia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.book.presentation.book_list.BookListScreen
import com.jorge.bookpedia.book.presentation.book_list.BookListState
import com.jorge.bookpedia.book.presentation.book_list.components.BookSearchBar
import kotlinx.coroutines.flow.MutableSharedFlow

@Preview
@Composable
private fun BookSearchBarPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        BookSearchBar(
            searchQuery = "",
            onSearchQueryChange = {},
            onImeSearch = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private val books = (1..100).map {
    Book(
        id = it.toString(),
        title = "Book $it",
        imageUrl = "https://test.com",
        authors = listOf("Jorge"),
        description = "Description $it",
        languages = emptyList(),
        firstPublishYear = null,
        averageRating = 4.67854,
        ratingCount = 5,
        numPages = 100,
        numEditions = 3
    )
}

@Preview
@Composable
private fun BookListScreenPreview() {
    BookListScreen(
        state = BookListState(
            isLoading = false,
            searchResults = books
        ),
        onAction = {},
        effects = MutableSharedFlow(),
        onNavigateToBookDetail = {}
    )
}