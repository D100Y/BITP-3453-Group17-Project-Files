package com.example.project1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DonationPostDao {
    @Insert
    suspend fun insertDonationPost(donationPost: EntityDonationPost)

    @Query("SELECT * FROM donation_posts")
    suspend fun getAllDonationPosts(): List<EntityDonationPost>

    @Query("SELECT COUNT(*) FROM donation_posts WHERE event_description = :eventDescription AND event_date = :eventDate")
    suspend fun isDuplicateDonationPost(eventDescription: String, eventDate: String): Int
}
