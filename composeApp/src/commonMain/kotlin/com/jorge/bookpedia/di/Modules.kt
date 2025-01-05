package com.jorge.bookpedia.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jorge.bookpedia.book.data.source.local.database.DatabaseFactory
import com.jorge.bookpedia.book.data.source.local.database.FavoriteBookDatabase
import com.jorge.bookpedia.book.data.source.remote.api.BookApi
import com.jorge.bookpedia.book.data.source.remote.datasource.RemoteBookDataSource
import com.jorge.bookpedia.book.data.repository.BookRepositoryImpl
import com.jorge.bookpedia.book.domain.repository.BookRepository
import com.jorge.bookpedia.book.presentation.SelectedBookViewModel
import com.jorge.bookpedia.book.presentation.book_detail.BookDetailViewModel
import com.jorge.bookpedia.book.presentation.book_list.BookListViewModel
import com.jorge.bookpedia.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::BookRepositoryImpl).bind<BookRepository>()
    singleOf(::RemoteBookDataSource)
    singleOf(::BookApi)

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
}