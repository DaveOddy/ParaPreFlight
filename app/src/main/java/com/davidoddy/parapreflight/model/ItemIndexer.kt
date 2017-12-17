package com.davidoddy.parapreflight.model

/**
 * Created by DOddy on 12/13/17.
 */
data class ItemIndexer(private val index: Int, private val itemCount: Int, private val formatPattern: String) {

    override fun toString() = String.format(this.formatPattern, this.index, this.itemCount)
}