package com.example.project1

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.*

@Entity(
    tableName = "donation_details",
    foreignKeys = [
        ForeignKey(
            entity = EntityCategoryAmount::class,
            parentColumns = ["id"],
            childColumns = ["categoryAmountId"],
            onDelete = ForeignKey.CASCADE // Optional: Delete donation details if category is deleted
        )
    ],
    indices = [Index(value = ["categoryAmountId"])] // Index for faster foreign key lookup
)
data class EntityDonationDetails(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val email: String,
    @ColumnInfo(name = "categoryAmountId") val categoryAmountId: Int, // Foreign key column
    val donatedAmount: Int
)
