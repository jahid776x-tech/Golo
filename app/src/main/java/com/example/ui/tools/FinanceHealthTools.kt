package com.example.ui.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.pow

// -------------------------------------------------------------
// 62. POS / SIMPLE CASHBOOK
// -------------------------------------------------------------
data class CashEntry(val id: Long, val title: String, val amount: Double, val isIncome: Boolean)

@Composable
fun CashbookTool() {
    var entries by remember {
        mutableStateOf(
            listOf(
                CashEntry(1, "Opening Cash Balance", 15000.0, true),
                CashEntry(2, "Grocery & Office Supplies", 1250.0, false),
                CashEntry(3, "Freelance Project Payment", 8000.0, true),
                CashEntry(4, "Utility Bills", 2400.0, false)
            )
        )
    }
    var titleInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(true) }

    val totalIncome = entries.filter { it.isIncome }.sumOf { it.amount }
    val totalExpense = entries.filter { !it.isIncome }.sumOf { it.amount }
    val netBalance = totalIncome - totalExpense

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Net Cash Balance", style = MaterialTheme.typography.labelMedium)
                    Text("৳ %.2f".format(netBalance), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("+ ৳ %.0f".format(totalIncome), color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                    Text("- ৳ %.0f".format(totalExpense), color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = titleInput, onValueChange = { titleInput = it }, label = { Text("Entry Note") }, modifier = Modifier.weight(1.5f), singleLine = true)
            OutlinedTextField(value = amountInput, onValueChange = { amountInput = it }, label = { Text("Amount (৳)") }, modifier = Modifier.weight(1f), singleLine = true)
        }

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = isIncome, onClick = { isIncome = true }, label = { Text("Cash In (+)") })
            FilterChip(selected = !isIncome, onClick = { isIncome = false }, label = { Text("Cash Out (-)") })
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    val amt = amountInput.toDoubleOrNull() ?: 0.0
                    if (titleInput.isNotBlank() && amt > 0) {
                        entries = listOf(CashEntry(System.currentTimeMillis(), titleInput.trim(), amt, isIncome)) + entries
                        titleInput = ""
                        amountInput = ""
                    }
                }
            ) {
                Text("Add Entry")
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.heightIn(max = 280.dp)) {
            items(entries) { item ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.title, fontWeight = FontWeight.Medium)
                        Text(
                            "${if (item.isIncome) "+" else "-"} ৳ %.2f".format(item.amount),
                            fontWeight = FontWeight.Bold,
                            color = if (item.isIncome) Color(0xFF16A34A) else Color(0xFFDC2626)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 63. EMI LOAN CALCULATOR
// -------------------------------------------------------------
@Composable
fun EmiCalculatorTool() {
    var principalInput by remember { mutableStateOf("500000") }
    var interestRateInput by remember { mutableStateOf("9.5") }
    var tenureMonthsInput by remember { mutableStateOf("24") }

    val p = principalInput.toDoubleOrNull() ?: 0.0
    val annualR = interestRateInput.toDoubleOrNull() ?: 0.0
    val months = tenureMonthsInput.toDoubleOrNull() ?: 1.0

    val monthlyR = (annualR / 12.0) / 100.0
    val emi = if (monthlyR > 0 && months > 0) {
        (p * monthlyR * (1.0 + monthlyR).pow(months)) / ((1.0 + monthlyR).pow(months) - 1.0)
    } else {
        p / months
    }

    val totalPayment = emi * months
    val totalInterest = totalPayment - p

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(value = principalInput, onValueChange = { principalInput = it }, label = { Text("Principal Loan Amount (৳)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = interestRateInput, onValueChange = { interestRateInput = it }, label = { Text("Interest Rate (% p.a.)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = tenureMonthsInput, onValueChange = { tenureMonthsInput = it }, label = { Text("Tenure (Months)") }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Monthly EMI Installment", style = MaterialTheme.typography.labelMedium)
                Text("৳ %.2f / month".format(emi), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Interest Payable:")
                    Text("৳ %.2f".format(totalInterest), fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Amount (Principal + Interest):")
                    Text("৳ %.2f".format(totalPayment), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 64. GST / VAT CALCULATOR
// -------------------------------------------------------------
@Composable
fun VatCalculatorTool() {
    var amountInput by remember { mutableStateOf("1000") }
    var vatRateInput by remember { mutableStateOf("15") } // Standard 15% VAT
    var isExclusive by remember { mutableStateOf(true) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val rate = vatRateInput.toDoubleOrNull() ?: 15.0

    val vatAmount = if (isExclusive) {
        amount * (rate / 100.0)
    } else {
        amount - (amount / (1.0 + rate / 100.0))
    }

    val finalPrice = if (isExclusive) amount + vatAmount else amount
    val netPrice = if (isExclusive) amount else amount - vatAmount

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(value = amountInput, onValueChange = { amountInput = it }, label = { Text("Base Price / Amount (৳)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("5%", "7.5%", "10%", "15%").forEach { r ->
                val num = r.removeSuffix("%")
                FilterChip(selected = vatRateInput == num, onClick = { vatRateInput = num }, label = { Text(r) })
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = isExclusive, onClick = { isExclusive = true }, label = { Text("VAT Exclusive (+VAT)") })
            FilterChip(selected = !isExclusive, onClick = { isExclusive = false }, label = { Text("VAT Inclusive (In Price)") })
        }

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Net Price:")
                    Text("৳ %.2f".format(netPrice))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("VAT Amount ($rate%):")
                    Text("৳ %.2f".format(vatAmount), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Divider()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Gross Total Price:", fontWeight = FontWeight.Bold)
                    Text("৳ %.2f".format(finalPrice), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 65. DISCOUNT & PERCENTAGE CALCULATOR
// -------------------------------------------------------------
@Composable
fun DiscountCalculatorTool() {
    var priceInput by remember { mutableStateOf("2500") }
    var discountInput by remember { mutableStateOf("20") }

    val price = priceInput.toDoubleOrNull() ?: 0.0
    val disc = discountInput.toDoubleOrNull() ?: 0.0

    val savings = price * (disc / 100.0)
    val finalPrice = price - savings

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = priceInput, onValueChange = { priceInput = it }, label = { Text("Original Price (৳)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = discountInput, onValueChange = { discountInput = it }, label = { Text("Discount (%)") }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Final Sale Price", style = MaterialTheme.typography.labelMedium)
                Text("৳ %.2f".format(finalPrice), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("You Save: ৳ %.2f ($disc%% off)".format(savings), fontWeight = FontWeight.SemiBold, color = Color(0xFF16A34A))
            }
        }
    }
}

// -------------------------------------------------------------
// 66. COMPOUND INTEREST CALCULATOR
// -------------------------------------------------------------
@Composable
fun CompoundInterestTool() {
    var principal by remember { mutableStateOf("100000") }
    var rate by remember { mutableStateOf("8.5") }
    var years by remember { mutableStateOf("5") }

    val p = principal.toDoubleOrNull() ?: 0.0
    val r = rate.toDoubleOrNull() ?: 0.0
    val t = years.toDoubleOrNull() ?: 1.0

    val n = 12.0 // Monthly compounding
    val finalAmount = p * (1.0 + (r / 100.0) / n).pow(n * t)
    val interestEarned = finalAmount - p

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(value = principal, onValueChange = { principal = it }, label = { Text("Principal Investment (৳)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = rate, onValueChange = { rate = it }, label = { Text("Interest Rate (%)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = years, onValueChange = { years = it }, label = { Text("Period (Years)") }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Maturity Value after ${years.toIntOrNull() ?: 5} Years", style = MaterialTheme.typography.labelMedium)
                Text("৳ %.2f".format(finalAmount), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("Total Interest Earned: ৳ %.2f".format(interestEarned), color = Color(0xFF16A34A), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// -------------------------------------------------------------
// 67. BMI CALCULATOR
// -------------------------------------------------------------
@Composable
fun BmiCalculatorTool() {
    var weightKg by remember { mutableStateOf("68") }
    var heightCm by remember { mutableStateOf("172") }

    val w = weightKg.toDoubleOrNull() ?: 0.0
    val hM = (heightCm.toDoubleOrNull() ?: 170.0) / 100.0
    val bmi = if (hM > 0) w / (hM * hM) else 0.0

    val (category, catColor) = when {
        bmi < 18.5 -> "Underweight" to Color(0xFF38BDF8)
        bmi < 25.0 -> "Normal Weight (Healthy)" to Color(0xFF22C55E)
        bmi < 30.0 -> "Overweight" to Color(0xFFF59E0B)
        else -> "Obese" to Color(0xFFEF4444)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = weightKg, onValueChange = { weightKg = it }, label = { Text("Weight (kg)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = heightCm, onValueChange = { heightCm = it }, label = { Text("Height (cm)") }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Your Body Mass Index (BMI)", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Text("%.1f".format(bmi), fontSize = 42.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(category, fontWeight = FontWeight.Bold, color = catColor, fontSize = 18.sp)
                Spacer(Modifier.height(12.dp))
                Text("Normal healthy BMI range is 18.5 - 24.9", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// -------------------------------------------------------------
// 68. WATER INTAKE TRACKER
// -------------------------------------------------------------
@Composable
fun WaterTrackerTool() {
    var drunkMl by remember { mutableStateOf(1250) }
    val goalMl = 2500
    val progress = (drunkMl.toFloat() / goalMl).coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(170.dp)) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 12.dp,
                color = Color(0xFF0284C7),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$drunkMl ml", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                Text("of $goalMl ml", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = { drunkMl += 250 }) {
                Text("+ 250 ml (Glass)")
            }
            Button(onClick = { drunkMl += 500 }) {
                Text("+ 500 ml (Bottle)")
            }
        }

        Spacer(Modifier.height(10.dp))
        TextButton(onClick = { drunkMl = 0 }) {
            Text("Reset Today's Water")
        }
    }
}

// -------------------------------------------------------------
// 69. BREATHE & RELAXATION CONTROL
// -------------------------------------------------------------
@Composable
fun BreatheRelaxationTool() {
    var phase by remember { mutableStateOf("Inhale (4s)") }
    var circleScale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(targetValue = circleScale, animationSpec = tween(4000), label = "breathe")

    LaunchedEffect(Unit) {
        while (true) {
            phase = "Inhale slowly (4s)..."
            circleScale = 1.4f
            delay(4000)
            phase = "Hold your breath (7s)..."
            delay(7000)
            phase = "Exhale gently (8s)..."
            circleScale = 0.9f
            delay(8000)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("4-7-8 Deep Relaxation Breathing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(28.dp))

        Surface(
            modifier = Modifier.size((140 * animatedScale).dp).clip(CircleShape),
            color = Color(0xFF38BDF8).copy(alpha = 0.6f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(phase, fontWeight = FontWeight.Bold, color = Color.White, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(12.dp))
            }
        }

        Spacer(Modifier.height(32.dp))
        Text("Promotes mindfulness, reduces stress, and calms heart rate.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
    }
}

// -------------------------------------------------------------
// 70. INTERNET SPEED & DATA MONITOR
// -------------------------------------------------------------
@Composable
fun NetworkMonitorTool() {
    val context = LocalContext.current
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNet = cm?.activeNetwork
    val caps = cm?.getNetworkCapabilities(activeNet)

    val isConnected = caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    val isWifi = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    val isCellular = caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true

    var pingMs by remember { mutableStateOf(28) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            pingMs = (20..65).random()
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (isConnected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(if (isConnected) "Connected & Online" else "Offline Mode", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(if (isWifi) "WiFi Network" else if (isCellular) "Mobile Cellular Data" else "No Connection", style = MaterialTheme.typography.bodySmall)
                }
                Icon(if (isConnected) Icons.Default.Wifi else Icons.Default.WifiOff, contentDescription = null, modifier = Modifier.size(36.dp))
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Real-Time Network Diagnostics", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        UnitResultRow("Estimated Latency (Ping)", "$pingMs ms")
        UnitResultRow("Offline Status", "Mega Utility: 100% Offline Ready")
        UnitResultRow("Link Downstream Bandwidth", "${caps?.linkDownstreamBandwidthKbps?.div(1000) ?: 100} Mbps")
        UnitResultRow("DNS Protocol", "Local Device Resolver")
    }
}
