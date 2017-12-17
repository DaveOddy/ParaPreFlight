package com.davidoddy.parapreflight.provider

import com.davidoddy.parapreflight.dao.AppDatabase
import com.davidoddy.parapreflight.model.ChecklistItem

/**
 * Created by doddy on 12/15/17.
 */
class ChecklistDatabaseProvider(private val database: AppDatabase) : IChecklistProvider {

    override fun getChecklist(): List<ChecklistItem> {
        return database.itemDAO()
                .queryItems()
                .sortedWith(ChecklistItem.ItemComparator)
    }
}