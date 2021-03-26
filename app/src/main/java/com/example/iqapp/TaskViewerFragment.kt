package com.example.iqapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iqapp.entities.Task
import android.view.MotionEvent

import android.view.View.OnTouchListener

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TaskViewerFragment : Fragment() {
    private val variantsViews = mutableListOf<ImageView>()
    private val questionsViews = mutableListOf<ImageView>()

    private val tasks = mutableListOf<Task>()

    private val maxTasks = 5
    private var currTask = 0

    private val chosenAnswers = IntArray(maxTasks) { -1 }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.task_viewer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTasks()
        initImageView()
        updateTask()

        val taskNum = view.findViewById<TextView>(R.id.taskNum)
        taskNum.text = "${currTask + 1}/${maxTasks}"

        val buttonNext = view.findViewById<Button>(R.id.button_next)

        view.findViewById<Button>(R.id.button_previous).setOnClickListener {
            saveAnswer()
            if (currTask != 0) {
                currTask--
                taskNum.text = "${currTask + 1}/${maxTasks}"
                updateTask()
                buttonNext.text = "NEXT"
            } else {
                findNavController().navigate(R.id.action_TaskViewerFragment_to_TutorialFragment)
            }

        }


        buttonNext.setOnClickListener {
            saveAnswer()
            if (currTask != maxTasks - 1) {
                currTask++
                taskNum.text = "${currTask + 1}/${maxTasks}"

                updateTask()

                if (currTask == maxTasks - 1) {
                    buttonNext.text = "RESULT"
                }

            } else {
                val bundle = bundleOf("result" to calculateResult())
                findNavController().navigate(R.id.action_TaskViewerFragment_to_resultScreenFragment, bundle)
            }
        }

    }

    private fun updateTask() {
        for ((variantView, variant) in variantsViews.zip(tasks[currTask].variants)) {
            variantView.setImageDrawable(variant.drawable)
            variantView.setBackgroundColor(Color.RED);
        }

        for ((questionView, question) in questionsViews.zip(tasks[currTask].questions)) {
            questionView.setImageDrawable(question.drawable)
        }

        val answer = chosenAnswers[currTask]
        if (answer != -1) {
            variantsViews[answer].isSelected = true
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initImageView() {
        view?.let {
            variantsViews.add(it.findViewById(R.id.variantView1))
            variantsViews.add(it.findViewById(R.id.variantView2))
            variantsViews.add(it.findViewById(R.id.variantView3))
            variantsViews.add(it.findViewById(R.id.variantView4))
            variantsViews.add(it.findViewById(R.id.variantView5))
            variantsViews.add(it.findViewById(R.id.variantView6))

            questionsViews.add(it.findViewById(R.id.questionView1))
            questionsViews.add(it.findViewById(R.id.questionView2))
            questionsViews.add(it.findViewById(R.id.questionView3))
            questionsViews.add(it.findViewById(R.id.questionView4))
        }

        for (variantView in variantsViews) {
            variantView.setOnTouchListener { _, arg1 ->
                for (variant in variantsViews) {
                    if (variant != variantView) {
                        variant.isSelected = false
                    }
                }

                if (arg1.action == MotionEvent.ACTION_DOWN) {
                    variantView.isSelected = true
                }
                true
            }

        }
    }

    private fun loadTasks() {
        val taskIds = Task.availableTaskIds()
        taskIds.shuffle()
        val chosenTasks = taskIds.take(maxTasks)

        for (taskId in chosenTasks) {
            tasks.add(Task(taskId, resources, context))
        }
    }

    private fun saveAnswer() {
        for ((i, variantView) in variantsViews.withIndex()) {
            if (variantView.isSelected) {
                variantView.isSelected = false
                chosenAnswers[currTask] = i
            }
        }
    }

    private fun calculateResult(): Int {
        var result = 0
        for ((i, task) in tasks.withIndex()) {
            if (task.answerIdx == chosenAnswers[i]) {
                result += 1
            }
        }
        return result
    }

}