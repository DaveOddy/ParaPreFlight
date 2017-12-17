package com.davidoddy.parapreflight.provider

import com.davidoddy.parapreflight.dao.AppDatabase
import com.davidoddy.parapreflight.model.ChecklistItem

/**
 * Created by doddy on 12/15/17.
 */
class DatabaseLoader {

    fun loadDatabase(checklist: List<ChecklistItem>, database: AppDatabase) {

        database.itemDAO().deleteAllItems()
        checklist.forEach { it ->
            database.itemDAO().insertItem(it)
        }
    }
}