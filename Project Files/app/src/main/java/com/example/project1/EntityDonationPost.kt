package com.example.project1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donation_posts")
data class EntityDonationPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary key with auto increment
    @ColumnInfo(name = "organization_name") val organizationName: String,
    @ColumnInfo(name = "event_type") val eventType: String,
    @ColumnInfo(name = "event_title") val eventTitle: String,
    @ColumnInfo(name = "event_date") val eventDate: String,
    @ColumnInfo(name = "event_description") val eventDescription: String,
    @ColumnInfo(name = "necessary_materials") val necessaryMaterials: String,
    @ColumnInfo(name = "contact_info") val contactInfo: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "event_image_res_id") val eventImageResId: Int,
    @ColumnInfo(name = "is_loading") val isLoading: Boolean
)
