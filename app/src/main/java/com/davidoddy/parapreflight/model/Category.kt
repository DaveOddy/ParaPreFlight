package com.davidoddy.parapreflight.model

import android.arch.persistence.room.TypeConverter
import com.davidoddy.parapreflight.R

/**
 * Created by DOddy on 12/13/17.
 */
enum class Category(val priority: Int, val resourceId: Int) {
    Safety(0, R.drawable.priority_icon_safety),
    Other(1, R.drawable.priority_icon_other);


    companion object PriorityComparator : Comparator<Category> {
        override fun compare(o1: Category, o2: Category) = o1.priority - o2.priority
    }


    class EnumConverter {
        @TypeConverter
        fun toCategory(asString: String) = Category.valueOf(asString)

        @TypeConverter
        fun toString(asEnum: Category) = asEnum.toString()
    }
}