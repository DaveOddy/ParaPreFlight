package com.davidoddy.parapreflight.widgets

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.davidoddy.parapreflight.R
import com.davidoddy.parapreflight.databinding.ViewCheckListItemBinding
import com.davidoddy.parapreflight.model.ChecklistItem
import com.davidoddy.parapreflight.model.ChecklistViewState
import com.davidoddy.parapreflight.model.ItemIndexer

/**
 * Created by DOddy on 12/14/17.
 */
class ChecklistItemView
            @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: ViewCheckListItemBinding

    private var stashedIconSize: Int
    private var stashedTitleTextSize: Float
    private var stashedSubTitleTextSize: Float
    private var stashedIndexTextSize: Float

    init {
        val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.binding = DataBindingUtil.inflate(inflater, R.layout.view_check_list_item,this, true)

        this.stashedIconSize = (this.binding.iconImage.layoutParams as ConstraintLayout.LayoutParams).width
        this.stashedTitleTextSize = this.binding.titleText.textSize
        this.stashedSubTitleTextSize = this.binding.subTitleText.textSize
        this.stashedIndexTextSize = this.binding.indexText.textSize
    }


    private fun adjustSize(factor: Float) {
        setIconSize(this.stashedIconSize, factor)
        setTextSize(this.binding.titleText, this.stashedTitleTextSize, factor)
        setTextSize(this.binding.subTitleText, this.stashedSubTitleTextSize, factor)
        setTextSize(this.binding.indexText, this.stashedIndexTextSize, factor)
    }


    private fun restoreSize() {
        setIconSize(this.stashedIconSize)
        setTextSize(this.binding.titleText, this.stashedTitleTextSize)
        setTextSize(this.binding.subTitleText, this.stashedSubTitleTextSize)
        setTextSize(this.binding.indexText, this.stashedIndexTextSize)
    }


    private fun setIconSize(baseSize: Int?, factor: Float) {
        val newIconSize: Int = (factor * (baseSize ?: return)).toInt()
        setIconSize(newIconSize)
    }


    private fun setIconSize(newSize: Int?) {
        val params = this.binding.iconImage.layoutParams ?: return
        params.height = newSize ?: return
        params.width = newSize
        this.binding.iconImage.requestLayout()
    }


    private fun setTextSize(textView: TextView?, baseSize: Float?, factor: Float) {
        val newTextSize = (factor * (baseSize ?: return))
        setTextSize(textView, newTextSize)
    }


    private fun setTextSize(textView: TextView?, newSize: Float?) {
        val size = newSize ?: return
        textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }


    fun setItem(item: ChecklistItem) {
        this.binding.item = item
        requestLayout()
    }


    fun setIndexer(indexer: ItemIndexer) {
        this.binding.indexer = indexer
        this.binding.indexText.requestLayout()
        requestLayout()
    }


    fun setViewState(viewState: ChecklistViewState) {
        this.binding.viewState = viewState

        when (viewState.state) {
            ChecklistViewState.State.Current -> restoreSize()
            ChecklistViewState.State.Edit -> restoreSize()
            else -> adjustSize(.75f)
        }

        requestLayout()
    }
}