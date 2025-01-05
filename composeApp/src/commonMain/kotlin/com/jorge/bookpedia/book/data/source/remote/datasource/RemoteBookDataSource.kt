package com.jorge.bookpedia.book.data.source.remote.datasource

import com.jorge.bookpedia.book.data.mappers.toBook
import com.jorge.bookpedia.book.data.source.remote.api.BookApi
import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.core.domain.DataError
import com.jorge.bookpedia.core.domain.Result
import com.jorge.bookpedia.core.domain.map

class RemoteBookDataSource(private val bookApi: BookApi) {

    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<List<Book>, DataError.Remote> =
        bookApi.searchBooks(query, resultLimit).map { searchResponseDto ->
            searchResponseDto.results.map { it.toBook() }
        }

    suspend fun getBookDetails(bookWorkId: String): Result<String?, DataError.Remote> =
        bookApi.getBookDetails(bookWorkId).map { it.description }
}