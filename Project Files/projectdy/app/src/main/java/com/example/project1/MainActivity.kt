package com.example.project1

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment


// Main activity that serves as the entry point for the application
class MainActivity : AppCompatActivity() {
    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Set the content view to the activity layout

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)// Find the BottomNavigationView

        // Default fragment (Home or whichever you want to show initially)
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment()) // Assuming you have HomeFragment
        }

        // Set listener for item selection in the BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    item.icon?.setTint(ContextCompat.getColor(this, R.color.selector_icon_color))// Change icon color
                    replaceFragment(HomeFragment()) // Show HomeFragment
                }
                R.id.nav_organize -> {
                    item.icon?.setTint(ContextCompat.getColor(this, R.color.selector_icon_color))// Change icon color
                    replaceFragment(OrganizationFragment()) // Show OrganizationFragment
                }
                R.id.profile -> {
                    item.icon?.setTint(ContextCompat.getColor(this, R.color.selector_icon_color))// Change icon color
                    replaceFragment(CenterFragment()) // Show ProfileFragment (you can create this similarly)
                }
            }
            handleBubbleEffect(item) // Call bubble effect here
            true // Return true to indicate the item selection was handled
        }

    }

    // Function to replace fragments in the main content area
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContent, fragment) // Replace the fragment container (FrameLayout)
            .commit()// Commit the transaction
    }

    // Function to handle bubble effect animation on the selected menu item
    private fun handleBubbleEffect(item: MenuItem) {
        // Get the view associated with the menu item (the icon view)
        val menuView: View? = item.actionView

        // Safely handle the possibility of null
        menuView?.let {
            // Create a scale-up animation
            val scaleUp = ScaleAnimation(
                1f, 1.3f,// Scale up from 1 to 1.3
                1f, 1.3f, // Scale up from 1 to 1.3
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // Pivot point for scaling (center)
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f // Pivot point for scaling (center)
            )
            scaleUp.duration = 200// Duration of the scale-up animation
            scaleUp.fillAfter = true// Keep the view scaled up after the animation

            // Create a scale-down animation
            val scaleDown = ScaleAnimation(
                1.3f, 1f,  // Scale down from 1.3 to 1
                1.3f, 1f, // Scale down from 1.3 to 1
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,// Pivot point for scaling (center)
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f// Pivot point for scaling (center)
            )
            scaleDown.startOffset = 200 // Start the scale-down animation after the scale-up
            scaleDown.duration = 100 // Duration of the scale-down animation

            // Apply the animations to the menu item's icon view
            it.startAnimation(scaleUp) // Start the scale-up animation
            it.startAnimation(scaleDown) // Start the scale-down animation
        }
    }
}
