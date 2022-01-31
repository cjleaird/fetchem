package com.leaird.fetchem.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PuppyItem::class], version = 1, exportSchema = false)
abstract class FetchEmDatabase : RoomDatabase() {
    abstract fun puppyItemDao(): PuppyItemDao

    companion object {
        const val DB_NAME = "fetch_em.db"
    }
}