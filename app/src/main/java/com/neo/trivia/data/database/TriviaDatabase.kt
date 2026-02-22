package com.neo.trivia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.neo.trivia.data.database.dao.FavoriteDao
import com.neo.trivia.data.database.dao.QuestionDao
import com.neo.trivia.data.database.entity.FavoriteEntity
import com.neo.trivia.data.database.entity.QuestionEntity

@Database(
    entities = [QuestionEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TriviaDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: TriviaDatabase? = null

        fun getDatabase(context: Context): TriviaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TriviaDatabase::class.java,
                    "trivia_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}