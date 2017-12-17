package com.davidoddy.parapreflight.model

import android.view.View
import com.davidoddy.parapreflight.R

/**
 * Created by DOddy on 12/13/17.
 */
data class ChecklistViewState(var state: State) {

    fun getTextVisibility(item: ChecklistItem) =
            when {
                this.state != State.Current -> View.GONE
                item.subTitle == null -> View.GONE
                item.subTitle.trim().isEmpty() -> View.GONE
                else -> View.VISIBLE
            }

    fun getPaginationVisibility() = View.VISIBLE

    fun getIconId(item: ChecklistItem) =
            when {
                this.state == State.Checked -> R.drawable.priority_icon_checked
                else -> item.category.resourceId
            }

    enum class State {
        Pending,
        Checked,
        Current,
        Edit
    }
}