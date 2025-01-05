package com.jorge.bookpedia.book.presentation.book_list

import com.jorge.bookpedia.book.domain.model.Book

sealed interface BookListAction {
    data class OnSearchQueryChange(val query: String) : BookListAction
    data class OnBookClick(val book: Book) : BookListAction
    data class OnTabSelected(val index: Int) : BookListAction
}

sealed class BookListEffect {
    data class NavigateToBookDetail(val book: Book) : BookListEffect()
}