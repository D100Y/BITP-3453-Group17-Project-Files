// Organization.kt
package com.example.project1

data class Organization(
    val name: String,
    val type: String, // Example: "Flood", "Fire", or "Haze"
    val description: String,
    val iconResId: Int // Resource ID for the profile icon
)
