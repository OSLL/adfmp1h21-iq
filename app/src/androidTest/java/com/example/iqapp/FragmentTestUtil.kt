package com.example.iqapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.junit.Assert


object FragmentTestUtil {

    fun <T> navigationTest(
        scenario: FragmentScenario<T>,
        buttonId: Int,
        destFragmentId: Int
    ) where T : Fragment {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(buttonId)).perform(ViewActions.click())
        Assert.assertEquals(navController.currentDestination?.id, destFragmentId)
    }

    fun checkButtons(buttonIdToTextId: List<Pair<Int, Int>>) {
        for ((buttonId, textId) in buttonIdToTextId) {
            Espresso
                .onView(ViewMatchers.withId(buttonId))
                .check(ViewAssertions.matches(ViewMatchers.withText(textId)))
        }
    }

    fun checkDisplayed(ids: List<Int>) {
        for (id in ids) {
            Espresso
                .onView(ViewMatchers.withId(id))
                .check(ViewAssertions.matches(isDisplayed()))
        }
    }

}