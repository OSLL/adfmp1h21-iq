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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs

import com.example.iqapp.entities.AppState
import com.example.iqapp.entities.AppState.maxTasks

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TaskViewerFragment : Fragment() {
    private val variantsViews = mutableListOf<ImageView>()
    private val questionsViews = mutableListOf<ImageView>()

    private var currTask = 0
    private var isAnswers = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.task_viewer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isAnswers = arguments?.getBoolean("isAnswers") ?: false

        if (!isAnswers) {
            AppState.loadTasks(resources, context)
        }

        initImageView()


        val taskNum = view.findViewById<TextView>(R.id.taskNum)
        taskNum.text = progressString()

        val buttonPrevious = view.findViewById<Button>(R.id.button_previous)
        if (isAnswers && currTask == 0) {
            buttonPrevious.visibility = View.INVISIBLE
        }

        val buttonNext = view.findViewById<Button>(R.id.button_next)

        if (!isAnswers) {
            buttonPrevious.setOnClickListener {
                saveAnswer()
                if (currTask != 0) {
                    currTask--
                    taskNum.text = progressString()
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
                    taskNum.text = progressString()

                    updateTask()

                    if (currTask == maxTasks - 1) {
                        buttonNext.text = "RESULT"
                    }

                } else {
                        findNavController().navigate(R.id.action_TaskViewerFragment_to_resultScreenFragment)
                }
            }
        } else {
            buttonPrevious.setOnClickListener {
                if (currTask != 0) {
                    currTask--
                    taskNum.text = progressString()
                    updateTask()
                    buttonNext.text = "NEXT"

                    if (currTask == 0) {
                        buttonPrevious.visibility = View.INVISIBLE
                    }
                }
            }

            buttonNext.setOnClickListener {
                val lastIdx = maxTasks - 1

                if (currTask != lastIdx) {
                    currTask++
                    taskNum.text = progressString()

                    updateTask()

                    if (currTask == lastIdx) {
                        buttonNext.text = "FINAL"
                    }

                    buttonPrevious.visibility = View.VISIBLE

                } else {
                    findNavController().navigate(R.id.action_TaskViewerFragment_to_finalFragment)
                }
            }


        }
        updateTask()


    }

    private fun progressString(): String {
        return "${currTask + 1}/${maxTasks}"
    }

    private fun updateTask() {
        for ((variantView, variant) in variantsViews.zip(AppState.tasks[currTask].variants)) {
            variantView.setImageDrawable(variant.drawable)
        }

        for ((questionView, question) in questionsViews.zip(AppState.tasks[currTask].questions)) {
            questionView.setImageDrawable(question.drawable)
        }

        if (!isAnswers) {
            val answer = AppState.chosenAnswers[currTask]
            if (answer != -1) {
                variantsViews[answer].isSelected = true
            }
        } else {
            var realAnswer = -1
            for ((i, viewToVariant) in variantsViews.zip(AppState.tasks[currTask].variants).withIndex()) {
                val (variantView, variant) = viewToVariant
                variantView.setBackgroundColor(Color.TRANSPARENT)
                variantView.isPressed = false

                if (variant.isAnswer) {
                    variantView.setBackgroundColor(Color.GREEN)
                    realAnswer = i
                }
            }

            val answer = AppState.chosenAnswers[currTask]
            if (answer != realAnswer && answer != -1) {
                variantsViews[answer].setBackgroundColor(Color.RED)
            }

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

    private fun saveAnswer() {
        for ((i, variantView) in variantsViews.withIndex()) {
            if (variantView.isSelected) {
                variantView.isSelected = false
                AppState.chosenAnswers[currTask] = i
            }
        }
    }

}