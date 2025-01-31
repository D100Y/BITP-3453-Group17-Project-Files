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

class PostAdapter(
    private var posts: List<DonationPost>,
    private val onPostClick: (DonationPost) -> Unit // Listener for handling clicks
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val organizationName: TextView = itemView.findViewById(R.id.organizationName)
        val eventType: TextView = itemView.findViewById(R.id.eventType)
        val eventTitle: TextView = itemView.findViewById(R.id.eventTitle)
        val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        val eventDescription: TextView = itemView.findViewById(R.id.eventDescription)
        val statusProgressBar: ProgressBar = itemView.findViewById(R.id.statusProgressBar) // Circular progress bar
        val eventImage: ImageView = itemView.findViewById(R.id.eventImage)
        val loadingSpinner: ProgressBar = itemView.findViewById(R.id.statusProgressBar)
        val detailsButton: Button = itemView.findViewById(R.id.detailsButton)

        init {
            // Handle itemView click
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = posts[position]
                    onPostClick(post) // Pass the clicked post to the listener
                }
            }

            // Handle "Details" button click
            detailsButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = posts[position]
                    onPostClick(post) // Pass the clicked post to the listener
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_donation_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        // Bind data to the views
        holder.organizationName.text = post.organizationName
        holder.eventType.text = post.eventType
        holder.eventTitle.text = post.eventTitle
        holder.eventDate.text = post.eventDate
        holder.eventDescription.text = post.eventDescription
        holder.eventImage.setImageResource(post.eventImageResId)

        // Handle progress bar visibility based on status
        if (post.status == "Loading") {
            holder.statusProgressBar.visibility = View.VISIBLE
            holder.statusProgressBar.isIndeterminate = true
            holder.loadingSpinner.visibility = View.GONE
        } else {
            holder.statusProgressBar.visibility = View.GONE
            holder.loadingSpinner.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = posts.size

    // Method to update the list of posts and refresh the RecyclerView
    fun updatePosts(newPosts: List<DonationPost>) {
        posts = newPosts
        notifyDataSetChanged() // Notify the adapter of the data change
    }
}
