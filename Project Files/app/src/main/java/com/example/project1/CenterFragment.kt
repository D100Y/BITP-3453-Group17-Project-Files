package com.example.project1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CenterFragment : Fragment(R.layout.fragment_center) {

    // Initialize widgets and recycler view
    private lateinit var feedbackInput: EditText
    private lateinit var submitButton: Button
    private lateinit var confirmationMessage: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var feedbackAdapter: FeedbackAdapter

    private val feedbackList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_center, container, false)

        // Initialize views using `view`
        feedbackInput = view.findViewById(R.id.editTextFeedback)
        submitButton = view.findViewById(R.id.buttonSubmitFeedback)
        confirmationMessage = view.findViewById(R.id.textViewConfirmation)
        recyclerView = view.findViewById(R.id.recyclerViewFeedback)

        setupRecyclerView()
        setupSubmitButton()

        return view
    }

    // sets up the recycler view
    private fun setupRecyclerView() {
        feedbackAdapter = FeedbackAdapter(feedbackList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = feedbackAdapter
    }

    // allows actions on the button
    private fun setupSubmitButton() {
        submitButton.setOnClickListener {
            val feedbackText = feedbackInput.text.toString().trim()
            if (feedbackText.isNotEmpty()) {
                saveFeedback(feedbackText)
                feedbackInput.text.clear()
                confirmationMessage.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Please enter your feedback!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveFeedback(feedback: String) {
        feedbackList.add(0, feedback) // Add new feedback to the top
        feedbackAdapter.notifyDataSetChanged()
        Toast.makeText(requireContext(), "Feedback submitted!", Toast.LENGTH_SHORT).show()
    }
}
