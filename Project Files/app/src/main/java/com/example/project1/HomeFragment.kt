package com.example.project1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.adapters.PostAdapter
import com.example.project1.models.DonationPost

class HomeFragment : Fragment() {

    private lateinit var postAdapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var allPosts: List<DonationPost> = listOf()
    private var filteredPosts: List<DonationPost> = listOf()

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
                eventTitle = "Flood in Jakarta", // Example data
                eventDate = "5 December 2025", // Example data
                eventDescription = "Heavy flooding in Jakarta due to continuous rain.",
                status = "Active",
                eventImageResId = R.drawable.flood, // Example image resource
                isLoading = false
            ),
            DonationPost(
                organizationName = "Malaysian Relief Agency",
                eventType = "Fire",
                eventTitle = "Fire in Melaka",
                eventDate = "15 November 2025",
                eventDescription = "A massive fire broke out in Melaka, assistance needed.",
                status = "Funded",
                eventImageResId = R.drawable.fire, // Example image resource
                isLoading = false
            )
        )

        // Initially show all posts
        filteredPosts = allPosts

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.postList)
        postAdapter = PostAdapter(filteredPosts) { post ->

            // Navigate to DonationDetailsFragment with eventTitle
            navigateToDonationDetailsFragment(post)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postAdapter

        // Setup SearchView
        searchView = view.findViewById(R.id.searchBar)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPosts(newText)
                return true
            }
        })

        return view
    }

    // Function to navigate to DonationDetailsFragment
    private fun navigateToDonationDetailsFragment(post: DonationPost) {
        val donationDetailsFragment = DonationDetailsFragment()

        // Create a bundle to pass the eventTitle and other details
        val bundle = Bundle()
        bundle.putString("eventTitle", post.eventTitle)
        bundle.putString("eventDate", post.eventDate)
        bundle.putString("eventDescription", post.eventDescription)
        bundle.putInt("eventImageResId", post.eventImageResId)

        // Set arguments to the fragment
        donationDetailsFragment.arguments = bundle

        // Navigate to DonationDetailsFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContent, donationDetailsFragment) // Use the correct container view ID
            .addToBackStack(null)
            .commit()
    }

    // Function to filter posts based on query
    private fun filterPosts(query: String?) {
        filteredPosts = if (query.isNullOrEmpty()) {
            allPosts // Show all posts if query is empty
        } else {
            allPosts.filter { post ->
                post.organizationName.contains(query, ignoreCase = true) ||
                        post.eventType.contains(query, ignoreCase = true) ||
                        post.eventDescription.contains(query, ignoreCase = true)
            }
        }
        postAdapter.updatePosts(filteredPosts) // Update the adapter with the filtered list
    }
}
