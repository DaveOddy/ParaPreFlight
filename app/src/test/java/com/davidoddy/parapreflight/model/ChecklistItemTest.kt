package com.davidoddy.parapreflight.model

import org.junit.Assert
import org.junit.Test

/**
 * Created by DOddy on 12/13/17.
 */
class ChecklistItemTest {

    @Test
    fun toString_Returns_Properly_With_Title() {
        val title = "TITLE"
        val subTitle = "SUB-TITLE"

        val toString = ChecklistItem(Category.Safety, 0, title, subTitle).toString()

        Assert.assertEquals("Wrong sub-title format", "TITLE (SUB-TITLE)", toString)
    }

    @Test
    fun toString_Returns_Properly_Without_Title() {
        val title = "TITLE"
        val subTitle = null

        val toString = ChecklistItem(Category.Safety, 0, title, subTitle).toString()

        Assert.assertEquals("Wrong sub-title format", "TITLE", toString)
    }

    @Test
    fun toString_Returns_Properly_With_Empty_Title() {
        val title = "TITLE"
        val subTitle = ""

        val toString = ChecklistItem(Category.Safety, 0, title, subTitle).toString()

        Assert.assertEquals("Wrong sub-title format", "TITLE", toString)
    }

    @Test
    fun comparator_Sorts_Properly() {
        val items = arrayListOf(
                ChecklistItem(Category.Other, 2, "Sorted Item 6"),
                ChecklistItem(Category.Safety, 2, "Sorted Item 3"),
                ChecklistItem(Category.Other,3, "Sorted Item 7"),
                ChecklistItem(Category.Safety, 0, "Sorted Item 1"),
                ChecklistItem(Category.Other, 4, "Sorted Item 8"),
                ChecklistItem(Category.Safety, 1, "Sorted Item 2"),
                ChecklistItem(Category.Other, 0, "Sorted Item 4"),
                ChecklistItem(Category.Other, 1, "Sorted Item 5")
        )

        items.sortWith(ChecklistItem.ItemComparator)

        Assert.assertEquals("Incorrect sort", "Sorted Item 1", items[0].title)
        Assert.assertEquals("Incorrect sort", "Sorted Item 2", items[1].title)
        Assert.assertEquals("Incorrect sort", "Sorted Item 3", items[2].title)
        Assert.assertEquals("Incorrect sort", "Sorted Item 4", items[3].title)
        Assert.assertEquals("Incorrect sort", "Sorted Item 5", items[4].title)
        Assert.assertEquals("Incorrect sort", "Sorted Item 6", items[5].title)
        Assert.assertEquals("Incorrect sort", "Sorted Item 7", items[6].title)
        Assert.assertEquals("Incorrect sort", "Sorted Item 8", items[7].title)
    }
}