package com.example.newsapp

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.newsapp.ui.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testBottomNavigationSwitchesToBookmarksFragment() {
        // Launch the MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Click on the Bookmarks navigation item
        onView(withId(R.id.nav_bookmarks)).perform(click())

        // Check if the toolbar title is changed to "Bookmarks"
        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle("Bookmarks")))
    }

    @Test
    fun testBottomNavigationSwitchesToSearchFragment() {
        // Launch the MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Click on the Search navigation item
        onView(withId(R.id.nav_search)).perform(click())

        // Check if the toolbar title is changed to "Search"
        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle("Search")))
    }

    @Test
    fun testBottomNavigationSwitchesToHeadlinesFragment() {
        // Launch the MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Click on the Headlines navigation item
        onView(withId(R.id.nav_headlines)).perform(click())

        // Check if the toolbar title is changed to "Headlines"
        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle("Headlines")))
    }
}
