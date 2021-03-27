package com.example.iqapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class StartScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_start).setOnClickListener {
            val bundle = bundleOf("type" to TaskViewerFragment.Type.TEST.value)
            findNavController().navigate(R.id.action_startScreenFragment_to_TutorialFragment, bundle)
        }

        view.findViewById<Button>(R.id.button_train).setOnClickListener {
            val bundle = bundleOf("type" to TaskViewerFragment.Type.TRAIN.value)
            findNavController().navigate(R.id.action_startScreenFragment_to_TutorialFragment, bundle)
        }
    }
}