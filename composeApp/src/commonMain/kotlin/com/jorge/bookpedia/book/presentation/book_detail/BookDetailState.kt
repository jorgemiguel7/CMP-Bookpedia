package com.jorge.bookpedia.book.presentation.book_detail

import com.jorge.bookpedia.book.domain.model.Book

data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null
)
