package com.example.iqapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iqapp.entities.Difficulty

class TutorialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tutorial_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).supportActionBar!!.hide()

        val type = arguments?.getInt("type") ?: 1
        val tutorialTextView = view.findViewById<TextView>(R.id.text_view_tutorial)
        val chooseTextView = view.findViewById<TextView>(R.id.text_view_choose)
        val difficultyTextView = view.findViewById<TextView>(R.id.text_view_difficulty)

        val difficultySeekBar = view.findViewById<SeekBar>(R.id.seek_bar_difficulty)

        when (type) {
            TaskViewerFragment.Type.TEST.value -> {
                tutorialTextView.text = getString(R.string.tutorial_test)
                chooseTextView.text = getString(R.string.text_view_choose_and_difficulty)
                difficultySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        difficultyTextView.text = Difficulty.values()[progress].toString()
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                })
            }

            TaskViewerFragment.Type.TRAIN.value -> {
                tutorialTextView.text = getString(R.string.tutorial_train)
                chooseTextView.text = getString(R.string.text_view_choose)
                difficultySeekBar.visibility = View.INVISIBLE
                difficultyTextView.visibility = View.INVISIBLE
            }
        }


        val numOfTasksSeekBar = view.findViewById<SeekBar>(R.id.seek_bar_num_of_tasks)
        val numOfTasksTextView = view.findViewById<TextView>(R.id.text_view_num_of_tasks)
        numOfTasksTextView.text = (numOfTasksSeekBar.progress + 1).toString()

        numOfTasksSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                numOfTasksTextView.text = (progress + 1).toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        view.findViewById<Button>(R.id.button_continue_tutorial).setOnClickListener {
            val bundle = bundleOf(
                "type" to type,
                "numOfTasks" to numOfTasksSeekBar.progress + 1,
                "difficulty" to difficultySeekBar.progress
            )
            findNavController().navigate(R.id.action_TutorialFragment_to_TaskViewerFragment, bundle)
        }
    }
}