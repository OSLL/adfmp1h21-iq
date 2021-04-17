package com.example.iqapp.entities

import android.content.res.Resources
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class AppStateTest {
    private val numOfTasks = 10

    @Before
    fun setUp() {
        val resources = mock(Resources::class.java)
        AppState.loadTasks(numOfTasks, resources, null)
    }

    @Test
    fun `AppState loadTasks amount`() {
        assertEquals(numOfTasks, AppState.tasks.size)
        assertEquals(numOfTasks, AppState.chosenAnswers.size)
        assertEquals(numOfTasks, AppState.shownAnswers.size)
    }

    @Test
    fun `AppState passed tasks`() {
        assertEquals(numOfTasks, AppState.passedTasks())
        assertEquals(1.0, AppState.result(), 0.0001)
    }
}