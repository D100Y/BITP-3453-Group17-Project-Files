package com.example.project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrganizationAdapter(
    private var organizations: List<Organization>,
    private val onDetailsClick: (Organization) -> Unit // Pass the click listener as a lambda
) : RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_organization, parent, false)
        return OrganizationViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val organization = organizations[position]
        holder.bind(organization, onDetailsClick) // Pass the listener to the ViewHolder
    }

    override fun getItemCount(): Int = organizations.size

    fun updateData(newList: List<Organization>) {
        organizations = newList
        notifyDataSetChanged()
    }

    class OrganizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val organizationIcon: ImageView = itemView.findViewById(R.id.organizationIcon)
        private val organizationName: TextView = itemView.findViewById(R.id.organizationName)
        private val organizationDescription: TextView = itemView.findViewById(R.id.organizationDescription)
        private val viewDetailsButton: Button = itemView.findViewById(R.id.viewDetailsButton)

        fun bind(organization: Organization, onDetailsClick: (Organization) -> Unit) {
            organizationName.text = organization.name
            organizationDescription.text = organization.description
            organizationIcon.setImageResource(organization.iconResId)

            viewDetailsButton.setOnClickListener {
                onDetailsClick(organization) // Call the listener when the button is clicked
            }
        }
    }
}
