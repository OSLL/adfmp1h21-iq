package com.example.iqapp.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class TaskTest {
    @Test
    fun `num of unique tasks`() {
        val ids = Task.availableTaskIds().toSet()
        assertEquals(20, ids.size)
    }
}
