package com.davidoddy.parapreflight.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.davidoddy.parapreflight.model.Category
import com.davidoddy.parapreflight.model.ChecklistItem

/**
 * Created by doddy on 12/15/17.
 */
@Database(entities = [(ChecklistItem::class)], version = 1)
@TypeConverters(Category.EnumConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDAO(): ItemDAO
}