package com.example.project1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DonationDetailsDao {

    @Insert
    suspend fun insertDonationDetails(donationDetails: EntityDonationDetails)

    @Query("SELECT * FROM donation_details")
    suspend fun getAllDonationDetails(): List<EntityDonationDetails>
}
