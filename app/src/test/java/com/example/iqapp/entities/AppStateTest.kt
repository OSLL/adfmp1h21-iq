package com.example.iqapp.entities

import android.content.res.Resources
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class AppStateTest {
    @Test
    fun `AppState loadTasks amount`() {
        val numOfTasks = 10

        val resources = mock(Resources::class.java)
        AppState.loadTasks(numOfTasks, resources, null)

        assertEquals(numOfTasks, AppState.tasks.size)
        assertEquals(numOfTasks, AppState.chosenAnswers.size)
        assertEquals(numOfTasks, AppState.shownAnswers.size)
    }
}