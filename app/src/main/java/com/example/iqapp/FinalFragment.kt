package com.example.iqapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FinalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_final, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.button_home).setOnClickListener {
            findNavController().navigate(R.id.action_finalFragment_to_startScreenFragment)
        }

        view.findViewById<Button>(R.id.button_answers).setOnClickListener {
            val bundle = bundleOf("type" to TaskViewerFragment.Type.ANSWER.value)
            findNavController().navigate(R.id.action_finalFragment_to_TaskViewerFragment, bundle)
        }
    }

}