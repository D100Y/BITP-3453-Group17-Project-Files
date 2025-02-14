package com.example.project1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Define Migration from version 1 to version 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the new table for CategoryAmount
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `category_amount` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `material_name` TEXT NOT NULL, 
                `amount` INTEGER NOT NULL, 
                `unit` TEXT NOT NULL
            )
        """)
    }
}

@Database(
    entities = [
        EntityCategoryAmount::class,
        EntityDonationPost::class,
        EntityDonationDetails::class
    ],
    version = 2,  // Make sure the version is set to 2
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryAmountDao(): CategoryAmountDao
    abstract fun donationPostDao(): DonationPostDao
    abstract fun donationDetailsDao(): DonationDetailsDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db"
                )
                    .addMigrations(MIGRATION_1_2)  // Add the migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
