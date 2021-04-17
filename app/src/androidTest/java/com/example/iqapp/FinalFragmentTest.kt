package com.example.iqapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FinalFragmentTest {
    @Before
    fun setUp() {
        launchFragmentInContainer<FinalFragment>()
    }

    @Test
    fun buttonNamesTest() {
        val buttonIdToTextId = listOf(
            R.id.button_rate to R.string.button_rate,
            R.id.button_answers to R.string.button_answers,
            R.id.button_share to R.string.button_share,
            R.id.button_home to R.string.button_home,
        )
        FragmentTestUtil.checkButtons(buttonIdToTextId)
    }

    @Test
    fun displayedTest() {
        val ids = listOf(
            R.id.button_rate,
            R.id.button_answers,
            R.id.button_share,
            R.id.button_home,
        )
        FragmentTestUtil.checkDisplayed(ids)
    }
}