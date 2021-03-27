package com.example.iqapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

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
        val text =
            when (type) {
                TaskViewerFragment.Type.TEST.value -> {
                    getString(R.string.tutorial_test)
                }
                TaskViewerFragment.Type.TRAIN.value -> {
                    getString(R.string.tutorial_train)
                }
                else -> ""
            }
        view.findViewById<TextView>(R.id.text_view_tutorial).text = text

        view.findViewById<Button>(R.id.button_continue_tutorial).setOnClickListener {
            val bundle = bundleOf("type" to type)
            findNavController().navigate(R.id.action_TutorialFragment_to_TaskViewerFragment, bundle)
        }
    }
}