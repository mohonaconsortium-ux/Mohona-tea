package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ErpDao
import com.example.data.model.*

@Database(
    entities = [
        Company::class,
        Employee::class,
        Order::class,
        BulkProcurement::class,
        Attendance::class,
        GpsBreadcrumb::class,
        BroadcastCampaign::class,
        Payroll::class,
        DealerCredit::class,
        PaymentAudit::class,
        MaterialSourcing::class,
        ProductionBatch::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun erpDao(): ErpDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mohona_erp_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
