package com.jorge.bookpedia.book.data.source.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jorge.bookpedia.book.data.source.local.dao.FavoriteBookDao
import com.jorge.bookpedia.book.data.source.local.entity.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1
)
@TypeConverters(StringListTypeConverter::class)
@ConstructedBy(BookDatabaseConstructor::class)
abstract class FavoriteBookDatabase: RoomDatabase() {
    abstract val favoriteBookDao: FavoriteBookDao

    companion object {
        const val DB_NAME = "book.db"
    }
}