package com.example.project1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.models.DonationPost

// Adapter class for displaying a list of donation posts in a RecyclerView
class PostAdapter(private var posts: List<DonationPost>, private val onPostClick: (DonationPost) -> Unit) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // ViewHolder class to hold the views for each donation post item
    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val organizationName: TextView = itemView.findViewById(R.id.organizationName) // TextView for organization name
        val eventType: TextView = itemView.findViewById(R.id.eventType) // TextView for event type
        val eventTitle: TextView = itemView.findViewById(R.id.eventTitle) // TextView for event title
        val eventDate: TextView = itemView.findViewById(R.id.eventDate) // TextView for event date
        val eventDescription: TextView = itemView.findViewById(R.id.eventDescription) // TextView for event description
        val statusProgressBar: ProgressBar = itemView.findViewById(R.id.statusProgressBar) // Circular progress bar for status
        val eventImage: ImageView = itemView.findViewById(R.id.eventImage) // ImageView for event image
        val loadingSpinner: ProgressBar = itemView.findViewById(R.id.statusProgressBar) // Loading spinner (if needed)
        val detailsButton: Button = itemView.findViewById(R.id.detailsButton) // Button to view details

        init {
            // Set click listener for the item view
            itemView.setOnClickListener {
                val position = adapterPosition // Get the position of the clicked item
                if (position != RecyclerView.NO_POSITION) {
                    val post = posts[position] // Get the post at the clicked position
                    onPostClick(post) // Call the click listener with the post
                }
            }

            // Set click listener for the details button
            detailsButton.setOnClickListener {
                val position = adapterPosition // Get the position of the clicked item
                if (position != RecyclerView.NO_POSITION) {
                    val post = posts[position] // Get the post at the clicked position
                    onPostClick(post) // Handle Details button click
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_donation_post, parent, false)
        return PostViewHolder(view) // Return a new ViewHolder instance
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position] // Get the post at the current position

        // Bind the post data to the views
        holder.organizationName.text = post.organizationName
        holder.eventType.text = post.eventType
        holder.eventTitle.text = post.eventTitle // Binding eventTitle
        holder.eventDate.text = post.eventDate // Binding eventDate
        // Use the getShortDescription function to shorten the event description to 20 words
        holder.eventDescription.text = getShortDescription(post.eventDescription, 20)
        holder.eventImage.setImageResource(post.eventImageResId) // Setting the image resource ID

        // Show or hide the progress bar based on the post status
        if (post.status == "Loading") {
            holder.statusProgressBar.visibility = View.VISIBLE // Show ProgressBar when loading
            holder.statusProgressBar.isIndeterminate = true // Optional: Indeterminate loading
            holder.loadingSpinner.visibility = View.GONE // Hide loading spinner if necessary
        } else {
            holder.statusProgressBar.visibility = View.GONE // Hide ProgressBar when not loading
            holder.loadingSpinner.visibility = View.GONE // Hide loading spinner if not loading
        }
    }

    // Returns the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int = posts.size // Return the number of posts

    // Method to update the data and refresh the RecyclerView
    fun updatePosts(newPosts: List<DonationPost>) {
        posts = newPosts // Update the posts list
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}

// Function to shorten the description to a max of 20 words and add ellipsis
fun getShortDescription(description: String, maxWords: Int): String {
    val words = description.split(" ") // Split the description into words
    return if (words.size > maxWords) {
        // If the description has more than `maxWords` words, take the first `maxWords` words and append ellipsis
        words.take(maxWords).joinToString(" ") + "..."
    } else {
        // Return the original description if it's within the word limit
        description
    }
}
