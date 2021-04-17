package com.example.iqapp

import androidx.fragment.app.testing.launchFragmentInContainer
import org.junit.Test

class StartScreenFragmentTest {

    @Test
    fun buttonNamesTest() {
        launchFragmentInContainer<StartScreenFragment>()
        val buttonIdToTextId = listOf(
            R.id.button_start to R.string.start,
            R.id.button_train to R.string.button_train,
        )
        FragmentTestUtil.checkButtons(buttonIdToTextId)
    }

    @Test
    fun displayedTest() {
        launchFragmentInContainer<StartScreenFragment>()
        val ids = listOf(
            R.id.button_start,
            R.id.button_train
        )
        FragmentTestUtil.checkDisplayed(ids)
    }

    @Test
    fun navigationStartTest() {
        val scenario = launchFragmentInContainer<StartScreenFragment>()
        FragmentTestUtil.navigationTest(scenario, R.id.button_start, R.id.TutorialFragment)
    }

    @Test
    fun navigationTrainTest() {
        val scenario = launchFragmentInContainer<StartScreenFragment>()
        FragmentTestUtil.navigationTest(scenario, R.id.button_train, R.id.TutorialFragment)
    }
}