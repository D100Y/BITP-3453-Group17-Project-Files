package com.example.project1
// Data class to represent a donation record
data class Donation(
    val id: Long,// Unique identifier for the donation
    val donorName: String,// Name of the donor
    val donationAmount: Double,// Amount of the donation
    val donorEmail: String,// Email address of the donor
    val paymentMethod: String// Method used for the payment (e.g., TNG , Google pay)
)
