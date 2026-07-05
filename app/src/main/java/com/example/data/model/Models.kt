package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class Company(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val code: String
)

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val role: String, // "Owner", "ASM", "SO", "Incharge", "Laborer"
    val companyId: Int,
    val basePay: Double,
    val targetAmount: Double,
    val currentSales: Double,
    val commissionRate: Double,
    val email: String,
    val phone: String,
    val imagePath: String? = null
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyId: Int,
    val retailerName: String,
    val retailerAddress: String,
    val amount: Double,
    val itemsJson: String, // e.g. "Mohona Masala Tea - 50kg"
    val fieldSoId: Int,
    val asmId: Int?,
    val dealerId: Int?,
    val status: String, // "Ordered", "Verified", "FulfillTicket", "Completed"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bulk_procurements")
data class BulkProcurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyId: Int,
    val distributorName: String,
    val quantityKg: Double,
    val unitPrice: Double,
    val status: String, // "Pending", "Approved", "Out_of_Stock", "Fulfilled"
    val warehouseStockAvailableKg: Double,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "attendances")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: Int,
    val checkInTime: Long,
    val checkOutTime: Long? = null,
    val latitude: Double,
    val longitude: Double,
    val isSpoofed: Boolean,
    val wifiIp: String? = null,
    val status: String // "Approved", "Deducted"
)

@Entity(tableName = "gps_breadcrumbs")
data class GpsBreadcrumb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: Int,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "broadcast_campaigns")
data class BroadcastCampaign(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyId: Int,
    val channel: String, // "Robocall_Bengali", "SMS_Bengali", "Email"
    val recipientName: String,
    val messageText: String,
    val recipientPhone: String,
    val status: String, // "Sent", "Pending", "Failed"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "payrolls")
data class Payroll(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: Int,
    val monthString: String,
    val basePay: Double,
    val targetAchievedPercent: Double,
    val commissionEarned: Double,
    val penaltyDeduction: Double,
    val netPaid: Double,
    val paymentStatus: String, // "Disbursed", "Pending_Audit"
    val disputeStatement: String
)

@Entity(tableName = "dealer_credits")
data class DealerCredit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dealerName: String,
    val currentDue: Double,
    val limitDue: Double,
    val debtToSalesRatio: Double, // currentDue / totalSales
    val isFrozen: Boolean
)

@Entity(tableName = "payment_audits")
data class PaymentAudit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dealerId: Int,
    val amount: Double,
    val transactionToken: String,
    val slipPhotoUri: String? = null,
    val status: String, // "Pending Verification", "Approved", "Rejected"
    val auditNotes: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "material_sourcings")
data class MaterialSourcing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materialName: String, // "Raw Leaves", "CMC Thickener", "Packaging Rolls"
    val quantityKg: Double,
    val purchaseCost: Double, // total price of this sourcing lot
    val unitPrice: Double, // calculated purchaseCost / quantityKg
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "production_batches")
data class ProductionBatch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val inputMaterialKg: Double,
    val outputMaterialKg: Double,
    val cycleTimeMinutes: Int = 30, // Default spray/drum dryer batch cycles
    val gatepassNo: String,
    val challanNo: String,
    val isReceived: Boolean, // Received by Central Warehouse Incharge
    val timestamp: Long = System.currentTimeMillis()
)
