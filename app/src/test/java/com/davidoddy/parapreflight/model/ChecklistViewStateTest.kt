package com.davidoddy.parapreflight.model

import android.view.View
import com.davidoddy.parapreflight.R
import junit.framework.Assert
import org.junit.Test

/**
 * Created by DOddy on 12/13/17.
 */
class ChecklistViewStateTest {

    @Test
    fun subTextVisibility_Returns_Gone_For_Current_State_With_Null_SubTitle() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Current).getTextVisibility(ChecklistItem(Category.Safety, 0, "Title"))
        Assert.assertEquals("Wrong visibility", View.GONE, visibility)
    }

    @Test
    fun subTextVisibility_Returns_Gone_For_Current_State_With_Empty_SubTitle() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Current).getTextVisibility(ChecklistItem(Category.Safety, 0, "Title", "  "))
        Assert.assertEquals("Wrong visibility", View.GONE, visibility)
    }

    @Test
    fun subTextVisibility_Returns_Visible_For_Current_State_With_Subtitle() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Current).getTextVisibility(ChecklistItem(Category.Safety, 0, "Title", "  Sub Title w/Padding  "))
        Assert.assertEquals("Wrong visibility", View.VISIBLE, visibility)
    }

    @Test
    fun subTextVisibility_Returns_Gone_For_Non_Current_State_With_Subtitle() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Pending).getTextVisibility(ChecklistItem(Category.Safety, 0, "Title"))
        Assert.assertEquals("Wrong visibility", View.GONE, visibility)
    }

    @Test
    fun subTextVisibility_Returns_Gone_For_Non_Current_State_With_Null_SubTitle() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Pending).getTextVisibility(ChecklistItem(Category.Safety, 0, "Title", "  "))
        Assert.assertEquals("Wrong visibility", View.GONE, visibility)
    }

    @Test
    fun subTextVisibility_Returns_Visible_For_Non_Current_State_Empty_Subtitle() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Pending).getTextVisibility(ChecklistItem(Category.Safety, 0, "Title", "  Sub Title w/Padding  "))
        Assert.assertEquals("Wrong visibility", View.GONE, visibility)
    }

    @Test
    fun getIconId_Returns_Safety_Icon_For_Safety_Item_In_Pending_State() {
        val id = ChecklistViewState(ChecklistViewState.State.Pending).getIconId(ChecklistItem(Category.Safety, 0, "Test"))
        Assert.assertEquals("Wrong icon", R.drawable.priority_icon_safety, id)
    }

    @Test
    fun getIconId_Returns_Checked_Icon_For_Safety_Item_In_Checked_State() {
        val id = ChecklistViewState(ChecklistViewState.State.Checked).getIconId(ChecklistItem(Category.Safety, 0, "Test"))
        Assert.assertEquals("Wrong icon", R.drawable.priority_icon_checked, id)
    }

    @Test
    fun getIconId_Returns_Safety_Icon_For_Safety_Item_In_Current_State() {
        val id = ChecklistViewState(ChecklistViewState.State.Current).getIconId(ChecklistItem(Category.Safety, 0, "Test"))
        Assert.assertEquals("Wrong icon", R.drawable.priority_icon_safety, id)
    }

    @Test
    fun getIconId_Returns_Safety_Icon_For_Safety_Item_In_Edit_State() {
        val id = ChecklistViewState(ChecklistViewState.State.Edit).getIconId(ChecklistItem(Category.Safety, 0, "Test"))
        Assert.assertEquals("Wrong icon", R.drawable.priority_icon_safety, id)
    }

    @Test
    fun getIconId_Returns_Other_Icon_For_Other_Item_In_Pending_State() {
        val id = ChecklistViewState(ChecklistViewState.State.Pending).getIconId(ChecklistItem(Category.Other, 0, "Test"))
        Assert.assertEquals("Wrong icon", R.drawable.priority_icon_other, id)
    }

    @Test
    fun getIconId_Returns_Checked_Icon_For_Other_Item_In_Checked_State() {
        val id = ChecklistViewState(ChecklistViewState.State.Checked).getIconId(ChecklistItem(Category.Other, 0, "Test"))
        Assert.assertEquals("Wrong icon", R.drawable.priority_icon_checked, id)
    }

    @Test
    fun getIconId_Returns_Other_Icon_For_Other_Item_In_Current_State() {
        val id = ChecklistViewState(ChecklistViewState.State.Current).getIconId(ChecklistItem(Category.Other, 0, "Test"))
        Assert.assertEquals("Wrong icon", R.drawable.priority_icon_other, id)
    }

    @Test
    fun getIconId_Returns_Other_Icon_For_Other_Item_In_Edit_State() {
        val id = ChecklistViewState(ChecklistViewState.State.Edit).getIconId(ChecklistItem(Category.Other, 0, "Test"))
        Assert.assertEquals("Wrong icon", R.drawable.priority_icon_other, id)
    }

    @Test
    fun getPaginationVisibility_Returns_Visible_For_Current_Item() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Current).getPaginationVisibility()
        Assert.assertEquals("Wrong pagination visibility", View.VISIBLE, visibility)
    }

    @Test
    fun getPaginationVisibility_Returns_Visible_For_Pending_Item() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Pending).getPaginationVisibility()
        Assert.assertEquals("Wrong pagination visibility", View.VISIBLE, visibility)
    }

    @Test
    fun getPaginationVisibility_Returns_Visible_For_Checked_Item() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Checked).getPaginationVisibility()
        Assert.assertEquals("Wrong pagination visibility", View.VISIBLE, visibility)
    }

    @Test
    fun getPaginationVisibility_Returns_Gone_For_Edit_Item() {
        val visibility = ChecklistViewState(ChecklistViewState.State.Edit).getPaginationVisibility()
        Assert.assertEquals("Wrong pagination visibility", View.GONE, visibility)
    }

}