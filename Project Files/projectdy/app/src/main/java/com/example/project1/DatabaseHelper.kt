package com.example.project1
// Import necessary classes for database operations
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.util.Log
// Define a class that extends SQLiteOpenHelper to manage database creation and version management
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Companion object to hold constants for database configuration
    companion object {
        const val DATABASE_NAME = "donations.db"// Name of the database
        const val DATABASE_VERSION = 2  // Increment the version number for upgrades
        const val TABLE_NAME = "donations"// Name of the donations table
        const val COLUMN_ID = "id"// Column for unique ID
        const val COLUMN_DONOR_NAME = "donor_name"// Column for donor's name
        const val COLUMN_DONATION_AMOUNT = "donation_amount" // Column for donation amount
        const val COLUMN_DONOR_EMAIL = "donor_email" // Column for donor's email
        const val COLUMN_PAYMENT_METHOD = "payment_method"// Column for payment method
        const val COLUMN_ORGANIZER_NAME = "organizer_name"  // Column for organizer's name
    }

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        // SQL statement to create the donations table with the necessary columns
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_DONOR_NAME TEXT," +
                "$COLUMN_DONATION_AMOUNT REAL," +
                "$COLUMN_DONOR_EMAIL TEXT," +
                "$COLUMN_PAYMENT_METHOD TEXT," +
                "$COLUMN_ORGANIZER_NAME TEXT)"  // Include organizer_name column
        db.execSQL(CREATE_TABLE)// Execute the SQL statement to create the table
    }
    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade by adding new columns if necessary
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_ORGANIZER_NAME TEXT")// Add organizer_name column
        }
    }

    // Insert donation data into the database
    fun insertDonation(donorName: String, donationAmount: Double, donorEmail: String, paymentMethod: String, organizerName: String): Long {
        val db = writableDatabase // Get writable database instance
        val values = ContentValues() // Create ContentValues object to hold data
        values.put(COLUMN_DONOR_NAME, donorName) // Insert donor name
        values.put(COLUMN_DONATION_AMOUNT, donationAmount) // Insert donation amount
        values.put(COLUMN_DONOR_EMAIL, donorEmail) // Insert donor email
        values.put(COLUMN_PAYMENT_METHOD, paymentMethod) // Insert payment method
        values.put(COLUMN_ORGANIZER_NAME, organizerName)  // Insert organizer name

        // Insert data into the database and return the row ID
        return db.insert(TABLE_NAME, null, values)
    }

    // Get all donations from the database
    fun getAllDonations(): List<String> {
        val db = readableDatabase// Get readable database instance
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)// Query to get all records
        val donationsList = mutableListOf<String>()// List to hold donation records


        // Ensure that the column indexes are valid
        val donorNameIndex = cursor.getColumnIndex(COLUMN_DONOR_NAME)
        val donationAmountIndex = cursor.getColumnIndex(COLUMN_DONATION_AMOUNT)
        val donorEmailIndex = cursor.getColumnIndex(COLUMN_DONOR_EMAIL)
        val paymentMethodIndex = cursor.getColumnIndex(COLUMN_PAYMENT_METHOD)
        val organizerNameIndex = cursor.getColumnIndex(COLUMN_ORGANIZER_NAME)

        // Loop through the cursor to retrieve data
        while (cursor.moveToNext()) {
            val donorName = cursor.getString(donorNameIndex)// Get donor name
            val donationAmount = cursor.getDouble(donationAmountIndex)// Get donation amount
            val donorEmail = cursor.getString(donorEmailIndex) // Get donor email
            val paymentMethod = cursor.getString(paymentMethodIndex)// Get payment method
            val organizerName = cursor.getString(organizerNameIndex)  // Fetch/ Get organizer name
            // Add formatted donation record to the list
            donationsList.add("$donorName - $donationAmount - $donorEmail - $paymentMethod - $organizerName")
        }

        cursor.close()// Close the cursor to free resources
        return donationsList// Return the list of all donations
    }


    // Calculate the total donations for a specific organizer
    fun getTotalDonationsForOrganizer(organizerName: String): Double {
        val db = readableDatabase// Get readable database instance
        var total = 0.0// Initialize total donations
        val query = "SELECT SUM($COLUMN_DONATION_AMOUNT) FROM $TABLE_NAME WHERE $COLUMN_ORGANIZER_NAME = ?" // SQL query to sum donations
        val cursor = db.rawQuery(query, arrayOf(organizerName))// Execute the query with the organizer's name
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0) // Get the total donation amount
        }
        cursor.close() // Close the cursor
        return total // Return the total donations
    }

    // Get a list of donors for a specific organizer
    fun getDonorsByOrganizer(organizerName: String): List<Donor> {
        val db = readableDatabase// Get readable database instance
        val donors = mutableListOf<Donor>() // List to hold donor records
        // Query to get donors associated with the specified organizer
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_DONOR_NAME, COLUMN_DONATION_AMOUNT, COLUMN_DONOR_EMAIL),
            "$COLUMN_ORGANIZER_NAME = ?",
            arrayOf(organizerName),
            null,
            null,
            null
        )
        // Loop through the cursor to retrieve donor data
        while (cursor.moveToNext()) {
            val donorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DONOR_NAME))// Get donor name
            val donationAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DONATION_AMOUNT)) // Get donation amount
            val donorEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DONOR_EMAIL)) // Get donor email
            // Add donor record to the list
            donors.add(Donor(donorName, donationAmount, donorEmail))
        }

        cursor.close() // Close the cursor
        return donors // Return the list of donors
    }

}
