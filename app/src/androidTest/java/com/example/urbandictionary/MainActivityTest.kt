package com.example.urbandictionary

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.urbandictionary.customUtils.CustomAssertions.Companion.hasItemCount
import com.example.urbandictionary.customUtils.CustomMatchers.Companion.withRecyclerView
import com.example.urbandictionary.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun test_ui_display(){
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
        onView(withId(R.id.searchButton)).check(matches(isDisplayed()))
        onView(withId(R.id.sort_text)).check(matches(isDisplayed()))
        onView(withId(R.id.wordsRV)).check(matches(isDisplayed()))
    }

    @Test
    fun test_recyclerView_contents(){
        onView(withId(R.id.searchView)).perform(typeText("word"))
        onView(withId(R.id.searchButton)).perform(click())

        //verify recyclerView items count
        onView(withId(R.id.wordsRV)).check(hasItemCount(2))

        //verify item content at first position of recyclerView
        onView( withRecyclerView(R.id.wordsRV)?.atPosition(0))
            .check(matches(hasDescendant(withText("definition 1"))))
            .check(matches(hasDescendant(withText("author"))))
    }
}