package com.example.project1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryAmountDao {
    @Insert
    suspend fun insertCategoryAmount(categoryAmount: EntityCategoryAmount)

    @Query("SELECT * FROM category_amounts")
    suspend fun getAllCategoryAmounts(): List<EntityCategoryAmount>

    @Query("SELECT COUNT(*) FROM category_amounts WHERE material_name = :materialName AND amount = :amount AND unit = :unit")
    suspend fun isDuplicateCategoryAmount(materialName: String, amount: Int, unit: String): Int

    @Query("SELECT * FROM category_amounts WHERE material_name = :materialName LIMIT 1")
    suspend fun getCategoryAmountByMaterialName(materialName: String): EntityCategoryAmount?

    @Query("SELECT * FROM category_amounts WHERE id = :id LIMIT 1")
    suspend fun getCategoryAmountById(id: Int): EntityCategoryAmount?

    @Query("SELECT SUM(donatedAmount) FROM donation_details WHERE categoryAmountId = :categoryAmountId")
    suspend fun getTotalDonatedAmount(categoryAmountId: Int): Int?

    @Query("SELECT amount FROM category_amounts WHERE id = :categoryAmountId")
    suspend fun getTotalAmountByCategoryAmountId(categoryAmountId: Int): Int

    @Query("UPDATE category_amounts SET amount = :newAmount WHERE id = :categoryId")
    suspend fun updateCategoryAmount(categoryId: Int, newAmount: Int)
}
