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
import com.jorge.bookpedia.book.presentation.book_list.BookListScreen
import com.jorge.bookpedia.book.presentation.book_list.BookListViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.bookListScreen(navController: NavController) {
    composable<Route.BookList>(
        exitTransition = { slideOutHorizontally() },
        popEnterTransition = { slideInHorizontally() }
    ) {
        val viewModel = koinViewModel<BookListViewModel>()
        val selectedBookViewModel =
            it.sharedKoinViewModel<SelectedBookViewModel>(navController)
        val uiState by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(true) {
            selectedBookViewModel.onSelectBook(null)
        }

        BookListScreen(
            state = uiState,
            onAction = viewModel::onAction,
            effects = viewModel.effects,
            onNavigateToBookDetail = { book ->
                selectedBookViewModel.onSelectBook(book)
                navController.navigate(Route.BookDetail(book.id))
            }
        )
    }
}