package com.example.project1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

// Fragment to display organization details and handle donation submissions
class OrganizationDetailsFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper // Database helper for managing donations

    // Called to create the view hierarchy associated with the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_organization_details, container, false)

        // Initialize database helper
        databaseHelper = DatabaseHelper(requireContext())

        // Get the organization details passed in the arguments
        val organizationName = arguments?.getString("name") // Get organization name from arguments
        val organizationDescription = arguments?.getString("description") // Get organization description
        val organizationIconResId = arguments?.getInt("icon") // Get organization icon resource ID

        // Find views for displaying organization details
        val nameTextView: TextView = view.findViewById(R.id.detailsOrganizationName)
        val iconImageView: ImageView = view.findViewById(R.id.detailsOrganizationIcon)

        // Set the organization details in the views
        nameTextView.text = organizationName
        organizationIconResId?.let { iconImageView.setImageResource(it) }

        // Set up back button to navigate back to the previous fragment
        val backButton: ImageButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // Pop the back stack to return to the previous fragment
        }

        // Setup Spinner for donation amounts
        val predefinedAmounts = listOf("100", "200", "500", "1000", "Custom")// List of predefined amounts
        val donationAmountSpinner: Spinner = view.findViewById(R.id.donationAmountSpinner)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, predefinedAmounts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        donationAmountSpinner.adapter = adapter // Set the adapter for the spinner

        val customAmountEditText: EditText = view.findViewById(R.id.customDonationAmount)// EditText for custom amount

        // Handle the spinner item selection for donation amounts
        donationAmountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Show custom amount input if "Custom" is selected
                if (predefinedAmounts[position] == "Custom") {
                    customAmountEditText.visibility = View.VISIBLE // Show custom amount field
                } else {
                    customAmountEditText.visibility = View.GONE// Hide custom amount field
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                customAmountEditText.visibility = View.GONE  // Hide custom amount field if nothing is selected
            }
        }

        // Setup Spinner for Payment Methods
        val paymentMethods = listOf("Touch N Go", "Google Pay") // List of payment methods
        val paymentMethodSpinner: Spinner = view.findViewById(R.id.paymentMethodSpinner)
        val paymentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paymentMethodSpinner.adapter = paymentAdapter // Set the adapter for the payment method spinner

        // Setup submit button for donation submission
        val submitDonationButton: Button = view.findViewById(R.id.submitDonationButton)
        submitDonationButton.setOnClickListener {
            // Get form data
            val donorName = view.findViewById<EditText>(R.id.donorName).text.toString()// Get donor name
            val donationAmount: Double
            // Determine donation amount based on spinner selection
            if (donationAmountSpinner.selectedItem == "Custom") {
                // Get form data
                donationAmount = customAmountEditText.text.toString().toDoubleOrNull() ?: 0.0 // Get custom amount
            } else {
                donationAmount = donationAmountSpinner.selectedItem.toString().toDoubleOrNull() ?: 0.0// Get predefined amount
            }
            val donorEmail = view.findViewById<EditText>(R.id.donorEmail).text.toString()// Get donor email
            val paymentMethod = paymentMethodSpinner.selectedItem.toString()  // Get selected payment method

            // Use organization name from arguments
            val organizerName = organizationName ?: "Unknown"  // Default to "Unknown" if it's null

            // Insert data into the database if all fields are filled
            if (donorName.isNotEmpty() && donorEmail.isNotEmpty() && donationAmount > 0.0) {
                val result = databaseHelper.insertDonation(donorName, donationAmount, donorEmail, paymentMethod, organizerName) // Insert donation record
                if (result != -1L) {
                    Toast.makeText(requireContext(), "Donation submitted successfully!", Toast.LENGTH_SHORT).show()// Show success message
                    view.findViewById<EditText>(R.id.donorName).setText("")
                    view.findViewById<EditText>(R.id.donorEmail).setText("")
                     view.findViewById<EditText>(R.id.customDonationAmount).setText("")
                     paymentMethodSpinner.setSelection(0)  // Reset payment method spinner
                    donationAmountSpinner.setSelection(0)// Reset payment method spinner
                } else {
                    Toast.makeText(requireContext(), "Failed to submit donation", Toast.LENGTH_SHORT).show()// Show failure message
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()// Prompt user to fill all fields
            }
        }

        return view
    }
}
