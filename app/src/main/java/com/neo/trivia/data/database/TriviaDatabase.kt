package com.neo.trivia.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.neo.trivia.data.database.dao.FavoriteDao
import com.neo.trivia.data.database.dao.QuestionDao
import com.neo.trivia.data.database.dao.QuizResultDao
import com.neo.trivia.data.database.entity.FavoriteEntity
import com.neo.trivia.data.database.entity.QuestionEntity
import com.neo.trivia.data.database.entity.QuizResultEntity

@Database(
    entities = [QuestionEntity::class, FavoriteEntity::class, QuizResultEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TriviaDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun quizResultDao(): QuizResultDao
}