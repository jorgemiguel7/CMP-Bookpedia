package com.jorge.bookpedia.book.data.source.remote.api

import com.jorge.bookpedia.book.data.source.remote.dto.BookWorkDto
import com.jorge.bookpedia.book.data.source.remote.dto.SearchResponseDto
import com.jorge.bookpedia.core.data.safeCall
import com.jorge.bookpedia.core.domain.DataError
import com.jorge.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BookApi(private val httpClient: HttpClient) {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int?
    ): Result<SearchResponseDto, DataError.Remote> = safeCall<SearchResponseDto> {
        httpClient.get(urlString = "/search.json") {
            parameter("q", query)
            parameter("limit", resultLimit)
            parameter("language", "eng")
            parameter(
                "fields",
                "key,title,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count"
            )
        }
    }

    suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote> =
        safeCall<BookWorkDto> {
            httpClient.get(urlString = "/works/$bookWorkId.json")
        }
}