package com.example.project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat


// Adapter class for displaying a list of organizations in a RecyclerView
class OrganizationAdapter(
    private var organizations: List<Organization>, // List of organizations to display
    private val onDetailsClick: (Organization) -> Unit // Callback for when the details button is clicked
) : RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder>() {

    // Creates new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_organization, parent, false)
        return OrganizationViewHolder(view) // Return a new ViewHolder instance
    }

    // Replaces the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val organization = organizations[position] // Get the organization at the current position
        holder.bind(organization, onDetailsClick) // Bind the organization data to the ViewHolder
    }
    // Returns the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int = organizations.size// Return the number of organizations
    // Update the list of organizations and notify the adapter
    fun updateData(newList: List<Organization>) {
        organizations = newList
        notifyDataSetChanged()
    }
    // ViewHolder class to hold the views for each organization item
    class OrganizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val organizationIcon: ImageView = itemView.findViewById(R.id.organizationIcon)
        private val organizationName: TextView = itemView.findViewById(R.id.organizationName)
        private val organizationDescription: TextView = itemView.findViewById(R.id.organizationDescription)
        private val totalDonation: TextView = itemView.findViewById(R.id.total_doanation)
        private val viewDetailsButton: Button = itemView.findViewById(R.id.viewDetailsButton)
        // Bind the organization data to the views
        fun bind(organization: Organization, onDetailsClick: (Organization) -> Unit) {
            organizationName.text = organization.name
            organizationDescription.text = organization.description
            organizationIcon.setImageResource(organization.iconResId)

            // Fetch total donations from the database
            val dbHelper = DatabaseHelper(itemView.context)
            val totalDonations = dbHelper.getTotalDonationsForOrganizer(organization.name)

            // Format the total donations as currency
            val numberFormat = NumberFormat.getCurrencyInstance()
            numberFormat.maximumFractionDigits = 2
            // Format the total donations as RM
            numberFormat.currency = java.util.Currency.getInstance("MYR")
            val formattedTotal = numberFormat.format(totalDonations)
            //val formattedTotal = NumberFormat.getCurrencyInstance().format(totalDonations)
            totalDonation.text = "Total Donation: $formattedTotal"


            // Handle "Details" button click
            val detailsButton: Button = itemView.findViewById(R.id.DetailsButton)
            detailsButton.setOnClickListener {
                // Fetch donor details from the database
                val donors = dbHelper.getDonorsByOrganizer(organization.name)

                // Inflate the custom dialog layout
                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_donor_details, null)

                // Set the organization name in the dialog
                val organizationNameTextView: TextView = dialogView.findViewById(R.id.organizationNameText)
                organizationNameTextView.text = "Organization: ${organization.name}"
                organizationNameTextView.typeface = ResourcesCompat.getFont(itemView.context, R.font.roboto) // For custom font

                // Populate the donor details in the dialog
                val donorDetailsTextView: TextView = dialogView.findViewById(R.id.donorDetails)
                donorDetailsTextView.typeface = ResourcesCompat.getFont(itemView.context, R.font.roboto) // For custom font
                val donorDetails = donors.joinToString("\n") { donor ->
                    "Name: ${donor.donorName}\nEmail: ${donor.donorEmail}\nAmount: RM${donor.donationAmount}\n"
                }
                donorDetailsTextView.text = if (donors.isNotEmpty()) donorDetails else "No donors found for this organizer."

                // Create and show the AlertDialog
                val builder = android.app.AlertDialog.Builder(itemView.context)
                    .setView(dialogView)

                val dialog = builder.create()

                // Set the Close button click listener
                dialogView.findViewById<Button>(R.id.closeButton).setOnClickListener {
                    dialog.dismiss()// Dismiss the dialog when the close button is clicked
                }

                // Show the dialog
                dialog.show()
            }

            // Set up the click listener for the "Donate" button
            viewDetailsButton.setOnClickListener {
                onDetailsClick(organization) // Trigger the callback with the selected organization
            }
        }
    }
}
