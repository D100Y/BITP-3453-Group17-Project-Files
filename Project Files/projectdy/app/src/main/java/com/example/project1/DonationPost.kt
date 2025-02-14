package com.example.project1.models

import androidx.navigation.NavType
import java.io.Serializable

// Data class to represent a donation post for an event
data class DonationPost(
    val organizationName: String,        // Name of the organization associated with the donation event
    val eventType: String,                // Type of the event (e.g., fundraiser, charity run)
    val eventTitle: String,               // Title of the event
    val eventDate: String,                // Date of the event (formatted as a string)
    val eventDescription: String,         // Description of the event, providing details about it
    val necessaryMaterials: String,       // Details about the necessary materials for the event
    val contactInfo: String,              // Contact information for the organization
    val status: String,                   // Status of the donation post (e.g., loading, completed)
    val eventImageResId: Int,            // Resource ID for the event image (used for displaying an image)
    val isLoading: Boolean                 // Flag indicating whether the post is currently loading (used for ProgressBar)
): Serializable

