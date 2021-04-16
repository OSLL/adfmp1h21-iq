package com.example.iqapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iqapp.entities.AppState

class ResultScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.result_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.text_view_result).text = resultString()

        view.findViewById<Button>(R.id.button_finish).setOnClickListener {
            findNavController().navigate(R.id.action_resultScreenFragment_to_finalFragment)
        }
    }

    private fun resultString(): String {
        val passed = AppState.passedTasks()
        val result = AppState.result()

        return when {
            result >= 0.75 -> getString(R.string.resultView1, passed)
            result >= 0.5 -> getString(R.string.resultView2, passed)
            result >= 0.25 -> getString(R.string.resultView3, passed)
            else -> getString(R.string.resultView4, passed)
        }
    }

}