package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.*
import com.example.ui.viewmodel.ErpViewModel

// Premium Midnight Obsidian Palettes
val ObsidianBg = Color(0xFF05070B)
val GlassCardBg = Color(0x990D1117)
val CyanBorder = Color(0x4D0EA5E9)
val IndigoBorder = Color(0x4D4338CA)
val NeonCyan = Color(0xFF0EA5E9)
val DeepIndigo = Color(0xFF4338CA)
val NeonPurple = Color(0xFF8B5CF6)
val TextSilver = Color(0xFFE2E8F0)
val TextSlate = Color(0xFF94A3B8)
val EmeraldGreen = Color(0xFF10B981)
val CoralRed = Color(0xFFEF4444)
val AmberYellow = Color(0xFFF59E0B)

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyanBorder,
    testTagStr: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .testTag(testTagStr ?: "glass_card")
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(listOf(borderColor, borderColor.copy(alpha = 0.2f))),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = GlassCardBg),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainErpScreen(viewModel: ErpViewModel) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf(0) } // 0: Sales Chain, 1: HRM & Payroll, 2: Sourcing & Credit, 3: BI Boardroom

    val companies by viewModel.companies.collectAsStateWithLifecycle()
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val bulkProcurements by viewModel.bulkProcurements.collectAsStateWithLifecycle()
    val attendances by viewModel.attendances.collectAsStateWithLifecycle()
    val campaigns by viewModel.campaigns.collectAsStateWithLifecycle()
    val payrolls by viewModel.payrolls.collectAsStateWithLifecycle()
    val dealerCredits by viewModel.dealerCredits.collectAsStateWithLifecycle()
    val paymentAudits by viewModel.paymentAudits.collectAsStateWithLifecycle()
    val sourcingLogs by viewModel.sourcingLogs.collectAsStateWithLifecycle()
    val productionBatches by viewModel.productionBatches.collectAsStateWithLifecycle()

    val selectedCoId by viewModel.selectedCompanyId.collectAsStateWithLifecycle()
    val selectedRole by viewModel.selectedUserRole.collectAsStateWithLifecycle()
    val selectedEmpId by viewModel.selectedEmployeeId.collectAsStateWithLifecycle()

    val activeCompany = companies.find { it.id == selectedCoId } ?: Company(name = "Mohona Tea & Co.", code = "MTC")
    val activeEmployee = employees.find { it.id == selectedEmpId }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = GlassCardBg,
                modifier = Modifier.border(1.dp, IndigoBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    icon = { Icon(Icons.Default.ShoppingCart, "Sales & CRM") },
                    label = { Text("Sales & CRM") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        unselectedIconColor = TextSlate,
                        unselectedTextColor = TextSlate,
                        indicatorColor = DeepIndigo.copy(alpha = 0.5f)
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    icon = { Icon(Icons.Default.Groups, "HRM & Payroll") },
                    label = { Text("HR & Payroll") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        unselectedIconColor = TextSlate,
                        unselectedTextColor = TextSlate,
                        indicatorColor = DeepIndigo.copy(alpha = 0.5f)
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    icon = { Icon(Icons.Default.Settings, "Sourcing & Credit") },
                    label = { Text("Factory & Credit") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        unselectedIconColor = TextSlate,
                        unselectedTextColor = TextSlate,
                        indicatorColor = DeepIndigo.copy(alpha = 0.5f)
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 3,
                    onClick = { activeTab = 3 },
                    icon = { Icon(Icons.Default.Analytics, "BI Room") },
                    label = { Text("BI Boardroom") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        unselectedIconColor = TextSlate,
                        unselectedTextColor = TextSlate,
                        indicatorColor = DeepIndigo.copy(alpha = 0.5f)
                    )
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .drawBehind {
                    drawRect(color = ObsidianBg)
                    // Draw linear mesh gradient accents
                    drawCircle(
                        brush = Brush.radialGradient(listOf(NeonCyan.copy(alpha = 0.15f), Color.Transparent)),
                        center = Offset(size.width * 0.1f, size.height * 0.2f),
                        radius = size.width * 0.6f
                    )
                    drawCircle(
                        brush = Brush.radialGradient(listOf(DeepIndigo.copy(alpha = 0.15f), Color.Transparent)),
                        center = Offset(size.width * 0.9f, size.height * 0.8f),
                        radius = size.width * 0.6f
                    )
                }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // --- CUSTOM TOP HEAD BAR ---
                TopHeaderSection(
                    companies = companies,
                    selectedCoId = selectedCoId,
                    onCompanyChanged = { viewModel.selectedCompanyId.value = it },
                    activeCompany = activeCompany,
                    selectedRole = selectedRole,
                    onRoleChanged = { role ->
                        viewModel.selectedUserRole.value = role
                        // Set standard employee mapping based on selection for sandbox ease
                        val targetEmp = employees.find { it.role.equals(role, ignoreCase = true) }
                        if (targetEmp != null) {
                            viewModel.selectedEmployeeId.value = targetEmp.id
                        }
                    },
                    employees = employees,
                    selectedEmpId = selectedEmpId,
                    onEmpChanged = { viewModel.selectedEmployeeId.value = it }
                )

                Divider(color = IndigoBorder, thickness = 1.dp)

                // --- TABBED BODY CONTENTS ---
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (activeTab) {
                        0 -> SalesAndCrmTab(viewModel, orders, bulkProcurements, dealerCredits, employees)
                        1 -> HrmAndPayrollTab(viewModel, employees, attendances, payrolls)
                        2 -> SourcingAndCreditTab(viewModel, sourcingLogs, productionBatches, dealerCredits, paymentAudits)
                        3 -> BoardroomBiTab(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TopHeaderSection(
    companies: List<Company>,
    selectedCoId: Int,
    onCompanyChanged: (Int) -> Unit,
    activeCompany: Company,
    selectedRole: String,
    onRoleChanged: (String) -> Unit,
    employees: List<Employee>,
    selectedEmpId: Int,
    onEmpChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "MOHONA ENTERPRISE ERP",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = activeCompany.name,
                    color = TextSilver,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Multi-tenant Corporate Switcher
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(DeepIndigo.copy(alpha = 0.4f))
                    .clickable {
                        val nextId = if (selectedCoId == 1) 2 else 1
                        onCompanyChanged(nextId)
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Business, contentDescription = "Switch Tenant", tint = NeonCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "TENANT: ${if (selectedCoId == 1) "MTC" else "MMF"}",
                    color = TextSilver,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Role-Based User Hierarchy Matrix (Sandbox view switcher)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(GlassCardBg)
                .border(1.dp, CyanBorder.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = "Active Role", tint = AmberYellow, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ACTIVE ROLE DESK (Tap to switch viewpoint):",
                    color = TextSlate,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val roles = listOf("Owner", "ASM", "SO", "Incharge")
                roles.forEach { r ->
                    val isSel = selectedRole == r
                    Box(
                        modifier = Modifier
                            .testTag("role_btn_$r")
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSel) NeonCyan.copy(alpha = 0.2f) else Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = if (isSel) NeonCyan else Color(0x3394A3B8),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onRoleChanged(r) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = when (r) {
                                "Owner" -> "👑 Owner"
                                "ASM" -> "💼 Area Manager"
                                "SO" -> "🤝 Field Officer (SO)"
                                "Incharge" -> "⚙️ Factory Incharge"
                                else -> r
                            },
                            color = if (isSel) NeonCyan else TextSilver,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// --- TAB 1: SALES & CRM ---
@Composable
fun SalesAndCrmTab(
    viewModel: ErpViewModel,
    orders: List<Order>,
    bulkList: List<BulkProcurement>,
    dealers: List<DealerCredit>,
    employees: List<Employee>
) {
    var retailerName by remember { mutableStateOf("Desh Tea Cabin") }
    var retailerAddress by remember { mutableStateOf("Bund Road, Barisal") }
    var selectedDealerId by remember { mutableStateOf(1) }
    var voiceInputText by remember { mutableStateOf("Create order for Desh Tea, quantity 100kg, discount 8%") }

    val invoiceItem by viewModel.invoiceItem.collectAsStateWithLifecycle()
    val invoiceQuantity by viewModel.invoiceQuantity.collectAsStateWithLifecycle()
    val invoiceUnitPrice by viewModel.invoiceUnitPrice.collectAsStateWithLifecycle()
    val invoiceDiscount by viewModel.invoiceDiscount.collectAsStateWithLifecycle()
    val invoiceNotes by viewModel.invoiceNotes.collectAsStateWithLifecycle()
    val isProcessingVoice by viewModel.isProcessingVoice.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- MULTIMODAL GEMINI VOICE BILLING COMPONENT ---
        item {
            GlassmorphicCard(borderColor = CyanBorder, testTagStr = "voice_billing_panel") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice Billing", tint = NeonCyan, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GEMINI VOICE BILLING ENGINE",
                        color = TextSilver,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Verbal Order Parser: Speak or enter command to dynamically fill forms, apply dealer outstanding, previous বকেয়া, and calculate net due.",
                    color = TextSlate,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = voiceInputText,
                    onValueChange = { voiceInputText = it },
                    label = { Text("Verbal Command / Voice Simulator", color = TextSlate) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = TextSilver,
                        unfocusedTextColor = TextSilver,
                        focusedIndicatorColor = NeonCyan,
                        unfocusedIndicatorColor = IndigoBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.processVoiceInvoiceCommand(voiceInputText, selectedDealerId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        modifier = Modifier.weight(1f),
                        enabled = !isProcessingVoice
                    ) {
                        if (isProcessingVoice) {
                            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(16.dp))
                        } else {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Parse command", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Voice Parse", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Clear Voice Button
                    OutlinedButton(
                        onClick = {
                            voiceInputText = ""
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSilver),
                        border = BorderStroke(1.dp, IndigoBorder)
                    ) {
                        Text("Reset", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Divider(color = IndigoBorder, thickness = 1.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // DUAL CONTROL PANEL: Tactile Manual overrides
                Text(
                    text = "⚙️ DUAL-CONTROL INTERFACE (MANUAL OVERRIDES)",
                    color = AmberYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = invoiceItem,
                    onValueChange = { viewModel.invoiceItem.value = it },
                    label = { Text("Product Sourcing Item", color = TextSlate) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = invoiceQuantity.toString(),
                        onValueChange = {
                            val v = it.toDoubleOrNull()
                            if (v != null) viewModel.invoiceQuantity.value = v
                        },
                        label = { Text("Quantity (Kg)", color = TextSlate) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )

                    OutlinedTextField(
                        value = invoiceDiscount.toString(),
                        onValueChange = {
                            val v = it.toDoubleOrNull()
                            if (v != null) viewModel.invoiceDiscount.value = v
                        },
                        label = { Text("Discount (%)", color = TextSlate) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = retailerName,
                        onValueChange = { retailerName = it },
                        label = { Text("Retailer Outlet", color = TextSlate) },
                        modifier = Modifier.weight(1.2f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )

                    // Target Dealer Selector
                    Column(modifier = Modifier.weight(0.8f)) {
                        Text("Dealer Hub", color = TextSlate, fontSize = 10.sp)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFFFFF))
                                .clickable {
                                    selectedDealerId = if (selectedDealerId == 1) 2 else if (selectedDealerId == 2) 3 else 1
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val dName = dealers.find { it.id == selectedDealerId }?.dealerName ?: "Chittagong"
                            Text(dName, color = TextSilver, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Icon(Icons.Default.ArrowDropDown, "", tint = NeonCyan)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Outstanding Due Statement
                val matchingDealer = dealers.find { it.id == selectedDealerId }
                val previousDue = matchingDealer?.currentDue ?: 0.0
                val price = invoiceQuantity * invoiceUnitPrice
                val totalWithDiscount = price * (1.0 - (invoiceDiscount / 100.0))
                val netOutstanding = previousDue + totalWithDiscount

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepIndigo.copy(alpha = 0.2f))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("বকেয়া (Previous Due): BDT $previousDue", color = TextSlate, fontSize = 11.sp)
                        Text("Current Amount: BDT ${"%.1f".format(totalWithDiscount)}", color = TextSlate, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Net Outstanding Balance", color = TextSilver, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("BDT ${"%.1f".format(netOutstanding)}", color = NeonCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (retailerName.isBlank()) {
                            Toast.makeText(context, "Please write retailer name", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        // Enforce credit freeze rule: Lock order if বকেয়া exceeds limits!
                        if (matchingDealer?.isFrozen == true) {
                            Toast.makeText(context, "🚫 CREDIT LOCKED: Dealer's বকেয়া crosses threshold limits!", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        viewModel.logOrder(retailerName, retailerAddress, invoiceItem, invoiceQuantity, invoiceDiscount)
                        Toast.makeText(context, "Order added & state flushed to ASM", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (matchingDealer?.isFrozen == true) CoralRed else EmeraldGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Submit Invoice", tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (matchingDealer?.isFrozen == true) "CREDIT BLOCKED (বকেয়া সীমা অতিক্রম)" else "SUBMIT & FLUSH STATE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "*Note: AI generated invoice variables are kept open. Human admin always retains final execution editing and submit authority.",
                    color = TextSlate,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // --- ORDER PIPELINE & 3-STEP sales CHAIN ---
        item {
            Text(
                text = "⛓️ TWO-WAY SALES CHAIN & TRANSACTION LOGS",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        if (orders.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No active orders found in sales log.", color = TextSlate)
                }
            }
        } else {
            items(orders) { o ->
                GlassmorphicCard(borderColor = if (o.status == "Ordered") AmberYellow else if (o.status == "FulfillTicket") NeonCyan else EmeraldGreen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(
                                            when (o.status) {
                                                "Ordered" -> AmberYellow.copy(alpha = 0.2f)
                                                "FulfillTicket" -> NeonCyan.copy(alpha = 0.2f)
                                                else -> EmeraldGreen.copy(alpha = 0.2f)
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = o.status.uppercase(),
                                        color = when (o.status) {
                                            "Ordered" -> AmberYellow
                                            "FulfillTicket" -> NeonCyan
                                            else -> EmeraldGreen
                                        },
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Order #${o.id}", color = TextSlate, fontSize = 11.sp)
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(o.retailerName, color = TextSilver, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(o.itemsJson, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text("Outlet Address: ${o.retailerAddress}", color = TextSlate, fontSize = 11.sp)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("BDT ${"%.1f".format(o.amount)}", color = TextSilver, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))

                            // Interactive 3-Step Operations based on selected simulation role
                            when (o.status) {
                                "Ordered" -> {
                                    Button(
                                        onClick = {
                                            viewModel.verifyAndRouteOrder(o.id, 1) // Routes to Dealer 1 (Chittagong)
                                            Toast.makeText(context, "ASM Verified & Routed Ticket to Dealer Hub", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("ASM Verify & Route", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                "FulfillTicket" -> {
                                    Button(
                                        onClick = {
                                            viewModel.completeFulfillment(o.id)
                                            Toast.makeText(context, "Fulfillment complete. Flushed to Owner Dashboard", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("Dealer Deliver", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                "Completed" -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, "Completed", tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Owner Flushed", color = EmeraldGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- BULK PIPELINE CHANNEL ---
        item {
            GlassmorphicCard(borderColor = IndigoBorder) {
                Text(
                    text = "💼 AREA MANAGER BULK PIPELINE",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Input wholesale direct procurements linking distributor to warehouse limits.", color = TextSlate, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(10.dp))

                var bulkDist by remember { mutableStateOf("Sylhet Wholesale Syndicate") }
                var bulkQty by remember { mutableStateOf("300") }
                var bulkPrice by remember { mutableStateOf("260") }

                OutlinedTextField(
                    value = bulkDist,
                    onValueChange = { bulkDist = it },
                    label = { Text("Wholesale Distributor", color = TextSlate) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bulkQty,
                        onValueChange = { bulkQty = it },
                        label = { Text("Quantity (Kg)", color = TextSlate) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )

                    OutlinedTextField(
                        value = bulkPrice,
                        onValueChange = { bulkPrice = it },
                        label = { Text("Unit BDT Price", color = TextSlate) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val q = bulkQty.toDoubleOrNull() ?: 100.0
                        val p = bulkPrice.toDoubleOrNull() ?: 260.0
                        viewModel.submitBulkProcurement(bulkDist, q, p)
                        Toast.makeText(context, "Bulk pipeline submitted", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register Bulk Pipeline Order", color = Color.White, fontWeight = FontWeight.Bold)
                }

                if (bulkList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Live Pipelines:", color = TextSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    bulkList.take(3).forEach { b ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${b.distributorName} (${b.quantityKg}kg)", color = TextSlate, fontSize = 11.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (b.status == "Approved") EmeraldGreen.copy(alpha = 0.2f) else CoralRed.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(b.status, color = if (b.status == "Approved") EmeraldGreen else CoralRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // --- GEOTAGGED MAP ROUTE TRACE SIMULATION ---
        item {
            GlassmorphicCard(borderColor = CyanBorder) {
                Text(
                    text = "📍 GEOTAGGED CRM - LIVE GPS AUDIT TRAIL",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Enforce background location trace during retail check-ins to prevent location spoof fraud.", color = TextSlate, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(12.dp))

                // GPS Route trail Canvas Drawing (connecting route nodes)
                Text("Breadcrumb Map (Connecting route coordinates):", color = TextSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF030508))
                ) {
                    val path = Path().apply {
                        moveTo(size.width * 0.1f, size.height * 0.8f)
                        quadraticTo(size.width * 0.4f, size.height * 0.2f, size.width * 0.6f, size.height * 0.5f)
                        lineTo(size.width * 0.9f, size.height * 0.3f)
                    }
                    drawPath(
                        path = path,
                        color = NeonCyan,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // Draw node circles
                    drawCircle(color = EmeraldGreen, radius = 6.dp.toPx(), center = Offset(size.width * 0.1f, size.height * 0.8f))
                    drawCircle(color = AmberYellow, radius = 6.dp.toPx(), center = Offset(size.width * 0.6f, size.height * 0.5f))
                    drawCircle(color = NeonCyan, radius = 8.dp.toPx(), center = Offset(size.width * 0.9f, size.height * 0.3f))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active Field Node Trace", color = TextSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Sujon Ahmed (Field Officer) - Barisal Route", color = TextSlate, fontSize = 10.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                viewModel.logCheckIn(22.7010, 90.3535, false, "192.168.1.100")
                                Toast.makeText(context, "GPS Node Registered Successfully (Approved)", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Safe Checkin", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.logCheckIn(23.8103, 90.4125, true, null) // Dhaka coordinates while assigned in Barisal
                                Toast.makeText(context, "GPS Spoof Detected! Log flagged as Deducted Penalty.", Toast.LENGTH_LONG).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Sim Spoof", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- BRAND ENGAGEMENT HUB ---
        item {
            GlassmorphicCard(borderColor = IndigoBorder) {
                Text(
                    text = "📢 BRAND ENGAGEMENT HUB (OMNICHANNEL RECOVERIES)",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Broadcast promotional alerts, localized Bengali robo-calls or SMS notifications straight to retailer shops.", color = TextSlate, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(10.dp))

                var mType by remember { mutableStateOf("SMS_Bengali") }
                var mText by remember { mutableStateOf("প্রিয় মোহনা গ্রাহক, আমাদের মাসালা চায়ের নতুন লটে ৫% ডাইরেক্ট ক্যাশব্যাক ডিলার পয়েন্টে সংগ্রহ করুন!") }
                var mPhone by remember { mutableStateOf("+8801890011223") }

                OutlinedTextField(
                    value = mText,
                    onValueChange = { mText = it },
                    label = { Text("Broadcast Message (Bengali/English)", color = TextSlate) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = mPhone,
                        onValueChange = { mPhone = it },
                        label = { Text("Recipient Mobile", color = TextSlate) },
                        modifier = Modifier.weight(1.2f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )

                    Column(modifier = Modifier.weight(0.8f)) {
                        Text("Channel", color = TextSlate, fontSize = 9.sp)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFFFFF))
                                .clickable {
                                    mType = if (mType == "SMS_Bengali") "Robocall_Bengali" else "SMS_Bengali"
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(mType, color = TextSilver, fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.broadcastMarketingCampaign(mType, "Retail Outlet Owner", mText, mPhone)
                        Toast.makeText(context, "Omnichannel Broadcast Succeeded", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Broadcast Alert", tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Broadcast AI Campaign Alert", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- TAB 2: HR & PAYROLL ---
@Composable
fun HrmAndPayrollTab(
    viewModel: ErpViewModel,
    employees: List<Employee>,
    attendances: List<Attendance>,
    payrolls: List<Payroll>
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "📊 ZERO-COMPLAINT DYNAMIC 3-LAYER PAYROLL PANEL",
                color = NeonCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Computes payroll factoring Base Pay, Target-vs-Achievement Scaling Curve, automated Sales Commission, and direct deductions for GPS nodes absences.",
                color = TextSlate,
                fontSize = 11.sp
            )
        }

        // List Employees & trigger payrolls
        items(employees) { emp ->
            // Filter attendance issues for deductions
            val deductionsCount = attendances.filter { it.employeeId == emp.id && it.status == "Deducted" }.size

            GlassmorphicCard(borderColor = CyanBorder) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(emp.name, color = TextSilver, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Role: ${emp.role} | Base: BDT ${emp.basePay}", color = TextSlate, fontSize = 11.sp)
                        Text("Target: BDT ${emp.targetAmount} | Sales: BDT ${emp.currentSales}", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        
                        if (deductionsCount > 0) {
                            Text("⚠️ GPS Gaps Detected: $deductionsCount unapproved nodes", color = CoralRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text("✅ GPS Verification Clear", color = EmeraldGreen, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.runPayrollCalculation(emp.id, "July 2026")
                            Toast.makeText(context, "Payroll Calculated & Disbursed for ${emp.name}", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Disburse Pay", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Disbursement Log Table with Transparent dispute statements
        item {
            Text(
                text = "📜 DISPATCHED DISPUTE-PROOF STATEMENT RECORDS",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        if (payrolls.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No payroll invoices disbursed yet.", color = TextSlate)
                }
            }
        } else {
            items(payrolls) { p ->
                val matchedEmp = employees.find { it.id == p.employeeId }

                GlassmorphicCard(borderColor = IndigoBorder) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Disbursement for ${matchedEmp?.name ?: "Employee"}", color = TextSilver, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Month: ${p.monthString} | Net payout: BDT ${p.netPaid}", color = NeonPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = p.disputeStatement,
                                color = TextSlate,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF030508))
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- TAB 3: PRODUCTION & CREDIT RISK ---
@Composable
fun SourcingAndCreditTab(
    viewModel: ErpViewModel,
    sourcingLogs: List<MaterialSourcing>,
    batches: List<ProductionBatch>,
    dealers: List<DealerCredit>,
    audits: List<PaymentAudit>
) {
    val context = LocalContext.current
    var matName by remember { mutableStateOf("Raw Leaves") }
    var matQty by remember { mutableStateOf("150") }
    var matCost by remember { mutableStateOf("33000") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- AUTONOMOUS FACTORY PRODUCTION WING ---
        item {
            Text(
                text = "🏭 COMPLETELY AUTONOMOUS FACTORY PRODUCTION WING",
                color = NeonCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Isolated factory sourcing logs separate from standard marketing structures to avoid execution friction.", color = TextSlate, fontSize = 11.sp)
        }

        item {
            GlassmorphicCard(borderColor = CyanBorder) {
                Text(
                    text = "🍃 FLUCTUATING MATERIAL SOURCING",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Material", color = TextSlate, fontSize = 10.sp)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFFFFF))
                                .clickable {
                                    matName = if (matName == "Raw Leaves") "CMC Thickener" else if (matName == "CMC Thickener") "Packaging Rolls" else "Raw Leaves"
                                }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(matName, color = TextSilver, fontSize = 11.sp)
                        }
                    }

                    OutlinedTextField(
                        value = matQty,
                        onValueChange = { matQty = it },
                        label = { Text("Weight (Kg)", color = TextSlate) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )

                    OutlinedTextField(
                        value = matCost,
                        onValueChange = { matCost = it },
                        label = { Text("Total Cost", color = TextSlate) },
                        modifier = Modifier.weight(1.2f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val q = matQty.toDoubleOrNull() ?: 100.0
                        val c = matCost.toDoubleOrNull() ?: 20000.0
                        viewModel.logSourcingLot(matName, q, c)
                        Toast.makeText(context, "Sourcing lot successfully logged", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register Sourcing Purchase", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Divider(color = IndigoBorder, thickness = 1.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // Displays moving average costing
                Text(
                    text = "⚖️ MOVING AVERAGE COST (MAC) CALCULATIONS:",
                    color = AmberYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Raw Leaves (Average)", color = TextSlate, fontSize = 10.sp)
                        Text("BDT ${"%.1f".format(viewModel.getMovingAverageCost("Raw Leaves"))} / kg", color = TextSilver, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("CMC Thickener", color = TextSlate, fontSize = 10.sp)
                        Text("BDT ${"%.1f".format(viewModel.getMovingAverageCost("CMC Thickener"))} / kg", color = TextSilver, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Packaging Rolls", color = TextSlate, fontSize = 10.sp)
                        Text("BDT ${"%.1f".format(viewModel.getMovingAverageCost("Packaging Rolls"))} / kg", color = TextSilver, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Batch dryer cycles & warehouse gatepass
        item {
            GlassmorphicCard(borderColor = IndigoBorder) {
                Text(
                    text = "⚡ Dryer Drum Batch cycles & Gatepass tracking",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Log 30kg dry batch cycles. Items cannot leave production floor unless Warehouse Incharge accepts gatepass receipt.", color = TextSlate, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(10.dp))

                var inputWeight by remember { mutableStateOf("30") }
                var outputWeight by remember { mutableStateOf("28.8") }
                var gatepassNo by remember { mutableStateOf("GP-2026-105") }
                var challanNo by remember { mutableStateOf("CH-3044") }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = inputWeight,
                        onValueChange = { inputWeight = it },
                        label = { Text("Input (Kg)", color = TextSlate) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                    OutlinedTextField(
                        value = outputWeight,
                        onValueChange = { outputWeight = it },
                        label = { Text("Output (Kg)", color = TextSlate) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = gatepassNo,
                        onValueChange = { gatepassNo = it },
                        label = { Text("Gatepass #", color = TextSlate) },
                        modifier = Modifier.weight(1.2f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                    OutlinedTextField(
                        value = challanNo,
                        onValueChange = { challanNo = it },
                        label = { Text("Challan #", color = TextSlate) },
                        modifier = Modifier.weight(0.8f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val i = inputWeight.toDoubleOrNull() ?: 30.0
                        val o = outputWeight.toDoubleOrNull() ?: 28.5
                        viewModel.logProductionBatch(i, o, gatepassNo, challanNo)
                        Toast.makeText(context, "Production drum cycle batch submitted", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Trigger Batch dryer cycle Output", color = Color.White, fontWeight = FontWeight.Bold)
                }

                if (batches.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Pending Warehouse Receipts:", color = TextSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    batches.take(3).forEach { b ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Gatepass ${b.gatepassNo} | Out: ${b.outputMaterialKg}kg", color = TextSlate, fontSize = 11.sp)
                                Text("Challan: ${b.challanNo}", color = TextSlate, fontSize = 10.sp)
                            }
                            if (b.isReceived) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, "", tint = EmeraldGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Received", color = EmeraldGreen, fontSize = 10.sp)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        viewModel.receiveProductionBatch(b.id)
                                        Toast.makeText(context, "Challan Outward Approved", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("Receive In WH", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- CREDIT-RISK & MATRIX GRAphS ---
        item {
            Text(
                text = "🛡️ CREDIT-RISK MATRIX & VERIFICATION ENGINE",
                color = NeonCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Automated debt controls and bank-slip verification panel to approve dealer transactions.", color = TextSlate, fontSize = 11.sp)
        }

        // Debt to Sales Ratio visualizer
        item {
            GlassmorphicCard(borderColor = CyanBorder) {
                Text(
                    text = "📊 DEBT-TO-SALES RISK RATIO MATRIX",
                    color = TextSilver,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                dealers.forEach { d ->
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(d.dealerName, color = TextSilver, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Ratio: ${"%.1f".format(d.debtToSalesRatio * 100)}% (${if (d.isFrozen) "FROZEN" else "ACTIVE"})",
                                color = if (d.debtToSalesRatio >= 0.9) CoralRed else if (d.debtToSalesRatio >= 0.5) AmberYellow else EmeraldGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Linear progress ratio bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF030508))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(d.debtToSalesRatio.coerceAtMost(1.0).toFloat())
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            listOf(EmeraldGreen, if (d.debtToSalesRatio >= 0.9) CoralRed else AmberYellow)
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }

        // BULLETPROOF PAYMENTS AUDITING WORKDESK
        item {
            GlassmorphicCard(borderColor = IndigoBorder) {
                Text(
                    text = "💼 FINANCE AUDITING - BANK SLIP CLEARANCE DESK",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Finance desk must review mobile transaction tokens or bank check photos to release order freeze blocks.", color = TextSlate, fontSize = 11.sp)

                if (audits.isEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No payment slips awaiting review.", color = TextSlate, fontSize = 11.sp)
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    audits.forEach { au ->
                        val matchedDealer = dealers.find { it.id == au.dealerId }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, IndigoBorder, RoundedCornerShape(8.dp))
                                .background(Color(0xFF030508))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(matchedDealer?.dealerName ?: "Dealer Point", color = TextSilver, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("TxnID: ${au.transactionToken}", color = NeonPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("Amount Submitted: BDT ${au.amount}", color = TextSilver, fontSize = 11.sp)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (au.status == "Pending Verification") {
                                        Button(
                                            onClick = {
                                                viewModel.auditPaymentSlip(au.id, true, "Checked bank settlement successfully.")
                                                Toast.makeText(context, "Slip approved, dues adjusted", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(26.dp)
                                        ) {
                                            Text("Approve", color = Color.White, fontSize = 9.sp)
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.auditPaymentSlip(au.id, false, "Token mismatches.")
                                                Toast.makeText(context, "Slip rejected", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = CoralRed),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(26.dp)
                                        ) {
                                            Text("Reject", color = Color.White, fontSize = 9.sp)
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(if (au.status == "Approved") EmeraldGreen.copy(alpha = 0.2f) else CoralRed.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(au.status, color = if (au.status == "Approved") EmeraldGreen else CoralRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Divider(color = IndigoBorder, thickness = 1.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // Submit manual payment transaction simulation
                Text("Simulate Dealer Bank Slip Upload:", color = TextSilver, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                var payAmt by remember { mutableStateOf("45000") }
                var payToken by remember { mutableStateOf("TXN9081223401") }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = payAmt,
                        onValueChange = { payAmt = it },
                        label = { Text("Payment Amount", color = TextSlate) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                    OutlinedTextField(
                        value = payToken,
                        onValueChange = { payToken = it },
                        label = { Text("Txn ID Token", color = TextSlate) },
                        modifier = Modifier.weight(1.2f),
                        colors = TextFieldDefaults.colors(focusedTextColor = TextSilver, unfocusedTextColor = TextSilver, focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val a = payAmt.toDoubleOrNull() ?: 20000.0
                        viewModel.submitPaymentAudit(1, a, payToken, null)
                        Toast.makeText(context, "Payment bank slip uploaded successfully. Sent to verification desk.", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload Bank slip & TxnToken", color = Color.White)
                }
            }
        }
    }
}

// --- TAB 4: OWNER BI ADVISORY BOARDROOM ---
@Composable
fun BoardroomBiTab(viewModel: ErpViewModel) {
    var biQueryInput by remember { mutableStateOf("আমাদের বরিশাল জোনে প্রফিট মার্জিন ড্রপ করার আসল কারণ কী?") }
    val responseText by viewModel.biChatResponse.collectAsStateWithLifecycle()
    val isProcessing by viewModel.isProcessingBI.collectAsStateWithLifecycle()
    val strategies by viewModel.strategyCards.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "👑 CHIEF OWNER'S STRATEGIC COMMAND BOARDROOM",
                color = NeonCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Premium workspace reserved solely for Company Executives and Owners to query databases directly using natural language.", color = TextSlate, fontSize = 11.sp)
        }

        // --- CONVERSATIONAL BI ENGINE SECTION ---
        item {
            GlassmorphicCard(borderColor = CyanBorder, testTagStr = "bi_conversational_panel") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Analytics, contentDescription = "BI Analysis", tint = NeonCyan, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONVERSATIONAL BI CHAT ENGINE",
                        color = TextSilver,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Query in Bengali or English to diagnose field attendance gaps, credit risk margins, profit drops, and finished goods costings.",
                    color = TextSlate,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = biQueryInput,
                    onValueChange = { biQueryInput = it },
                    label = { Text("Ask BI Advisor (e.g. 'আমাদের বরিশাল জোনে প্রফিট ড্রপ কেন?')", color = TextSlate) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = TextSilver,
                        unfocusedTextColor = TextSilver,
                        focusedIndicatorColor = NeonCyan,
                        unfocusedIndicatorColor = IndigoBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.submitBIChatMessage(biQueryInput)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(16.dp))
                    } else {
                        Icon(Icons.Default.Send, contentDescription = "Submit BI Query", modifier = Modifier.size(16.dp), tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Extract Real-Time BI Diagnostics", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Diagnostic response output viewport
                Text("Advisory Analysis Output:", color = AmberYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = responseText,
                    color = TextSilver,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF030508))
                        .border(1.dp, IndigoBorder, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }
        }

        // --- ACTIONABLE EXECUTIVE STRATEGY CARDS FEED ---
        item {
            Text(
                text = "💡 ACTIONABLE PRESCRIPTION STRATEGIES",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        items(strategies) { card ->
            GlassmorphicCard(borderColor = IndigoBorder) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("MOHONA prescription Strategy Card", color = TextSlate, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(card, color = TextSilver, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            Toast.makeText(context, "Executive Command Issued to ASM/Finance Desks!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("Apply", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
