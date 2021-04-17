package com.example.iqapp

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class TutorialFragmentTest {
    @Test
    fun buttonNamesTest() {
        launchFragmentInContainer<TutorialFragment>()
        val buttonIdToTextId = listOf(
            R.id.button_continue_tutorial to R.string.continue_to_tasks
        )
        FragmentTestUtil.checkButtons(buttonIdToTextId)
    }

    @Test
    fun displayedTest() {
        launchFragmentInContainer<TutorialFragment>()
        val ids = listOf(
            R.id.button_continue_tutorial,
            R.id.seek_bar_difficulty,
            R.id.seek_bar_num_of_tasks,
            R.id.text_view_tutorial,
        )
        FragmentTestUtil.checkDisplayed(ids)
    }

}