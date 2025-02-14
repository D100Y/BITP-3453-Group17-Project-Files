package com.example.project1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_amounts")
data class EntityCategoryAmount(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary key with auto increment
    @ColumnInfo(name = "material_name") val materialName: String,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "amount") val amount: Int
)
