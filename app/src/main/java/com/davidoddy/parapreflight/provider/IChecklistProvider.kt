package com.davidoddy.parapreflight.provider

import com.davidoddy.parapreflight.model.ChecklistItem

/**
 * Created by DOddy on 12/13/17.
 */
interface IChecklistProvider {
    fun getChecklist() : List<ChecklistItem>


}