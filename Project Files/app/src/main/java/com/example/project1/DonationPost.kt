package com.example.project1.models


data class DonationPost(
    val organizationName: String,
    val eventType: String,
    val eventTitle: String,         // Add eventTitle
    val eventDate: String,          // Add eventDate
    val eventDescription: String,   // Add eventDescription
    val status: String,             // Add status(loading)
    val eventImageResId: Int,       // Add eventImageResId (image resource ID)
    val isLoading: Boolean          // Add isLoading (used for the ProgressBar)

)
