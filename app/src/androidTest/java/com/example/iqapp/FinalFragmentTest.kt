package com.example.iqapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FinalFragmentTest{

    @Before
    fun setUp() {
        launchFragmentInContainer<FinalFragment>()
    }

    @Test
    fun checkButtonNames() {

        val checkList = listOf(
            R.id.button_rate to R.string.button_rate,
            R.id.button_answers to R.string.button_answers,
            R.id.button_share to R.string.button_share,
            R.id.button_home to R.string.button_home,
        )

        for ((button, text) in checkList) {
            Espresso
                .onView(withId(button))
                .check(ViewAssertions.matches(withText(text)))
        }
    }
}