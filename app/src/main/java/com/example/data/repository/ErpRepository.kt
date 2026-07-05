package com.example.data.repository

import com.example.data.dao.ErpDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class ErpRepository(private val erpDao: ErpDao) {
    val companies: Flow<List<Company>> = erpDao.getAllCompanies()
    val employees: Flow<List<Employee>> = erpDao.getAllEmployees()
    val orders: Flow<List<Order>> = erpDao.getAllOrders()
    val bulkProcurements: Flow<List<BulkProcurement>> = erpDao.getAllBulkProcurements()
    val attendances: Flow<List<Attendance>> = erpDao.getAllAttendances()
    val campaigns: Flow<List<BroadcastCampaign>> = erpDao.getAllCampaigns()
    val payrolls: Flow<List<Payroll>> = erpDao.getAllPayrolls()
    val dealerCredits: Flow<List<DealerCredit>> = erpDao.getAllDealerCredits()
    val paymentAudits: Flow<List<PaymentAudit>> = erpDao.getAllPaymentAudits()
    val sourcingLogs: Flow<List<MaterialSourcing>> = erpDao.getAllSourcingLogs()
    val productionBatches: Flow<List<ProductionBatch>> = erpDao.getAllProductionBatches()

    fun getBreadcrumbs(employeeId: Int): Flow<List<GpsBreadcrumb>> =
        erpDao.getBreadcrumbsForEmployee(employeeId)

    suspend fun getEmployeeById(id: Int): Employee? = erpDao.getEmployeeById(id)
    suspend fun getDealerCreditById(id: Int): DealerCredit? = erpDao.getDealerCreditById(id)

    suspend fun insertCompany(company: Company) = erpDao.insertCompany(company)
    suspend fun insertEmployee(employee: Employee) = erpDao.insertEmployee(employee)
    suspend fun insertOrder(order: Order) = erpDao.insertOrder(order)
    suspend fun insertBulkProcurement(bulk: BulkProcurement) = erpDao.insertBulkProcurement(bulk)
    suspend fun insertAttendance(attendance: Attendance) = erpDao.insertAttendance(attendance)
    suspend fun insertBreadcrumb(crumb: GpsBreadcrumb) = erpDao.insertBreadcrumb(crumb)
    suspend fun insertCampaign(campaign: BroadcastCampaign) = erpDao.insertCampaign(campaign)
    suspend fun insertPayroll(payroll: Payroll) = erpDao.insertPayroll(payroll)
    suspend fun insertDealerCredit(credit: DealerCredit) = erpDao.insertDealerCredit(credit)
    suspend fun insertPaymentAudit(audit: PaymentAudit) = erpDao.insertPaymentAudit(audit)
    suspend fun insertSourcingLog(log: MaterialSourcing) = erpDao.insertSourcingLog(log)
    suspend fun insertProductionBatch(batch: ProductionBatch) = erpDao.insertProductionBatch(batch)

    suspend fun updateOrderStatus(orderId: Int, status: String, asmId: Int?, dealerId: Int?) =
        erpDao.updateOrderStatus(orderId, status, asmId, dealerId)

    suspend fun updateBulkStatus(id: Int, status: String) = erpDao.updateBulkStatus(id, status)
    suspend fun adjustDealerDue(id: Int, amount: Double) = erpDao.adjustDealerDue(id, amount)
    suspend fun updateDealerFrozen(id: Int, isFrozen: Boolean) = erpDao.updateDealerFrozen(id, isFrozen)
    suspend fun updatePaymentAuditStatus(id: Int, status: String, notes: String?) =
        erpDao.updatePaymentAuditStatus(id, status, notes)
    suspend fun updateBatchReceived(id: Int, isReceived: Boolean) = erpDao.updateBatchReceived(id, isReceived)
    suspend fun addEmployeeSales(id: Int, amount: Double) = erpDao.addEmployeeSales(id, amount)
}
