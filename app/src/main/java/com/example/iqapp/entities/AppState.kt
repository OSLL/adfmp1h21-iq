package com.example.iqapp.entities

import android.content.Context
import android.content.res.Resources

object AppState {

    const val maxTasks = 5
    const val timeMinutes: Long = 5
    val tasks = mutableListOf<Task>()
    var chosenAnswers = IntArray(maxTasks) { -1 }

    fun loadTasks(resources: Resources, context: Context?) {
        tasks.clear()
        chosenAnswers = IntArray(maxTasks) { -1 }

        val taskIds = Task.availableTaskIds()
        taskIds.shuffle()
        val chosenTasks = taskIds.take(maxTasks)

        for (taskId in chosenTasks) {
            tasks.add(Task(taskId, resources, context))
        }
    }

    fun calculateResult(): Int {
        var result = 0
        for ((i, task) in tasks.withIndex()) {
            if (task.answerIdx == chosenAnswers[i]) {
                result += 1
            }
        }
        return result
    }


}