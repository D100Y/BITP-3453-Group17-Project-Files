package com.example.project1

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Default fragment (Home or whichever you want to show initially)
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment()) // Assuming you have HomeFragment
        }

        // Set listener for item selection
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    item.icon?.setTint(ContextCompat.getColor(this, R.color.selector_icon_color))
                    replaceFragment(HomeFragment()) // Show HomeFragment
                }
                R.id.nav_organize -> {
                    item.icon?.setTint(ContextCompat.getColor(this, R.color.selector_icon_color))
                    replaceFragment(OrganizationFragment()) // Show OrganizationFragment
                }
                R.id.profile -> {
                    item.icon?.setTint(ContextCompat.getColor(this, R.color.selector_icon_color))
                    replaceFragment(CenterFragment()) // Show ProfileFragment (you can create this similarly)
                }
            }
            handleBubbleEffect(item) // Call bubble effect here
            true
        }

    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContent, fragment) // Replace the fragment container (FrameLayout)
            .commit()
    }

    // Function to handle bubble effect
    private fun handleBubbleEffect(item: MenuItem) {
        // Get the view associated with the menu item (the icon view)
        val menuView: View? = item.actionView

        // Safely handle the possibility of null
        menuView?.let {
            // Create a scale-up animation
            val scaleUp = ScaleAnimation(
                1f, 1.3f, // Scale up
                1f, 1.3f, // Scale up
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            )
            scaleUp.duration = 200
            scaleUp.fillAfter = true

            // Create a scale-down animation
            val scaleDown = ScaleAnimation(
                1.3f, 1f, // Scale down
                1.3f, 1f, // Scale down
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            )
            scaleDown.startOffset = 200
            scaleDown.duration = 100

            // Apply the animations to the menu item's icon view
            it.startAnimation(scaleUp)
            it.startAnimation(scaleDown)
        }
    }
}
