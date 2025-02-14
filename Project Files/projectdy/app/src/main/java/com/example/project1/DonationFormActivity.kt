package com.example.project1

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DonationFormActivity : AppCompatActivity() {

    // Declare categoryAmountMap as a class-level variable
    private var categoryAmountMap: List<Triple<String, Int, String>>? = null
    // A map to hold category names and their associated SeekBars
    private val categorySeekBarMap = mutableMapOf<String, SeekBar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donation_page) // Set your XML layout file

        // Retrieve the categoryAmountMap passed from DonationDetailsActivity
        categoryAmountMap = intent.getSerializableExtra("categoryAmountMap") as? List<Triple<String, Int, String>>

        // Check if categoryAmountMap has input
        if (categoryAmountMap != null) {
            Log.d("DonationDetailsActivity", "CategoryAmountMap has input: $categoryAmountMap")
        } else {
            Log.d("DonationDetailsActivity", "CategoryAmountMap is null or empty.")
        }

        // Initialize the UI components
        val backButton: ImageButton = findViewById(R.id.donationBackButton)
        val submitButton: Button = findViewById(R.id.submitButton)
        val seekBarContainer: LinearLayout = findViewById(R.id.seekBarContainer)

        backButton.setOnClickListener {
            finish()
        }

        // Clear any previous SeekBars
        seekBarContainer.removeAllViews()

        // Dynamically create SeekBars if categoryAmountMap is not null
        categoryAmountMap?.let { map ->
            lifecycleScope.launch {
                val db = AppDatabase.getInstance(applicationContext)
                val categoryAmountDao = db.categoryAmountDao()

                for ((category, _, _) in map) {
                    // Retrieve the amount from the database for each category
                    val categoryAmountFromDb = categoryAmountDao.getCategoryAmountByMaterialName(category)

                    if (categoryAmountFromDb != null) {
                        val amountFromDb = categoryAmountFromDb.amount

                        if (amountFromDb == 0) {
                            // If amount is 0, don't display the SeekBar
                            continue  // Skip the rest of this loop iteration, the SeekBar won't be added
                        }

                        val categoryText = TextView(this@DonationFormActivity).apply {
                            text = category
                            textSize = 16f
                        }

                        val amountText = TextView(this@DonationFormActivity).apply {
                            text = "Selected: 0"
                            textSize = 14f
                        }

                        val seekBar = SeekBar(this@DonationFormActivity).apply {
                            max = amountFromDb  // Set the SeekBar's max value from the database
                        }

                        // Store the SeekBar in the map
                        categorySeekBarMap[category] = seekBar

                        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                amountText.text = "Selected: $progress"
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })

                        val seekBarContainerLayout = LinearLayout(this@DonationFormActivity).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(0, 20, 0, 20)
                            addView(categoryText)
                            addView(seekBar)
                            addView(amountText)
                        }

                        // Add the container layout with SeekBar to the seekBarContainer
                        seekBarContainer.addView(seekBarContainerLayout)
                    } else {
                        Log.e("DonationFormActivity", "CategoryAmount not found for category: $category")
                    }
                }
            }
        }

        // Set up the submit button's click event
        submitButton.setOnClickListener {
            val nameInput: EditText = findViewById(R.id.nameInput)
            val phoneInput: EditText = findViewById(R.id.phoneInput)
            val emailInput: EditText = findViewById(R.id.emailInput)

            val name = nameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val email = emailInput.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val donationAmounts = mutableMapOf<String, Int>()
            for (i in 0 until seekBarContainer.childCount) {
                val container = seekBarContainer.getChildAt(i) as LinearLayout
                val categoryText = container.getChildAt(0) as TextView
                val seekBar = container.getChildAt(1) as SeekBar
                val amount = seekBar.progress

                if (amount > 0) {
                    donationAmounts[categoryText.text.toString()] = amount
                }
            }

            if (donationAmounts.isEmpty()) {
                Toast.makeText(this, "Please donate at least one item", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showConfirmationDialog(name, phone, email, donationAmounts)
        }
    }

    private fun showConfirmationDialog(
        name: String,
        phone: String,
        email: String,
        donationAmounts: Map<String, Int>
    ) {
        val message = buildString {
            append("Name: $name\n")
            append("Phone: $phone\n")
            append("Email: $email\n")
            append("Donations:\n")
            donationAmounts.forEach { (category, amount) ->
                append("- $category: $amount\n")
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Confirm Details")
            .setMessage(message)
            .setPositiveButton("Confirm") { _, _ ->
                lifecycleScope.launch {
                    val db = AppDatabase.getInstance(applicationContext)
                    val categoryAmountDao = db.categoryAmountDao()

                    donationAmounts.forEach { (category, amount) ->

                        // Log the category name you're searching for
                        Log.d("DonationFormActivity", "Searching for category: $category")

                        // Retrieve the categoryAmount from categoryAmountMap based on the category name
                        val categoryAmount = categoryAmountMap?.find { it.first == category }

                        if (categoryAmount == null) {
                            Log.e("DonationFormActivity", "Category not found in categoryAmountMap for: $category")
                            return@forEach
                        }

                        // Use the category name to retrieve the categoryAmount from the database
                        val categoryAmountFromDb = categoryAmountDao.getCategoryAmountByMaterialName(categoryAmount.first)

                        // Log the result of the query
                        if (categoryAmountFromDb != null) {
                            Log.d("DonationFormActivity", "Found CategoryAmount from DB: $categoryAmountFromDb")
                        } else {
                            Log.e("DonationFormActivity", "CategoryAmount not found for category: $category")
                            return@forEach
                        }

                        // Insert into EntityDonationDetails if categoryAmount exists
                        val donationDetail = EntityDonationDetails(
                            name = name,
                            phone = phone,
                            email = email,
                            categoryAmountId = categoryAmountFromDb.id, // Use the retrieved categoryAmountId
                            donatedAmount = amount
                        )
                        val donationDetailsDao = db.donationDetailsDao()  // Get the DAO instance
                        donationDetailsDao.insertDonationDetails(donationDetail)
                        Log.d("DonationFormActivity", "Inserted: $donationDetail")

                        // Get the current amount from the database
                        val currentAmount = categoryAmountDao.getTotalAmountByCategoryAmountId(categoryAmountFromDb.id)

                        // Update the category_amounts table with the current amount
                        if (currentAmount != null) {
                            val updatedAmount = currentAmount - amount  // Use 'amount' instead of 'donatedAmount'
                            categoryAmountDao.updateCategoryAmount(categoryAmountFromDb.id, updatedAmount)

                            Log.d("DonationFormActivity", "Updated categoryAmount with current amount: $updatedAmount")
                        } else {
                            Log.e("DonationFormActivity", "Failed to retrieve amount from the database for category: $category")
                        }
                    }
                    // Show success message after all donations have been processed
                    Toast.makeText(this@DonationFormActivity, "Details saved successfully!", Toast.LENGTH_SHORT).show()
                    finish()


                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}
