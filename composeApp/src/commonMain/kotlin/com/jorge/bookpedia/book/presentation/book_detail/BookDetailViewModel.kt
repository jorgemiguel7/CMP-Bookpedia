package com.jorge.bookpedia.book.presentation.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jorge.bookpedia.app.Route
import com.jorge.bookpedia.book.domain.repository.BookRepository
import com.jorge.bookpedia.book.presentation.book_list.BookListEffect
import com.jorge.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart {
            fetchBookDescription()
            observeFavoriteStatus()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )
    private val _effect = MutableSharedFlow<BookDetailEffect>()
    val effect = _effect.asSharedFlow()

    fun onAction(action: BookDetailAction) {
        when (action) {
            is BookDetailAction.OnSelectedBookChange -> _state.update { it.copy(book = action.book) }
            is BookDetailAction.OnFavoriteClick -> handleFavoriteClick()
            is BookDetailAction.OnBackClick -> viewModelScope.launch {
                _effect.emit(BookDetailEffect.NavigateBack)
            }
        }
    }

    private fun handleFavoriteClick() = viewModelScope.launch {
        if (state.value.isFavorite) {
            bookRepository.deleteFromFavorites(bookId)
        } else {
            state.value.book?.let { book ->
                bookRepository.markAsFavorite(book)
            }
        }
    }

    private fun observeFavoriteStatus() {
        bookRepository
            .isBookFavorite(bookId)
            .onEach { isFavorite ->
                _state.update { it.copy(isFavorite = isFavorite) }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchBookDescription() {
        viewModelScope.launch {
            bookRepository
                .getBookDescription(bookId)
                .onSuccess { description ->
                    _state.update {
                        it.copy(
                            book = it.book?.copy(description = description),
                            isLoading = false
                        )
                    }
                }
        }
    }
}