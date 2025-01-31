package com.example.project1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
class DonationDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation_details, container, false)

        // Retrieve passed data
        val eventTitle = arguments?.getString("eventTitle")
        val date = arguments?.getString("date")
        val description = arguments?.getString("description")
        val imageResId = arguments?.getInt("imageResId")

        // Bind data to views
        val titleTextView: TextView = view.findViewById(R.id.eventTitleTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val eventImageView: ImageView = view.findViewById(R.id.eventImageView)

        titleTextView.text = eventTitle
        dateTextView.text = date
        descriptionTextView.text = description
        imageResId?.let { eventImageView.setImageResource(it) }

        return view
    }
}
