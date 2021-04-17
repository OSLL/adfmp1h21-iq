package com.example.iqapp.entities

import android.content.Context
import android.content.res.Resources

object AppState {

    private var numOfTasks = 0

    private val _tasks = mutableListOf<Task>()
    val tasks: List<Task>
        get() = _tasks

    lateinit var chosenAnswers: IntArray
        private set

    lateinit var shownAnswers: BooleanArray
        private set

    fun loadTasks(numOfTasks: Int, resources: Resources, context: Context?) {
        _tasks.clear()
        chosenAnswers = IntArray(numOfTasks) { -1 }
        shownAnswers = BooleanArray(numOfTasks) { false }

        val taskIds = Task.availableTaskIds().toMutableList()
        taskIds.shuffle()
        val chosenTasks = taskIds.take(numOfTasks)

        for (taskId in chosenTasks) {
            _tasks.add(Task(taskId, resources, context))
        }

        this.numOfTasks = numOfTasks
    }

    fun passedTasks(): Int {
        var result = 0
        for ((i, task) in _tasks.withIndex()) {
            if (task.answerIdx == chosenAnswers[i]) {
                result += 1
            }
        }
        return result
    }

    fun result(): Double = passedTasks().toDouble() / numOfTasks
}
