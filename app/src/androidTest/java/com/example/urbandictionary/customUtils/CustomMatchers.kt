package com.example.urbandictionary.customUtils

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class CustomMatchers {
    companion object {
        fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher? {
            return RecyclerViewMatcher(
                recyclerViewId
            )
        }

        fun typeSearchViewText(text: String?): ViewAction? {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View> { //Ensure that only apply if it is a SearchView and if it is visible.
                    return Matchers.allOf(
                        ViewMatchers.isDisplayed(),
                        ViewMatchers.isAssignableFrom(SearchView::class.java)
                    )
                }

                override fun getDescription(): String {
                    return "Change view text"
                }

                override fun perform(uiController: UiController?, view: View) {
                    (view as SearchView).setQuery(text, false)
                }
            }
        }
    }
}