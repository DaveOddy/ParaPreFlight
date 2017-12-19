package com.davidoddy.parapreflight.model

import android.arch.persistence.room.Entity

/**
 * Created by DOddy on 12/13/17.
 */
@Entity(tableName = "item", primaryKeys = ["category", "ordinal"])
data class ChecklistItem(val category: Category, val ordinal: Int, val title: String, val subTitle: String? = null) {

    companion object ItemComparator : Comparator<ChecklistItem> {
        override fun compare(o1: ChecklistItem, o2: ChecklistItem): Int {
            val compareResult = Category.PriorityComparator.compare(o1.category, o2.category)
            return when (compareResult) {
                0 -> o1.ordinal.compareTo(o2.ordinal)
                else -> compareResult
            }
        }
    }


    override fun toString(): String {
        val builder = StringBuilder("${this.ordinal}) ${this.title}")
        if (!this.subTitle.isNullOrBlank())
            builder.append(" (${this.subTitle})")
        return builder.toString()
    }
}