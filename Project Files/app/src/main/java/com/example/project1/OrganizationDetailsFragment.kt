package com.example.project1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class OrganizationDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_organization_details, container, false)

        val organizationName = arguments?.getString("name")
        val organizationDescription = arguments?.getString("description")
        val organizationIconResId = arguments?.getInt("icon")

        val nameTextView: TextView = view.findViewById(R.id.detailsOrganizationName)
        val descriptionTextView: TextView = view.findViewById(R.id.detailsOrganizationDescription)
        val iconImageView: ImageView = view.findViewById(R.id.detailsOrganizationIcon)

        nameTextView.text = organizationName
        descriptionTextView.text = organizationDescription
        organizationIconResId?.let { iconImageView.setImageResource(it) }

        // Set up back button
        val backButton: ImageButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Get the "Involvement Areas" TextView
        val involvementAreasTextView: TextView = view.findViewById(R.id.detailsInvolvementAreas)

        // Determine the involvement area based on the organization's focus
        val involvementArea = when (organizationName) {
            "Malaysian Relief Agency" ->
                "       The Malaysian Relief Agency (MRA) " +
                        "is a humanitarian organization" +
                        " dedicated to providing aid and support to" +
                        " communities affected by natural disasters and " +
                        "armed conflicts both within Malaysia and internationally." +
                        " MRA focuses on addressing critical needs such as medical care," +
                        " shelter, clean water, and food security, while also working towards" +
                        " long-term recovery and community development. Involvement Areas:" +
                        " Medical and Health Care, Community Development, Shelter, WASH, Education," +
                        " Economic Empowerment, Training, Livelihood & Food Security."
            "Fire Rescue Malaysia" -> "     Fire Rescue Malaysia is the primary" +
                    " agency responsible for fire and rescue operations," +
                    " emergency medical services, and search and rescue " +
                    "missions across the country. With a commitment to public " +
                    "safety, they work tirelessly to protect lives and property," +
                    " responding to emergencies with professionalism and dedication." +
                    " Involvement Areas: Fire and Rescue Operations, " +
                    "Emergency Medical Services, Search and Rescue," +
                    " Fire Prevention and Safety Education."

            "Clean Air Group" ->
                "       The Clean Air Group is a non-profit organization" +
                        " dedicated to improving air quality and " +
                        "protecting public health. They work through research," +
                        " advocacy, and community engagement to raise awareness" +
                        " about air pollution, promote sustainable practices, and" +
                        " advocate for policies that reduce air pollution and its" +
                        " harmful effects. Involvement Areas: Air Quality Monitoring " +
                        "and Research, Public Awareness and Education, Advocacy and " +
                        "Policy Recommendations, Community Outreach Programs."
            "Hope Foundation" -> "      Hope Foundation is a" +
                    " humanitarian organization committed to " +
                    "alleviating poverty and empowering vulnerable" +
                    " communities. They focus on providing education," +
                    " healthcare, and livelihood support to those in need," +
                    " with a particular emphasis on disaster relief and recovery" +
                    " efforts. Involvement Areas: Disaster Relief and Recovery," +
                    " Community Development, Education and Training, Healthcare" +
                    " and Nutrition, Livelihood Support."
            else -> "N/A"
        }

        // Set the text in the TextView
        involvementAreasTextView.text = involvementArea

        return view
    }
}