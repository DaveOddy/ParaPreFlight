package com.davidoddy.parapreflight.provider

import android.content.res.Resources
import android.util.JsonReader
import com.davidoddy.parapreflight.model.Category
import com.davidoddy.parapreflight.model.ChecklistItem
import java.io.InputStreamReader

/**
 * Created by DOddy on 12/13/17.
 */
class ChecklistJSONProvider(private val jsonReader: JsonReader) : IChecklistProvider {

    companion object {
        private const val NAME_TITLE = "title"
        private const val NAME_SUB_TITLE = "subtitle"

        fun createReaderFromResource(resources: Resources, id: Int) : JsonReader {
            return JsonReader(InputStreamReader(resources.openRawResource(id)))
        }
    }

    override fun getChecklist() : List<ChecklistItem> {

        val list = readChecklists()
        return list.sortedWith(ChecklistItem.ItemComparator)
    }


    private fun readChecklists() : List<ChecklistItem> {

        val list = ArrayList<ChecklistItem>()

        this.jsonReader.beginArray()
        while (this.jsonReader.hasNext()) {
            readCategory(list)
        }
        this.jsonReader.endArray()

        return list
    }


    private fun readCategory(list: MutableList<ChecklistItem>) {

        this.jsonReader.beginObject()
        while (this.jsonReader.hasNext()) {
            val name = this.jsonReader.nextName()
            when (name.toLowerCase()) {
                Category.Safety.toString().toLowerCase() -> readCategoryChecklist(Category.Safety, list)
                Category.Other.toString().toLowerCase() -> readCategoryChecklist(Category.Other, list)
                else -> this.jsonReader.skipValue()
            }
        }
        this.jsonReader.endObject()
    }


    private fun readCategoryChecklist(category: Category, list: MutableList<ChecklistItem>) {

        var ordinal = 0

        this.jsonReader.beginArray()
        while (this.jsonReader.hasNext()) {
            readChecklist(category, list, ordinal++)
        }
        this.jsonReader.endArray()
    }


    private fun readChecklist(category: Category, list: MutableList<ChecklistItem>, ordinal: Int) : Int {

        var title: String? = null
        var subTitle: String? = null

        this.jsonReader.beginObject()
        while (this.jsonReader.hasNext()) {

            val name = this.jsonReader.nextName()

            when (name.toLowerCase()) {
                NAME_TITLE -> title = this.jsonReader.nextString()
                NAME_SUB_TITLE -> subTitle = this.jsonReader.nextString()
                else -> this.jsonReader.skipValue()
            }
        }
        this.jsonReader.endObject()

        return if (title == null) {
            ordinal
        }
        else {
            list.add(ChecklistItem(category, ordinal, title, subTitle))
            ordinal + 1
        }
    }
}