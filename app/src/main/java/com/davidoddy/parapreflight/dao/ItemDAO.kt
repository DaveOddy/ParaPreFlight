package com.davidoddy.parapreflight.dao

import android.arch.persistence.room.*
import com.davidoddy.parapreflight.model.ChecklistItem

/**
 * Created by doddy on 12/15/17.
 */
@Dao
interface ItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: ChecklistItem)

    @Delete
    fun deleteItem(item: ChecklistItem)

    @Query("DELETE FROM item")
    fun deleteAllItems()

    @Query("SELECT * FROM item")
    fun queryItems(): Array<ChecklistItem>
}