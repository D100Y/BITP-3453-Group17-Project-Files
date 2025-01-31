package com.example.project1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
class OrganizationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrganizationAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_organization, container, false)

        recyclerView = view.findViewById(R.id.organizationList)

        // Pass the list and onDetailsClick handler
        adapter = OrganizationAdapter(originalList) { organization ->
            openDetailsFragment(organization) // Handle navigation
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.filterBar)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Flood" -> filterList("Flood")
                    "Fire" -> filterList("Fire")
                    "Haze" -> filterList("Haze")
                    "All" -> filterList("") // Show all organizations
                    else -> adapter.updateData(originalList) // Show all items
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return view
    }

    private fun filterList(type: String) {
        val filteredList = if (type.isEmpty()) {
            originalList // Show all items when "All" is selected
        } else {
            originalList.filter { it.type == type }
        }
        adapter.updateData(filteredList)
    }

    private fun openDetailsFragment(organization: Organization) {
        val fragment = OrganizationDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("name", organization.name)
                putString("description", organization.description)
                putInt("icon", organization.iconResId)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContent, fragment) // Replace with your container ID
            .addToBackStack(null) // Add to the back stack for navigation
            .commit()
    }
}
