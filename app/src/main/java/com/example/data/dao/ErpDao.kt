package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ErpDao {
    // --- Company ---
    @Query("SELECT * FROM companies")
    fun getAllCompanies(): Flow<List<Company>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompany(company: Company): Long

    // --- Employee ---
    @Query("SELECT * FROM employees")
    fun getAllEmployees(): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE id = :id")
    suspend fun getEmployeeById(id: Int): Employee?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee): Long

    @Query("UPDATE employees SET currentSales = currentSales + :saleAmount WHERE id = :id")
    suspend fun addEmployeeSales(id: Int, saleAmount: Double)

    // --- Order ---
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Query("UPDATE orders SET status = :status, asmId = :asmId, dealerId = :dealerId WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String, asmId: Int?, dealerId: Int?)

    // --- BulkProcurement ---
    @Query("SELECT * FROM bulk_procurements ORDER BY timestamp DESC")
    fun getAllBulkProcurements(): Flow<List<BulkProcurement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBulkProcurement(bulk: BulkProcurement): Long

    @Query("UPDATE bulk_procurements SET status = :status WHERE id = :id")
    suspend fun updateBulkStatus(id: Int, status: String)

    // --- Attendance ---
    @Query("SELECT * FROM attendances")
    fun getAllAttendances(): Flow<List<Attendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance): Long

    // --- GPS Breadcrumb ---
    @Query("SELECT * FROM gps_breadcrumbs WHERE employeeId = :employeeId ORDER BY timestamp ASC")
    fun getBreadcrumbsForEmployee(employeeId: Int): Flow<List<GpsBreadcrumb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreadcrumb(crumb: GpsBreadcrumb): Long

    // --- BroadcastCampaign ---
    @Query("SELECT * FROM broadcast_campaigns ORDER BY timestamp DESC")
    fun getAllCampaigns(): Flow<List<BroadcastCampaign>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: BroadcastCampaign): Long

    // --- Payroll ---
    @Query("SELECT * FROM payrolls")
    fun getAllPayrolls(): Flow<List<Payroll>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayroll(payroll: Payroll): Long

    // --- DealerCredit ---
    @Query("SELECT * FROM dealer_credits")
    fun getAllDealerCredits(): Flow<List<DealerCredit>>

    @Query("SELECT * FROM dealer_credits WHERE id = :id")
    suspend fun getDealerCreditById(id: Int): DealerCredit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDealerCredit(credit: DealerCredit): Long

    @Query("UPDATE dealer_credits SET currentDue = currentDue + :amount WHERE id = :id")
    suspend fun adjustDealerDue(id: Int, amount: Double)

    @Query("UPDATE dealer_credits SET isFrozen = :isFrozen WHERE id = :id")
    suspend fun updateDealerFrozen(id: Int, isFrozen: Boolean)

    // --- PaymentAudit ---
    @Query("SELECT * FROM payment_audits ORDER BY timestamp DESC")
    fun getAllPaymentAudits(): Flow<List<PaymentAudit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentAudit(audit: PaymentAudit): Long

    @Query("UPDATE payment_audits SET status = :status, auditNotes = :notes WHERE id = :id")
    suspend fun updatePaymentAuditStatus(id: Int, status: String, notes: String?)

    // --- MaterialSourcing ---
    @Query("SELECT * FROM material_sourcings ORDER BY timestamp DESC")
    fun getAllSourcingLogs(): Flow<List<MaterialSourcing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSourcingLog(log: MaterialSourcing): Long

    // --- ProductionBatch ---
    @Query("SELECT * FROM production_batches ORDER BY timestamp DESC")
    fun getAllProductionBatches(): Flow<List<ProductionBatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductionBatch(batch: ProductionBatch): Long

    @Query("UPDATE production_batches SET isReceived = :isReceived WHERE id = :id")
    suspend fun updateBatchReceived(id: Int, isReceived: Boolean)
}
