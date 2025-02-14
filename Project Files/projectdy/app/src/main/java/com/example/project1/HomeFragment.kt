package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.project1.adapters.PostAdapter
import com.example.project1.models.DonationPost
import android.util.Log

// Fragment to display a list of donation posts
class HomeFragment : Fragment() {

    private lateinit var postAdapter: PostAdapter // Adapter for the RecyclerView
    private lateinit var recyclerView: RecyclerView // RecyclerView to display the list of posts
    private lateinit var searchView: SearchView // SearchView for filtering posts
    private var allPosts: List<DonationPost> = listOf() // List to hold all donation posts
    private var filteredPosts: List<DonationPost> = listOf() // List to hold filtered donation posts

    // Called to create the view hierarchy associated with the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Sample data (this should be replaced by real data)
        allPosts = listOf(
            DonationPost(
                organizationName = "Indo Relief Agency",
                eventType = "Flood",
                eventTitle = "Flood in Jakarta",  // Example data
                eventDate = "5 December 2025",   // Example data
                eventDescription = "Jakarta is currently facing devastating flooding caused by continuous heavy rain. Many areas in the city are submerged, including residential neighborhoods, roads, and public spaces. The flood has caused significant damage to homes and infrastructure, leaving hundreds of families displaced and in urgent need of assistance. The affected residents require immediate help, including food and clean water Rescue teams are working hard to provide aid, but they need your support to ensure that relief reaches those in need. Please donate to help the flood victims in Jakarta. Your contribution can make a difference in providing essential supplies and helping families rebuild their lives after this disaster. \n\nBelow is the list of materials needed: ",
                necessaryMaterials = "Materials Needed: \n - Mineral water(2000 bottle)\n - Rice(500kg)\n - Sardin(500pcs) \n - Vegetables(500kg)",
                contactInfo = "Contact information:\nName: Mr Tan Chee Keong \nPhone no: 123-456-7890 \nEmail: tancheekeong@gmail.com",
                status = "Active",
                eventImageResId = R.drawable.flood,  // Example image resource
                isLoading = false
            ),
            DonationPost(
                organizationName = "Malaysian Relief Agency",
                eventType = "Fire",
                eventTitle = "Fire in Melaka",
                eventDate = "15 November 2025",
                eventDescription = "A devastating fire has broken out in Melaka, causing widespread destruction. Many homes, businesses, and public facilities have been severely damaged, leaving dozens of families without shelter and basic necessities. The fire has left many people injured and in urgent need of medical assistance. Rescue teams are working tirelessly to control the fire and evacuate the affected residents, but more support is needed to help those in distress. Your generous donations will provide essential supplies like food, clean water, and shelter for those impacted by this disaster. Please donate to help the fire victims in Melaka. Every contribution counts in bringing relief to those affected. \n\nBelow is the list of materials needed: ",
                necessaryMaterials = "Materials Needed: \n - Mineral water(2000 bottles)\n - Rice(500 kg)\n - Canned food(1000 cans)\n - Instant noodles(500 packs)\n - Blankets(500 pcs)\n - Tents(100 pcs)",
                contactInfo = "Contact information:\nName: Mr. Ahmad Zaini \n Phone no: 123-456-7890 \n Email: ahmadzaini@gmail.com",
                status = "Active",
                eventImageResId = R.drawable.fire,  // Example image resource
                isLoading = false
            )

        )

        filteredPosts = allPosts // Initially, show all posts

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.postList) // Find the RecyclerView in the layout
        postAdapter = PostAdapter(filteredPosts) { post -> // Initialize the adapter with the filtered posts
            onPostClick(post) // Set up click listener for each post
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set the layout manager for the RecyclerView
        recyclerView.adapter = postAdapter // Set the adapter for the RecyclerView

        // Setup SearchView
        searchView = view.findViewById(R.id.searchBar) // Find the SearchView in the layout
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // No action needed on query submission
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPosts(newText) // Filter posts based on the search query
                return true
            }
        })

        return view// Return the inflated view
    }

    // Filter the list of posts based on the search query
    private fun filterPosts(query: String?) {
        filteredPosts = if (query.isNullOrEmpty()) {
            allPosts // If the query is empty, show all posts
        } else {
            allPosts.filter { post ->
                // Filter posts based on organization name, event type, or event description
                post.organizationName.contains(query, ignoreCase = true) ||
                        post.eventType.contains(query, ignoreCase = true) ||
                        post.eventDescription.contains(query, ignoreCase = true)
            }
        }
        postAdapter.updatePosts(filteredPosts) // Update the adapter with the filtered list
    }

    fun extractMaterialList(materials: String): List<Triple<String, Int, String>> {
        val materialList = mutableListOf<Triple<String, Int, String>>() // List to hold material details
        val lines = materials.split("\n") // Split the input into lines

        Log.d("HomeFragment", "Input Materials String: $materials")

        for (line in lines) {
            val trimmedLine = line.trim()

            // Log the line being processed
            Log.d("HomeFragment", "Processing line: '$trimmedLine'")

            // Check if the line contains the expected format of material, amount, and unit
            if (trimmedLine.contains("- ")) {
                // Split the line into material and its details by " - "
                val parts = trimmedLine.split("- ")

                // Log the split parts for debugging
                Log.d("HomeFragment", "Parts after splitting: ${parts.joinToString(",")}")

                if (parts.size > 1) {

                    // Regex pattern to match amount and unit inside parentheses
                    val regex = Regex("\\((\\d+)\\s?([a-zA-Z]+)\\)") // Capture number and unit inside parentheses
                    val matchResult = regex.find(parts[1].trim())  // Process the second part after " - "

                    matchResult?.let {
                        // Extract the amount (number) and unit (string after the number inside the parentheses)
                        val amount = it.groupValues[1].toInt()
                        val unit = it.groupValues[2].trim()
                        val materialName = parts[1].trim().replace(Regex("\\(\\d+\\s?[a-zA-Z]+\\)"), "").trim()


                        // Add the material details as a Triple (material name, amount, unit) to the list
                        materialList.add(Triple(materialName, amount, unit))
                        Log.d("HomeFragment", "Added material detail: $materialName, $amount, $unit")
                    } ?: run {
                        Log.d("HomeFragment", "Material detail extraction failed for: $trimmedLine")
                    }
                }
            }
        }
        // Log the final list of materials
        Log.d("HomeFragment", "Final Material List: $materialList")
        return materialList
    }

    // Handle the click on a donation post
    private fun onPostClick(post: DonationPost) {
        // Show a toast message with the organization name of the clicked post
        Toast.makeText(requireContext(), "Clicked: ${post.organizationName}", Toast.LENGTH_SHORT).show()

        // Extract the category and amount map from the necessary materials
        val categoryAmountMap = extractMaterialList(post.necessaryMaterials)

        // Log the map content to Logcat
        Log.d("HomeFragment", "Category Amount Map: $categoryAmountMap")
        // Create an Intent to go to DonationFormActivity (or another target activity)
        val intent = Intent(requireContext(), DonationDetailsActivity::class.java)

        // Pass the entire DonationPost object
        intent.putExtra("donationPost", post)

        // Pass the categoryAmountMap as an extra
        intent.putExtra("categoryAmountMap", categoryAmountMap as java.io.Serializable)

        // Start the target activity
        startActivity(intent)
    }
}
