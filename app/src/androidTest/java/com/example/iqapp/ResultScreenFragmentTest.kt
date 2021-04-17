package com.example.iqapp

import androidx.fragment.app.testing.launchFragmentInContainer
import org.junit.Before
import org.junit.Test

class ResultScreenFragmentTest {

    @Before
    fun setUp() {
        launchFragmentInContainer<ResultScreenFragment>()
    }

    @Test
    fun buttonNamesTest() {
        val buttonIdToTextId = listOf(
            R.id.button_finish to R.string.button_go_to_end,
        )
        FragmentTestUtil.checkButtons(buttonIdToTextId)
    }

    @Test
    fun displayedTest() {
        val ids = listOf(
            R.id.button_finish,
            R.id.text_view_result
        )
        FragmentTestUtil.checkDisplayed(ids)
    }
}