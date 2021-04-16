package com.example.iqapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iqapp.entities.AppState
import com.example.iqapp.entities.Difficulty
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class TaskViewerFragment : Fragment() {

    enum class Type(val value: Int) {
        TEST(1),
        ANSWER(2),
        TRAIN(3)
    }

    private val variantsViews = mutableListOf<ImageView>()
    private val questionsViews = mutableListOf<ImageView>()

    private var currTask = 0
    private var type by Delegates.notNull<Int>()
    private var numOfTasks by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.task_viewer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments?.getInt("type")!!

        initImageView()

        val taskNumTextView = view.findViewById<TextView>(R.id.text_view_task_num)
        val buttonPrevious = view.findViewById<Button>(R.id.button_previous)
        val buttonNext = view.findViewById<Button>(R.id.button_next)
        val buttonAnswer = view.findViewById<Button>(R.id.button_show_answer)

        buttonAnswer.visibility = View.INVISIBLE
        buttonPrevious.visibility = View.INVISIBLE

        when {
            isTest() || isTrain() -> {
                numOfTasks = arguments?.getInt("numOfTasks")!!

                AppState.loadTasks(numOfTasks, resources, context)
                taskNumTextView.text = progressString()

                buttonPrevious.setOnClickListener {
                    saveAnswer()
                    decrementTask(buttonPrevious, buttonNext, taskNumTextView)
                    answerLogic(buttonAnswer)
                }

                buttonNext.setOnClickListener {
                    saveAnswer()
                    incrementTask(
                        buttonPrevious, buttonNext, taskNumTextView,
                        getString(R.string.button_result), R.id.action_TaskViewerFragment_to_resultScreenFragment
                    )
                    answerLogic(buttonAnswer)
                }

                if (isTrain()) {
                    buttonAnswer.visibility = View.VISIBLE

                    buttonAnswer.setOnClickListener {
                        showAnswer()
                        AppState.shownAnswer[currTask] = true
                        buttonAnswer.visibility = View.INVISIBLE
                    }

                    val timer = view.findViewById<TextView>(R.id.text_view_time_left)
                    timer.visibility = View.INVISIBLE
                }
            }

            isAnswer() -> {
                numOfTasks = AppState.tasks.size
                taskNumTextView.text = progressString()

                val timer = view.findViewById<TextView>(R.id.text_view_time_left)
                timer.visibility = View.INVISIBLE

                buttonPrevious.setOnClickListener {
                    decrementTask(buttonPrevious, buttonNext, taskNumTextView)
                }

                buttonNext.setOnClickListener {
                    incrementTask(
                        buttonPrevious, buttonNext, taskNumTextView,
                        getString(R.string.button_final), R.id.action_TaskViewerFragment_to_finalFragment
                    )
                }
            }
        }

        updateTask()
        val difficulty = Difficulty.values()[arguments?.getInt("difficulty")!!]
        createTimer(difficulty)?.start()
    }

    private fun createTimer(difficulty: Difficulty): CountDownTimer? {
        if (isTest()) {
            val timeForOneTask = when (difficulty) {
                Difficulty.EASY -> TimeUnit.MINUTES.toMillis(numOfTasks.toLong())
                Difficulty.NORMAL -> TimeUnit.SECONDS.toMillis(40 * numOfTasks.toLong())
                Difficulty.HARD -> TimeUnit.SECONDS.toMillis(20 * numOfTasks.toLong())
            }

            return object :
                CountDownTimer(timeForOneTask, TimeUnit.SECONDS.toMillis(1)) {
                override fun onFinish() {
                    saveAnswer()
                    findNavController().navigate(R.id.action_TaskViewerFragment_to_resultScreenFragment)
                }

                override fun onTick(p0: Long) {
                    val date = Date(p0)
                    val formatter: DateFormat = SimpleDateFormat("mm:ss")
                    formatter.timeZone = TimeZone.getTimeZone("UTC")
                    val dateFormatted: String = formatter.format(date)
                    view?.findViewById<TextView>(R.id.text_view_time_left)?.text = dateFormatted
                }
            }
        }

        return null
    }

    private fun answerLogic(buttonAnswer: Button) {
        if (isTrain()) {
            if (AppState.shownAnswer[currTask]) {
                showAnswer()
                buttonAnswer.visibility = View.INVISIBLE
            } else {
                setDefaultSelector()
                buttonAnswer.visibility = View.VISIBLE
            }
        }
    }

    private fun decrementTask(buttonPrevious: Button, buttonNext: Button, taskNum: TextView) {
        if (currTask != 0) {
            currTask--
            taskNum.text = progressString()
            updateTask()
            buttonNext.text = getString(R.string.button_next)

            if (currTask == 0) {
                buttonPrevious.visibility = View.INVISIBLE
            }
        }
    }

    private fun incrementTask(
        buttonPrevious: Button,
        buttonNext: Button,
        taskNum: TextView,
        lastNextName: String,
        actionNavigate: Int
    ) {
        val lastIdx = numOfTasks - 1

        if (currTask != lastIdx) {
            currTask++
            taskNum.text = progressString()

            updateTask()

            if (currTask == lastIdx) {
                buttonNext.text = lastNextName
            }

            buttonPrevious.visibility = View.VISIBLE

        } else {
            findNavController().navigate(actionNavigate)
        }
    }

    private fun progressString() = "${currTask + 1}/${numOfTasks}"

    private fun updateTask() {
        for ((variantView, variant) in variantsViews.zip(AppState.tasks[currTask].variants)) {
            variantView.setImageDrawable(variant.drawable)
        }

        for ((questionView, question) in questionsViews.zip(AppState.tasks[currTask].questions)) {
            questionView.setImageDrawable(question.drawable)
        }

        when {
            isTest() || isTrain() -> {
                val answer = AppState.chosenAnswers[currTask]
                if (answer != -1) {
                    variantsViews[answer].isSelected = true
                }
            }

            isAnswer() -> {
                showAnswer()
            }
        }

    }

    private fun showAnswer() {
        saveAnswer()

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

    @SuppressLint("ClickableViewAccessibility")
    private fun initImageView() {
        view?.let {
            val variantsIds = listOf(
                R.id.variantView1,
                R.id.variantView2,
                R.id.variantView3,
                R.id.variantView4,
                R.id.variantView5,
                R.id.variantView6,
            )
            for (id in variantsIds) {
                variantsViews.add(it.findViewById(id))
            }

            val questionsIds = listOf(
                R.id.questionView1,
                R.id.questionView2,
                R.id.questionView3,
                R.id.questionView4
            )

            for (id in questionsIds) {
                questionsViews.add(it.findViewById(id))
            }
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

    private fun setDefaultSelector() {
        for (variantView in variantsViews) {
            variantView.background = resources.getDrawable(R.drawable.answer_selector, context?.theme)
        }
    }

    private fun isAnswer(): Boolean = type == Type.ANSWER.value

    private fun isTrain(): Boolean = type == Type.TRAIN.value

    private fun isTest(): Boolean = type == Type.TEST.value

}
