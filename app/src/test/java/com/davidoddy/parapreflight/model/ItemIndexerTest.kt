package com.davidoddy.parapreflight.model

import org.junit.Assert
import org.junit.Test

/**
 * Created by DOddy on 12/13/17.
 */
class ItemIndexerTest {

    @Test
    fun toString_Returns_Properly_Formatted_String() {
        val formattedString = ItemIndexer(1, 10, "%d of %d").toString()
        Assert.assertEquals("Incorrect format result", "1 of 10", formattedString)
    }

}