package com.example.project1

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

// Fragment to display a list of organizations and their donation details
class OrganizationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView // RecyclerView to display the list of organizations
    private lateinit var adapter: OrganizationAdapter // Adapter for the RecyclerView
    private lateinit var dbHelper: DatabaseHelper // Database helper for managing donations
    private lateinit var donationsTable: TableLayout // TableLayout to display donation details

    // Original list of organizations
    private val originalList = listOf(
        Organization(
            "Malaysian Relief Agency",
            "Flood",
            "A trusted organization for flood relief",
            R.drawable.logo_1
        ),
        Organization(
            "Fire Rescue Malaysia",
            "Fire",
            "Focused on fire disaster assistance",
            R.drawable.logo2
        ),
        Organization(
            "Clean Air Group",
            "Haze",
            "Helping areas affected by haze",
            R.drawable.logo_3
        ),
        Organization(
            "Hope Foundation",
            "Flood",
            "Providing flood relief services across Malaysia",
            R.drawable.logo_4
        )
    )

    // Called to create the view hierarchy associated with the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_organization, container, false)

        // Initialize DatabaseHelper to fetch data from the database
        dbHelper = DatabaseHelper(requireContext())

        // Get the TableLayout from the layout
        donationsTable = view.findViewById(R.id.donationsTable)

        // Get all donations from the database
        val donations = dbHelper.getAllDonations()

        // Populate TableLayout with donations
        populateDonationsTable(donations)

        // Set up RecyclerView to display organizations
        recyclerView = view.findViewById(R.id.organizationList)
        adapter = OrganizationAdapter(originalList) { organization ->
            openDetailsFragment(organization) // Handle navigation to details fragment
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set layout manager for RecyclerView
        recyclerView.adapter = adapter // Set the adapter for the RecyclerView

        // Set up tab filtering for organization types
        val tabLayout = view.findViewById<TabLayout>(R.id.filterBar)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Flood" -> filterList("Flood") // Filter for Flood organizations
                    "Fire" -> filterList("Fire") // Filter for Fire organizations
                    "Haze" -> filterList("Haze") // Filter for Haze organizations
                    "All" -> filterList("") // Show all organizations
                    else -> adapter.updateData(originalList) // Show all items if no specific tab is selected
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return view // Return the inflated view
    }

    // Function to filter organizations based on type (Flood, Fire, Haze)
    private fun filterList(type: String) {
        val filteredList = if (type.isEmpty()) {
            originalList // Show all items when "All" is selected
        } else {
            originalList.filter { it.type == type } // Filter based on the selected type
        }
        adapter.updateData(filteredList) // Update the adapter with the filtered list
    }

    // Navigate to Organization Details Fragment
    private fun openDetailsFragment(organization: Organization) {
        val fragment = OrganizationDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("name", organization.name) // Pass organization name to the details fragment
                putString("description", organization.description) // Pass organization description
                putInt("icon", organization.iconResId) // Pass organization icon resource ID
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContent, fragment) // Replace the current fragment with the details fragment
            .addToBackStack(null) // Add to the back stack for navigation
            .commit() // Commit the transaction
    }

    // Populate the donations table with donation data
    private fun populateDonationsTable(donations: List<String>) {
        // Clear any existing rows before adding new ones
        donationsTable.removeAllViews()

        // Create a TableRow for the header (column names)
        val headerRow = TableRow(requireContext())
        headerRow.setPadding(8, 8, 8, 8)

        // Create and add Donor Name header TextView
        val donorNameHeaderTextView = TextView(requireContext())
        donorNameHeaderTextView.text = "Donor Name"
        donorNameHeaderTextView.setPadding(8, 8, 10, 8)
        donorNameHeaderTextView.setTextColor(resources.getColor(android.R.color.black))
        donorNameHeaderTextView.setTypeface(null, Typeface.BOLD) // Make it bold
        headerRow.addView(donorNameHeaderTextView)

        // Create and add Donation Amount header TextView
        val donationAmountHeaderTextView = TextView(requireContext())
        donationAmountHeaderTextView.text = "Donation Amount"
        donationAmountHeaderTextView.setPadding(8, 8, 8, 8)
        donationAmountHeaderTextView.setTextColor(resources.getColor(android.R.color.black))
        donationAmountHeaderTextView.setTypeface(null, Typeface.BOLD) // Make it bold
        headerRow.addView(donationAmountHeaderTextView)

        // Add the header row to the TableLayout
        donationsTable.addView(headerRow)

        // Loop through each donation and add a TableRow for data
        for (donation in donations) {
            val data = donation.split(" - ")
            if (data.size >= 2) {
                val donorName = data[0]
                val donationAmount = data[1]

                // Create a new TableRow for the donation data
                val row = TableRow(requireContext())
                row.setPadding(8, 8, 10, 8)

                // Create and add donor name TextView
                val donorNameTextView = TextView(requireContext())
                donorNameTextView.text = donorName
                donorNameTextView.setPadding(8, 8, 8, 8)
                row.addView(donorNameTextView)

                // Create and add donation amount TextView
                val donationAmountTextView = TextView(requireContext())
                donationAmountTextView.text = donationAmount
                donationAmountTextView.setPadding(8, 8, 8, 8)
                row.addView(donationAmountTextView)

                // Add the TableRow to the TableLayout
                donationsTable.addView(row)
            }
        }
    }
}