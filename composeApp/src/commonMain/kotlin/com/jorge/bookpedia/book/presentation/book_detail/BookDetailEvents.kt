package com.jorge.bookpedia.book.presentation.book_detail

import com.jorge.bookpedia.book.domain.model.Book

sealed interface BookDetailAction {
    data object OnBackClick : BookDetailAction
    data object OnFavoriteClick : BookDetailAction
    data class OnSelectedBookChange(val book: Book) : BookDetailAction
}

sealed class BookDetailEffect {
    data object NavigateBack : BookDetailEffect()
}