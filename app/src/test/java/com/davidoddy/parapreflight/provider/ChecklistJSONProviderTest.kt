package com.davidoddy.parapreflight.provider

import android.util.JsonReader
import com.davidoddy.parapreflight.BuildConfig
import com.davidoddy.parapreflight.model.ChecklistItem
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Created by DOddy on 12/13/17.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ChecklistJSONProviderTest {

    @Test
    fun getChecklist_Returns_Ordered_Checklist() {

        val jsonReader = JsonReader(InputStreamReader(ByteArrayInputStream(testJsonString.toByteArray(StandardCharsets.UTF_8))))
        jsonReader.use { jsonReader ->
            val checklistItems = ChecklistJSONProvider(jsonReader).getChecklist()

            Assert.assertNotEquals("Empty list returned.", 0, checklistItems.size)
            var lastItem: ChecklistItem? = null
            for (checkListItem in checklistItems) {
                Assert.assertEquals("List not properly sorted.", true, checkItemOrder(lastItem, checkListItem))
                lastItem = checkListItem
            }
        }
    }



    private fun checkItemOrder(lastItem: ChecklistItem?, thisItem: ChecklistItem): Boolean {
        return ChecklistItem.ItemComparator.compare(lastItem ?: return true, thisItem) < 0
    }



    private val testJsonString =
            """
                [
                  {
                    "other": [
                      {
                        "title": "Camera"
                      },
                      {
                        "title": "Pockets"
                      },
                      {
                        "title": "Music"
                      }
                    ]
                  },
                  {
                    "safety": [
                      {
                        "title": "Helmet/Chin Strap"
                      },
                      {
                        "title": "Glasses"
                      },
                      {
                        "title": "Shoe Laces"
                      },
                      {
                        "title": "Gloves"
                      },
                      {
                        "title": "Leg Straps",
                        "subTitle": "Secure and properly adjusted."
                      },
                      {
                        "title": "Chest Strap",
                        "subTitle": "Secure and properly adjusted."
                      },
                      {
                        "title": "Chest Clip",
                        "subTitle": "Secure and properly adjusted."
                      },
                      {
                        "title": "Reserve Handle",
                        "subTitle": "Secure and unobstructed."
                      },
                      {
                        "title": "Risers & Lines",
                        "subTitle": "Clear, untwisted and in good condition."
                      },
                      {
                        "title": "Speed Bar & Lines",
                        "subTitle": "Connected, available with clear lines."
                      },
                      {
                        "title": "Carabiners",
                        "subTitle": "Fastened and properly aligned."
                      },
                      {
                        "title": "Glider",
                        "subTitle": "Properly prepared and in good condition."
                      },
                      {
                        "title": "Radio",
                        "subTitle": "Charged, on and tuned to the correct frequency."
                      },
                      {
                        "title": "Flight Deck",
                        "subTitle": "Secure and properly aligned - instruments on."
                      }
                    ]
                  }
                ]
            """.trimMargin()
}