package com.davidoddy.parapreflight

import android.os.Bundle
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import android.view.View
import com.davidoddy.parapreflight.model.ChecklistItem
import com.davidoddy.parapreflight.model.ChecklistViewState
import com.davidoddy.parapreflight.model.ItemIndexer
import com.davidoddy.parapreflight.provider.ChecklistDatabaseProvider
import com.davidoddy.parapreflight.provider.ChecklistJSONProvider
import com.davidoddy.parapreflight.provider.DatabaseLoader
import com.davidoddy.parapreflight.widgets.ChecklistItemView
import kotlinx.android.synthetic.main.activity_pre_flight_check.*

class PreFlightCheckActivity : AppCompatActivity() {

    companion object {
        private val KEY_CURRENT_INDEX = "CurrentIndex"

        private val VIEW_INDEX_TOP_1 = 0
        private val VIEW_INDEX_TOP_2 = 1
        private val VIEW_INDEX_MIDDLE = 2
        private val VIEW_INDEX_BOTTOM_1 = 3
        private val VIEW_INDEX_BOTTOM_2 = 4
    }

    private var currentIndex: Int = 0
    private var checklist: List<ChecklistItem>? = null
    private val checklistViews = arrayOf<ChecklistItemView?>(null, null, null, null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_flight_check)

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        this.currentIndex = savedInstanceState?.getInt(KEY_CURRENT_INDEX) ?: 0
    }


    override fun onResume() {
        super.onResume()

        // TODO - not the right way - ok for now
        Thread(Runnable {
            loadChecklist()
            runOnUiThread {
                setupChecklistViews()
            }
        }).start()
    }


    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState?.putInt(KEY_CURRENT_INDEX, this.currentIndex)
    }


    private fun loadChecklist() : Boolean {
        val database = ParaPreFlightApplication.database ?: return false
        this.checklist = ChecklistDatabaseProvider(database).getChecklist()
        if (this.checklist?.isEmpty() ?: return false) {
            val checklist = ChecklistJSONProvider(ChecklistJSONProvider.createReaderFromResource(resources, R.raw.default_checklist)).getChecklist()
            DatabaseLoader().loadDatabase(checklist, database)
            return loadChecklist()
        }

        return true
    }


    private fun setupChecklistViews() {

        var itemIndex = Math.max(this.currentIndex - 2, 0)
        val listSize = this.checklist?.size ?: return

        clearViewArray()

        while (itemIndex < listSize && this.checklistViews[VIEW_INDEX_BOTTOM_2] == null) {
            val newView = createView(itemIndex) ?: continue
            when (itemIndex - this.currentIndex) {
                -2 -> this.checklistViews[VIEW_INDEX_TOP_1] = newView
                -1 -> this.checklistViews[VIEW_INDEX_TOP_2] = newView
                0 -> this.checklistViews[VIEW_INDEX_MIDDLE] = newView
                1 -> this.checklistViews[VIEW_INDEX_BOTTOM_1] = newView
                2 -> this.checklistViews[VIEW_INDEX_BOTTOM_2] = newView
            }

            itemIndex++
        }

        arrangeViews(false)
        setListeners()
    }


    private fun clearViewArray() {
        for (i in 0 until this.checklistViews.size) {
            this.checklistViews[i] = null
        }
    }


    private fun createView(index: Int) : ChecklistItemView? {

        val checklist = this.checklist ?: return null
        val newView = ChecklistItemView(this)
        val item = checklist[index]
        newView.setItem(item)
        newView.setIndexer(ItemIndexer(index + 1, checklist.size, getString(R.string.checklist_item_indexer_pattern)))
        newView.id = View.generateViewId()

        addView(newView)
        return newView
    }


    private fun addView(view: ChecklistItemView) {

        val horizontalMargin = resources.getDimensionPixelSize(R.dimen.padding_check_list)
        val verticalMargin = resources.getDimensionPixelSize(R.dimen.spacer_check_list)
        val layoutParams = ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = verticalMargin
        layoutParams.bottomMargin = verticalMargin
        layoutParams.leftMargin = horizontalMargin
        layoutParams.rightMargin = horizontalMargin

        mainLayout.addView(view, layoutParams)
    }


    private fun arrangeViews(animate: Boolean) {

        val constraintSet = ConstraintSet()
        setViewConstraints(constraintSet)
        if (animate) {
            this.checklistViews[4]?.visibility = View.GONE
            TransitionManager.beginDelayedTransition(mainLayout)
        }
        constraintSet.applyTo(mainLayout)
        if (animate) {
            this.checklistViews[4]?.visibility = View.VISIBLE
        }
    }


    private fun setViewConstraints(constraintSet: ConstraintSet) : ConstraintSet {
        arrangeTopViews(constraintSet)
        arrangeBottomViews(constraintSet)
        arrangeMiddleView(constraintSet)

        this.checklistViews[0]?.setViewState(ChecklistViewState(ChecklistViewState.State.Checked))
        this.checklistViews[1]?.setViewState(ChecklistViewState(ChecklistViewState.State.Checked))
        this.checklistViews[2]?.setViewState(ChecklistViewState(ChecklistViewState.State.Current))
        this.checklistViews[3]?.setViewState(ChecklistViewState(ChecklistViewState.State.Pending))
        this.checklistViews[4]?.setViewState(ChecklistViewState(ChecklistViewState.State.Pending))

        return constraintSet
    }


    private fun arrangeTopViews(constraintSet: ConstraintSet) {
        val topView = (if (this.checklistViews[0] == null) this.checklistViews[1] else this.checklistViews[0]) ?: return
        constraintSet.connect(topView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(topView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraintSet.connect(topView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        constraintSet.constrainDefaultHeight(topView.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)

        val bottomView = this.checklistViews[1] ?: return
        if (bottomView == topView) {
            return
        }
        val verticalMargin = resources.getDimensionPixelSize(R.dimen.spacer_check_list)
        constraintSet.connect(bottomView.id, ConstraintSet.TOP, topView.id, ConstraintSet.BOTTOM, verticalMargin)
        constraintSet.connect(bottomView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraintSet.connect(bottomView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        constraintSet.constrainDefaultHeight(bottomView.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)
    }


    private fun arrangeBottomViews(constraintSet: ConstraintSet) {
        val bottomView = (if (this.checklistViews[4] == null) this.checklistViews[3] else this.checklistViews[4]) ?: return
        constraintSet.connect(bottomView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(bottomView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraintSet.connect(bottomView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        constraintSet.constrainDefaultHeight(bottomView.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)

        val topView = this.checklistViews[3] ?: return
        if (topView == bottomView) {
            return
        }
        val verticalMargin = resources.getDimensionPixelSize(R.dimen.spacer_check_list)
        constraintSet.connect(topView.id, ConstraintSet.BOTTOM, bottomView.id, ConstraintSet.TOP, verticalMargin)
        constraintSet.connect(topView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraintSet.connect(topView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        constraintSet.constrainDefaultHeight(topView.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)
    }


    private fun arrangeMiddleView(constraintSet: ConstraintSet) {
        val middleView = this.checklistViews[2] ?: return
        constraintSet.connect(middleView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(middleView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(middleView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraintSet.connect(middleView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        constraintSet.constrainDefaultHeight(middleView.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)
    }


    private fun setListeners() {
        this.checklistViews[2]?.setOnClickListener {_ -> checkCurrentItem()}
    }


    private fun checkCurrentItem() {
        val checklist = this.checklist ?: return

        this.currentIndex++

        this.checklistViews[VIEW_INDEX_MIDDLE]?.setOnClickListener(null)

        val rotatingView = this.checklistViews[VIEW_INDEX_TOP_1]
        this.checklistViews[VIEW_INDEX_TOP_1] = this.checklistViews[VIEW_INDEX_TOP_2]
        this.checklistViews[VIEW_INDEX_TOP_2] = this.checklistViews[VIEW_INDEX_MIDDLE]
        this.checklistViews[VIEW_INDEX_MIDDLE] = this.checklistViews[VIEW_INDEX_BOTTOM_1]
        this.checklistViews[VIEW_INDEX_BOTTOM_1] = this.checklistViews[VIEW_INDEX_BOTTOM_2]
        this.checklistViews[VIEW_INDEX_BOTTOM_2] = rotatingView

        this.checklistViews[VIEW_INDEX_MIDDLE]?.setOnClickListener {_ -> checkCurrentItem()}


        if (this.currentIndex + 1 >= checklist.size) {
            if (this.checklistViews[VIEW_INDEX_BOTTOM_1] != null) {
                mainLayout.removeView(this.checklistViews[VIEW_INDEX_BOTTOM_1])
                this.checklistViews[VIEW_INDEX_BOTTOM_1] = null
            }
        }
        else if (this.checklistViews[VIEW_INDEX_BOTTOM_1] == null) {
            this.checklistViews[VIEW_INDEX_BOTTOM_1] = createView(this.currentIndex + 1)
        }

        if (this.currentIndex + 2 >= checklist.size) {
            if (this.checklistViews[VIEW_INDEX_BOTTOM_2] != null) {
                mainLayout.removeView(this.checklistViews[VIEW_INDEX_BOTTOM_2])
                this.checklistViews[VIEW_INDEX_BOTTOM_2] = null
            }
        }
        else {
            if (this.checklistViews[VIEW_INDEX_BOTTOM_2] == null) {
                this.checklistViews[VIEW_INDEX_BOTTOM_2] = createView(this.currentIndex + 2)
            }

            this.checklistViews[VIEW_INDEX_BOTTOM_2]?.setIndexer(ItemIndexer(this.currentIndex + 3, checklist.size, getString(R.string.checklist_item_indexer_pattern)))
            this.checklistViews[VIEW_INDEX_BOTTOM_2]?.setItem(checklist[this.currentIndex + 2])
        }


        arrangeViews(true)
    }
}
