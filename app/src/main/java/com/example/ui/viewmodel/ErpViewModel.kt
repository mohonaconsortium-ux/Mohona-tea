package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiVoiceService
import com.example.data.db.AppDatabase
import com.example.data.model.*
import com.example.data.repository.ErpRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject

class ErpViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = ErpRepository(db.erpDao())

    // --- Active User Context ---
    val selectedCompanyId = MutableStateFlow(1) // 1: Mohona Tea & Co, 2: Mohona Masala Food
    val selectedUserRole = MutableStateFlow("Owner") // "Owner", "ASM", "SO", "Incharge", "Laborer"
    val selectedEmployeeId = MutableStateFlow(1)

    // --- Observe Database State ---
    val companies = repository.companies.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val employees = repository.employees.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val orders = repository.orders.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val bulkProcurements = repository.bulkProcurements.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val attendances = repository.attendances.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val campaigns = repository.campaigns.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val payrolls = repository.payrolls.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val dealerCredits = repository.dealerCredits.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val paymentAudits = repository.paymentAudits.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val sourcingLogs = repository.sourcingLogs.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val productionBatches = repository.productionBatches.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Voice Invoicing States ---
    val invoiceItem = MutableStateFlow("Mohona Masala Tea")
    val invoiceQuantity = MutableStateFlow(50.0)
    val invoiceUnitPrice = MutableStateFlow(280.0)
    val invoiceDiscount = MutableStateFlow(5.0)
    val invoiceNotes = MutableStateFlow("Tap 'Speech Input' or write a prompt below")
    val isProcessingVoice = MutableStateFlow(false)

    // --- BI States ---
    val biChatQuery = MutableStateFlow("")
    val biChatResponse = MutableStateFlow("স্বাগতম! মোহনা ককপিটে আপনার প্রশ্ন টাইপ করুন বা নিচের স্ট্র্যাটেজি কার্ডগুলো দেখুন।")
    val isProcessingBI = MutableStateFlow(false)

    // Actionable strategy prescription cards
    val strategyCards = MutableStateFlow(
        listOf(
            "বরিশাল জোন: ক্যারিং খরচের প্রফিট ড্রপ ফ্যাক্টর অনুসন্ধান করুন।",
            "ডিলার বকেয়া: বকেয়া সীমা অতিক্রমকারী ৩টি অ্যাকাউন্ট ফ্রিজ করা হয়েছে।",
            "উৎপাদন ব্যালেন্স: গড় মূল্য ২৪৫ টাকা (কাঁচামালের মূল্য হ্রাসের চমৎকার সুযোগ)।"
        )
    )

    init {
        // Seed default sandbox data if the database is currently empty
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            seedInitialData()
        }
    }

    private suspend fun seedInitialData() {
        // Check if database contains companies, if not, seed
        val existingCos = db.erpDao().getAllCompanies().first()
        if (existingCos.isEmpty()) {
            // Seed Companies
            val co1Id = db.erpDao().insertCompany(Company(name = "Mohona Tea & Co.", code = "MTC"))
            val co2Id = db.erpDao().insertCompany(Company(name = "Mohona Masala & Food", code = "MMF"))

            // Seed Employees (Owner, ASM, SO, Incharge, Laborer)
            db.erpDao().insertEmployee(Employee(name = "Mr. Mohsin Khan", role = "Owner", companyId = co1Id.toInt(), basePay = 150000.0, targetAmount = 500000.0, currentSales = 0.0, commissionRate = 0.05, email = "mohsin@mohona.com", phone = "+8801711122233"))
            val asmId = db.erpDao().insertEmployee(Employee(name = "Rashedul Bari", role = "ASM", companyId = co1Id.toInt(), basePay = 60000.0, targetAmount = 300000.0, currentSales = 320000.0, commissionRate = 0.03, email = "rashed@mohona.com", phone = "+8801722233344"))
            val soId = db.erpDao().insertEmployee(Employee(name = "Sujon Ahmed", role = "SO", companyId = co1Id.toInt(), basePay = 25000.0, targetAmount = 150000.0, currentSales = 120000.0, commissionRate = 0.02, email = "sujon@mohona.com", phone = "+8801733344455"))
            db.erpDao().insertEmployee(Employee(name = "Abdur Rahman", role = "Incharge", companyId = co1Id.toInt(), basePay = 40000.0, targetAmount = 0.0, currentSales = 0.0, commissionRate = 0.0, email = "rahman@mohona.com", phone = "+8801744455566"))
            db.erpDao().insertEmployee(Employee(name = "Habib Miah", role = "Laborer", companyId = co1Id.toInt(), basePay = 18000.0, targetAmount = 0.0, currentSales = 0.0, commissionRate = 0.0, email = "habib@mohona.com", phone = "+8801755566677"))

            // Seed Dealer Credits
            db.erpDao().insertDealerCredit(DealerCredit(dealerName = "Chittagong Tea Depot", currentDue = 80000.0, limitDue = 300000.0, debtToSalesRatio = 0.2, isFrozen = false))
            db.erpDao().insertDealerCredit(DealerCredit(dealerName = "Barisal Mohona Store", currentDue = 245000.0, limitDue = 250000.0, debtToSalesRatio = 0.95, isFrozen = false))
            db.erpDao().insertDealerCredit(DealerCredit(dealerName = "Sylhet Wholesale Agency", currentDue = 320000.0, limitDue = 300000.0, debtToSalesRatio = 1.1, isFrozen = true))

            // Seed Materials Sourcing (Initial costs for Moving Average)
            db.erpDao().insertSourcingLog(MaterialSourcing(materialName = "Raw Leaves", quantityKg = 1000.0, purchaseCost = 220000.0, unitPrice = 220.0))
            db.erpDao().insertSourcingLog(MaterialSourcing(materialName = "CMC Thickener", quantityKg = 100.0, purchaseCost = 45000.0, unitPrice = 450.0))
            db.erpDao().insertSourcingLog(MaterialSourcing(materialName = "Packaging Rolls", quantityKg = 50.0, purchaseCost = 15000.0, unitPrice = 300.0))

            // Seed Production Batches
            db.erpDao().insertProductionBatch(ProductionBatch(inputMaterialKg = 60.0, outputMaterialKg = 58.5, gatepassNo = "GP-2026-001", challanNo = "CH-3091", isReceived = true))
            db.erpDao().insertProductionBatch(ProductionBatch(inputMaterialKg = 60.0, outputMaterialKg = 57.0, gatepassNo = "GP-2026-002", challanNo = "CH-3092", isReceived = false))

            // Seed active order
            db.erpDao().insertOrder(Order(companyId = co1Id.toInt(), retailerName = "Rahim Grocery", retailerAddress = "Chawkbazar, Barisal", amount = 14000.0, itemsJson = "Mohona Masala Tea - 50kg", fieldSoId = soId.toInt(), asmId = null, dealerId = null, status = "Ordered"))
        }
    }

    // --- MODULE 1: sales / Retail chain ---
    fun logOrder(retailer: String, address: String, item: String, qty: Double, discount: Double, unitPrice: Double = 280.0) {
        viewModelScope.launch {
            val amount = (qty * unitPrice) * (1.0 - (discount / 100.0))
            val newOrder = Order(
                companyId = selectedCompanyId.value,
                retailerName = retailer,
                retailerAddress = address,
                amount = amount,
                itemsJson = "$item - ${qty}kg",
                fieldSoId = selectedEmployeeId.value,
                asmId = null,
                dealerId = null,
                status = "Ordered"
            )
            repository.insertOrder(newOrder)
            // Increments order sales of the SO
            repository.addEmployeeSales(selectedEmployeeId.value, amount)
        }
    }

    fun verifyAndRouteOrder(orderId: Int, dealerId: Int) {
        viewModelScope.launch {
            // Step 2 & 3: ASM verifys and routes to Nearest Local Dealer
            val asmId = selectedEmployeeId.value
            repository.updateOrderStatus(orderId, "FulfillTicket", asmId, dealerId)
        }
    }

    fun completeFulfillment(orderId: Int) {
        viewModelScope.launch {
            // Step 3: Local Dealer fulfills, order completes, flushes to Owner's Admin Panel
            repository.updateOrderStatus(orderId, "Completed", null, null)
        }
    }

    fun submitBulkProcurement(distributor: String, quantityKg: Double, unitPrice: Double) {
        viewModelScope.launch {
            // Bulk Dealer Pipeline
            val stockAvailable = 500.0 // simulated warehouse inventory limit
            val status = if (stockAvailable >= quantityKg) "Approved" else "Out_of_Stock"
            val bulk = BulkProcurement(
                companyId = selectedCompanyId.value,
                distributorName = distributor,
                quantityKg = quantityKg,
                unitPrice = unitPrice,
                status = status,
                warehouseStockAvailableKg = stockAvailable
            )
            repository.insertBulkProcurement(bulk)
        }
    }

    fun verifyBulkProcurement(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateBulkStatus(id, status)
        }
    }

    // --- MODULE 2: CRM & GEOTAGGED MAPS ---
    fun logCheckIn(latitude: Double, longitude: Double, isSpoofed: Boolean, wifiIp: String?) {
        viewModelScope.launch {
            // Enforce GPS background check
            val empId = selectedEmployeeId.value
            val emp = repository.getEmployeeById(empId) ?: return@launch
            val status = if (isSpoofed) "Deducted" else "Approved"

            val attendance = Attendance(
                employeeId = empId,
                checkInTime = System.currentTimeMillis(),
                latitude = latitude,
                longitude = longitude,
                isSpoofed = isSpoofed,
                wifiIp = wifiIp,
                status = status
            )
            repository.insertAttendance(attendance)

            // Log current geofenced trace breadcrumb
            repository.insertBreadcrumb(
                GpsBreadcrumb(
                    employeeId = empId,
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }
    }

    fun broadcastMarketingCampaign(channel: String, recipientName: String, text: String, phone: String) {
        viewModelScope.launch {
            val camp = BroadcastCampaign(
                companyId = selectedCompanyId.value,
                channel = channel,
                recipientName = recipientName,
                messageText = text,
                recipientPhone = phone,
                status = "Sent"
            )
            repository.insertCampaign(camp)
        }
    }

    // --- MODULE 3: PAYROLL ENGINE ---
    fun runPayrollCalculation(employeeId: Int, monthString: String) {
        viewModelScope.launch {
            val emp = repository.getEmployeeById(employeeId) ?: return@launch
            val basePay = emp.basePay
            val target = emp.targetAmount
            val sales = emp.currentSales

            // 1. Calculate Target-vs-Achievement Percentage
            val targetPercent = if (target > 0) (sales / target) * 100.0 else 100.0
            
            // 2. Adjust payout scale based on target achievement curve
            val achievementFactor = when {
                targetPercent >= 120.0 -> 1.25 // outstanding bonus boost
                targetPercent >= 100.0 -> 1.10
                targetPercent >= 80.0 -> 0.90
                else -> 0.70 // sub-par drop scale
            }

            // 3. Automated Sales Commission
            val commission = sales * emp.commissionRate

            // 4. GPS absence penalties (Count spoofed/unapproved absent nodes)
            val allAtts = attendances.value.filter { it.employeeId == employeeId && it.status == "Deducted" }
            val deductionFraction = allAtts.size * 0.05 // deduct 5% per spoof/failure
            val penaltyDeduction = basePay * deductionFraction

            // 5. Compute final disbursements
            val netPaid = (basePay * achievementFactor) + commission - penaltyDeduction

            val statement = """
                📌 ${emp.name} Payroll:
                - Base pay scale: BDT $basePay
                - Sales target: BDT $target (Achieved: BDT $sales, %${"%.1f".format(targetPercent)})
                - Multiplier curve applied: x$achievementFactor
                - Commission: BDT ${"%.1f".format(commission)}
                - GPS Audit Penalties (${allAtts.size} failed nodes): -BDT ${"%.1f".format(penaltyDeduction)}
                - Total disbursed net: BDT ${"%.1f".format(netPaid)}
            """.trimIndent()

            repository.insertPayroll(
                Payroll(
                    employeeId = employeeId,
                    monthString = monthString,
                    basePay = basePay,
                    targetAchievedPercent = targetPercent,
                    commissionEarned = commission,
                    penaltyDeduction = penaltyDeduction,
                    netPaid = netPaid,
                    paymentStatus = "Disbursed",
                    disputeStatement = statement
                )
            )
        }
    }

    // --- MODULE 4: CREDIT-RISK & Payments ---
    fun checkAndAdjustDealerDue(dealerId: Int, amount: Double) {
        viewModelScope.launch {
            repository.adjustDealerDue(dealerId, amount)
            // Re-evaluate limits to check freeze state
            val credit = repository.getDealerCreditById(dealerId)
            if (credit != null) {
                if (credit.currentDue >= credit.limitDue) {
                    repository.updateDealerFrozen(dealerId, true)
                } else {
                    repository.updateDealerFrozen(dealerId, false)
                }
            }
        }
    }

    fun submitPaymentAudit(dealerId: Int, amount: Double, token: String, photoUri: String?) {
        viewModelScope.launch {
            val audit = PaymentAudit(
                dealerId = dealerId,
                amount = amount,
                transactionToken = token,
                slipPhotoUri = photoUri,
                status = "Pending Verification"
            )
            repository.insertPaymentAudit(audit)
        }
    }

    fun auditPaymentSlip(auditId: Int, approve: Boolean, notes: String?) {
        viewModelScope.launch {
            val status = if (approve) "Approved" else "Rejected"
            repository.updatePaymentAuditStatus(auditId, status, notes)
            if (approve) {
                // Find payment to reduce dues
                val list = paymentAudits.value
                val item = list.find { it.id == auditId }
                if (item != null) {
                    repository.adjustDealerDue(item.dealerId, -item.amount)
                    // Re-evaluate freeze state
                    val credit = repository.getDealerCreditById(item.dealerId)
                    if (credit != null && credit.currentDue < credit.limitDue) {
                        repository.updateDealerFrozen(item.dealerId, false)
                    }
                }
            }
        }
    }

    // --- MODULE 5: PRODUCTION & SOURCING ---
    fun logSourcingLot(materialName: String, qty: Double, cost: Double) {
        viewModelScope.launch {
            val unit = if (qty > 0) cost / qty else 0.0
            val log = MaterialSourcing(
                materialName = materialName,
                quantityKg = qty,
                purchaseCost = cost,
                unitPrice = unit
            )
            repository.insertSourcingLog(log)
        }
    }

    // Calculates "Moving Average Cost" for a specific material
    fun getMovingAverageCost(materialName: String): Double {
        val logs = sourcingLogs.value.filter { it.materialName.equals(materialName, ignoreCase = true) }
        if (logs.isEmpty()) return 0.0
        val totalQty = logs.sumOf { it.quantityKg }
        val totalCost = logs.sumOf { it.purchaseCost }
        return if (totalQty > 0) totalCost / totalQty else 0.0
    }

    fun logProductionBatch(inputMaterialKg: Double, outputMaterialKg: Double, gatepass: String, challan: String) {
        viewModelScope.launch {
            val batch = ProductionBatch(
                inputMaterialKg = inputMaterialKg,
                outputMaterialKg = outputMaterialKg,
                gatepassNo = gatepass,
                challanNo = challan,
                isReceived = false
            )
            repository.insertProductionBatch(batch)
        }
    }

    fun receiveProductionBatch(id: Int) {
        viewModelScope.launch {
            repository.updateBatchReceived(id, true)
        }
    }

    // --- MODULE 4 CONT: Gemini Voice Invoicing ---
    fun processVoiceInvoiceCommand(command: String, dealerId: Int) {
        viewModelScope.launch {
            isProcessingVoice.value = true
            val credit = repository.getDealerCreditById(dealerId)
            val currentDue = credit?.currentDue ?: 0.0

            val responseString = GeminiVoiceService.parseVoiceBilling(command, currentDue)
            try {
                val parsedJson = JSONObject(responseString)
                invoiceItem.value = parsedJson.optString("item", "Mohona Masala Tea")
                invoiceQuantity.value = parsedJson.optDouble("quantityKg", 50.0)
                invoiceUnitPrice.value = parsedJson.optDouble("unitPrice", 280.0)
                invoiceDiscount.value = parsedJson.optDouble("discountPercent", 5.0)
                invoiceNotes.value = parsedJson.optString("notes", "Auto-parsed voice input")
            } catch (e: Exception) {
                invoiceNotes.value = "Error parsing response JSON: ${e.message}. Using default values."
            } finally {
                isProcessingVoice.value = false
            }
        }
    }

    // --- MODULE 6: Conversational BI Engine ---
    fun submitBIChatMessage(queryText: String) {
        viewModelScope.launch {
            if (queryText.isBlank()) return@launch
            isProcessingBI.value = true
            biChatQuery.value = queryText

            // Compile database summary context for Gemini
            val totalSalesValue = orders.value.filter { it.status == "Completed" }.sumOf { it.amount }
            val pendingDuesTotal = dealerCredits.value.sumOf { it.currentDue }
            val activeEmpCount = employees.value.size
            val materialsMovingAvg = """
                Raw Leaves MAC: BDT ${"%.1f".format(getMovingAverageCost("Raw Leaves"))}
                CMC MAC: BDT ${"%.1f".format(getMovingAverageCost("CMC Thickener"))}
                Packaging MAC: BDT ${"%.1f".format(getMovingAverageCost("Packaging Rolls"))}
            """.trimIndent()

            val databaseStateContext = """
                Enterprise Status Summary:
                - Active Company ID in scope: ${selectedCompanyId.value}
                - Completed Orders Revenue: BDT $totalSalesValue
                - Active Dealer Credit Outstanding: BDT $pendingDuesTotal
                - Employee Payroll Pool Count: $activeEmpCount
                - Sourcing Material Logs:
                $materialsMovingAvg
                - Active Production Batches: ${productionBatches.value.size} cycles.
            """.trimIndent()

            val response = GeminiVoiceService.queryBoardroomBI(queryText, databaseStateContext)
            biChatResponse.value = response
            isProcessingBI.value = false
        }
    }
}
