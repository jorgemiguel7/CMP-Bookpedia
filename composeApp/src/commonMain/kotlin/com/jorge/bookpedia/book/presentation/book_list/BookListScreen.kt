package com.jorge.bookpedia.book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.favorites
import cmp_bookpedia.composeapp.generated.resources.no_favorite_books
import cmp_bookpedia.composeapp.generated.resources.no_search_results
import cmp_bookpedia.composeapp.generated.resources.search_results
import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.book.presentation.book_list.components.BookList
import com.jorge.bookpedia.book.presentation.book_list.components.BookSearchBar
import com.jorge.bookpedia.core.presentation.DarkBlue
import com.jorge.bookpedia.core.presentation.DesertWhite
import com.jorge.bookpedia.core.presentation.SandYellow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookListScreen(
    state: BookListState,
    onAction: (BookListAction) -> Unit,
    effects: SharedFlow<BookListEffect>,
    onNavigateToBookDetail: (Book) -> Unit
) {
    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                is BookListEffect.NavigateToBookDetail -> {
                    onNavigateToBookDetail(effect.book)
                }
            }
        }
    }

    BookListScreenContent(state, onAction)
}

@Composable
private fun BookListScreenContent(
    state: BookListState,
    onAction: (BookListAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val pagerState = rememberPagerState { 2 }
    val searchResultsListState = rememberLazyListState()
    val favoriteBooksListState = rememberLazyListState()

    LaunchedEffect(state.searchResults) {
        searchResultsListState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(BookListAction.OnTabSelected(pagerState.currentPage))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = { onAction(BookListAction.OnSearchQueryChange(it)) },
            onImeSearch = { keyboardController?.hide() },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BookTabs(state, onAction)
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { pageIndex ->
                    BookListContent(
                        uiState = state,
                        pageIndex = pageIndex,
                        searchResultsListState = searchResultsListState,
                        favoriteBooksListState = favoriteBooksListState,
                        onAction = onAction
                    )
                }
            }
        }
    }
}

@Composable
private fun BookTabs(uiState: BookListState, onAction: (BookListAction) -> Unit) {
    Column {
        TabRow(
            selectedTabIndex = uiState.selectedTabIndex,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .widthIn(max = 700.dp)
                .fillMaxWidth(),
            containerColor = DesertWhite,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    color = SandYellow,
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[uiState.selectedTabIndex])
                )
            }
        ) {
            Tab(
                selected = uiState.selectedTabIndex == 0,
                onClick = {
                    onAction(BookListAction.OnTabSelected(0))
                },
                modifier = Modifier.weight(1f),
                selectedContentColor = SandYellow,
                unselectedContentColor = Color.Black.copy(alpha = 0.5f)
            ) {
                Text(
                    text = stringResource(Res.string.search_results),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
            Tab(
                selected = uiState.selectedTabIndex == 1,
                onClick = {
                    onAction(BookListAction.OnTabSelected(1))
                },
                modifier = Modifier.weight(1f),
                selectedContentColor = SandYellow,
                unselectedContentColor = Color.Black.copy(alpha = 0.5f)
            ) {
                Text(
                    text = stringResource(Res.string.favorites),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun BookListContent(
    uiState: BookListState,
    pageIndex: Int,
    searchResultsListState: LazyListState,
    favoriteBooksListState: LazyListState,
    onAction: (BookListAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (pageIndex) {
            BookListTab.SEARCH_RESULTS.value -> {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    when {
                        uiState.errorMessage != null -> {
                            Text(
                                text = uiState.errorMessage.asString(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        uiState.searchResults.isEmpty() -> {
                            Text(
                                text = stringResource(Res.string.no_search_results),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        else -> {
                            BookList(
                                books = uiState.searchResults,
                                onBookClick = {
                                    onAction(BookListAction.OnBookClick(it))
                                },
                                modifier = Modifier.fillMaxSize(),
                                scrollState = searchResultsListState
                            )
                        }
                    }
                }
            }

            BookListTab.FAVORITES.value -> {
                if (uiState.favoriteBooks.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.no_favorite_books),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                } else {
                    BookList(
                        books = uiState.favoriteBooks,
                        onBookClick = {
                            onAction(BookListAction.OnBookClick(it))
                        },
                        modifier = Modifier.fillMaxSize(),
                        scrollState = favoriteBooksListState
                    )
                }
            }
        }
    }
}

private enum class BookListTab(val value: Int) {
    SEARCH_RESULTS(0),
    FAVORITES(1)
}
