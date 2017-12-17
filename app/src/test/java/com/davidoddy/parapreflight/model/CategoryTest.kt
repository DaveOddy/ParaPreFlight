package com.davidoddy.parapreflight.model

import org.junit.Assert
import org.junit.Test

/**
 * Created by DOddy on 12/13/17.
 */
class CategoryTest {

    @Test
    fun comparator_Sorts_Properly() {
        val categories = arrayListOf(Category.Other,Category.Safety)

        categories.sortWith(Category.PriorityComparator)

        Assert.assertEquals("Incorrect sort", Category.Safety, categories[0])
        Assert.assertEquals("Incorrect sort", Category.Other, categories[1])
    }

}