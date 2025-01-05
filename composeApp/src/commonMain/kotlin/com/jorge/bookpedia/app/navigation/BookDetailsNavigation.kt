package com.jorge.bookpedia.app.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jorge.bookpedia.app.Route
import com.jorge.bookpedia.app.sharedKoinViewModel
import com.jorge.bookpedia.book.presentation.SelectedBookViewModel
import com.jorge.bookpedia.book.presentation.book_detail.BookDetailAction
import com.jorge.bookpedia.book.presentation.book_detail.BookDetailScreen
import com.jorge.bookpedia.book.presentation.book_detail.BookDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.bookDetailsScreen(navController: NavController) {
    composable<Route.BookDetail>(
        enterTransition = {
            slideInHorizontally { initialOffset ->
                initialOffset
            }
        },
        exitTransition = {
            slideOutHorizontally { initialOffset ->
                initialOffset
            }
        }
    ) {
        val selectedBookViewModel =
            it.sharedKoinViewModel<SelectedBookViewModel>(navController)
        val viewModel = koinViewModel<BookDetailViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()

        LaunchedEffect(selectedBook) {
            selectedBook?.let {
                viewModel.onAction(BookDetailAction.OnSelectedBookChange(it))
            }
        }

        BookDetailScreen(
            state = state,
            effect = viewModel.effect,
            onAction = viewModel::onAction,
            onNavigateBack = navController::navigateUp
        )
    }
}