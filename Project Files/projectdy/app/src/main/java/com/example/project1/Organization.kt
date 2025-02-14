// Organization.kt
package com.example.project1

// Data class to represent an organization involved in donation events
data class Organization(
    val name: String,                // Name of the organization
    val type: String,                // Type of event the organization is associated with (e.g., "Flood", "Fire", "Haze")
    val description: String,         // Description of the organization and its mission
    val iconResId: Int               // Resource ID for the organization's profile icon (used for displaying an icon)
)