package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project1.AppDatabase
import com.example.project1.CategoryAmountDao
import com.example.project1.DonationPostDao
import com.example.project1.models.DonationPost
import kotlinx.coroutines.launch
import java.io.Serializable

class DonationDetailsActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var categoryAmountDao: CategoryAmountDao
    private lateinit var donationPostDao: DonationPostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_details)

        // Initialize Room database and DAOs
        db = AppDatabase.getInstance(applicationContext)
        categoryAmountDao = db.categoryAmountDao()
        donationPostDao = db.donationPostDao()

        // Get the DonationPost object passed from the previous activity
        val donationPost = intent.getSerializableExtra("donationPost") as? DonationPost

        // Get the categoryAmountMap passed from the previous activity
        val categoryAmountMap = intent.getSerializableExtra("categoryAmountMap") as? List<Triple<String, Int, String>>

        // Check if categoryAmountMap has input
        if (categoryAmountMap != null && categoryAmountMap.isNotEmpty()) {
            Log.d("DonationDetailsActivity", "CategoryAmountMap has input: $categoryAmountMap")
            // Perform operations with the data
        } else {
            Log.d("DonationDetailsActivity", "CategoryAmountMap is null or empty.")
            // Handle the case where there's no input
        }

        // Use donationPost data
        if (donationPost != null) {
            findViewById<TextView>(R.id.donationDetailsText).text = donationPost.eventDescription
            findViewById<TextView>(R.id.donationDetailsName).text = donationPost.eventTitle
            findViewById<ImageView>(R.id.donationDetailsIcon).setImageResource(donationPost.eventImageResId)
            findViewById<TextView>(R.id.Posted_date).text = donationPost.eventDate

            val materialsString = categoryAmountMap?.joinToString(separator = "\n") { triple ->
                "${triple.first} - ${triple.second} ${triple.third}"
            }
            findViewById<TextView>(R.id.Necessary_Materials).text = materialsString ?: "No materials available"
            findViewById<TextView>(R.id.Contact_info).text = donationPost.contactInfo
        } else {
            Log.e("DonationDetailsActivity", "DonationPost is null!")
        }

        // Back button functionality
        findViewById<ImageButton>(R.id.detailsBackButton).setOnClickListener {
            finish()
        }
        fun DonationPost.toEntityDonationPost(): EntityDonationPost {
            return EntityDonationPost(
                organizationName = this.organizationName,
                eventType = this.eventType,
                eventTitle = this.eventTitle,
                eventDate = this.eventDate,
                eventDescription = this.eventDescription,
                necessaryMaterials = this.necessaryMaterials,
                contactInfo = this.contactInfo,
                status = this.status,
                eventImageResId = this.eventImageResId,
                isLoading = this.isLoading
            )
        }

        fun Triple<String, Int, String>.toEntityCategoryAmount(): EntityCategoryAmount {
            return EntityCategoryAmount(
                materialName = this.first,
                amount = this.second,
                unit = this.third
            )
        }

        // Donate button functionality
        findViewById<Button>(R.id.donateBtn).setOnClickListener {
            if (donationPost != null && categoryAmountMap != null) {
                lifecycleScope.launch {
                    try {
                        // Check for duplicate DonationPost
                        val isDuplicatePost = donationPostDao.isDuplicateDonationPost(donationPost.eventDescription, donationPost.eventDate) > 0
                        if (!isDuplicatePost) {
                            // Insert DonationPost into the database
                            donationPostDao.insertDonationPost(donationPost.toEntityDonationPost())
                            Log.d("DonationDetailsActivity", "DonationPost inserted into the database.")
                        } else {
                            Log.w("DonationDetailsActivity", "Duplicate DonationPost found. Skipping insertion.")
                        }

                        // Check for duplicates in CategoryAmount before inserting
                        categoryAmountMap.forEach { triple ->
                            val isDuplicateCategoryAmount = categoryAmountDao.isDuplicateCategoryAmount(
                                materialName = triple.first,
                                amount = triple.second,
                                unit = triple.third
                            ) > 0
                            if (!isDuplicateCategoryAmount) {
                                val categoryAmount = triple.toEntityCategoryAmount()
                                categoryAmountDao.insertCategoryAmount(categoryAmount)
                                Log.d("DonationDetailsActivity", "CategoryAmount inserted into the database: $categoryAmount")
                            } else {
                                Log.w("DonationDetailsActivity", "Duplicate CategoryAmount found. Skipping insertion: $triple")
                            }
                        }

                        // Navigate to the DonationFormActivity
                        val intent = Intent(this@DonationDetailsActivity, DonationFormActivity::class.java)
                        intent.putExtra("donationPost", donationPost)
                        intent.putExtra("categoryAmountMap", categoryAmountMap as Serializable)
                        startActivity(intent)

                    } catch (e: Exception) {
                        Log.e("DonationDetailsActivity", "Error inserting data: ${e.localizedMessage}", e)
                        Toast.makeText(this@DonationDetailsActivity, "Error saving data. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Data is missing. Please try again.", Toast.LENGTH_SHORT).show()
                Log.e("DonationDetailsActivity", "Missing data: donationPost=$donationPost, categoryAmountMap=$categoryAmountMap")
            }
        }

    }
}
