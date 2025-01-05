package com.jorge.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.jorge.bookpedia.book.data.mappers.toBook
import com.jorge.bookpedia.book.data.mappers.toBookEntity
import com.jorge.bookpedia.book.data.source.local.dao.FavoriteBookDao
import com.jorge.bookpedia.book.data.source.remote.datasource.RemoteBookDataSource
import com.jorge.bookpedia.book.domain.model.Book
import com.jorge.bookpedia.book.domain.repository.BookRepository
import com.jorge.bookpedia.core.domain.DataError
import com.jorge.bookpedia.core.domain.EmptyResult
import com.jorge.bookpedia.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(
    private val remoteDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
) : BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteDataSource.searchBooks(query)
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        val localResult = favoriteBookDao.getFavoriteBook(bookId)

        return if (localResult == null) {
            remoteDataSource.getBookDetails(bookId)
        } else {
            Result.Success(localResult.description)
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.map { it.toBook() }
            }
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.any { it.id == id }
            }
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFromFavorites(id: String) {
        favoriteBookDao.deleteFavoriteBook(id)
    }
}