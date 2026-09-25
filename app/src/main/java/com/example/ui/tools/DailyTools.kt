package com.example.ui.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

// -------------------------------------------------------------
// 1. SMART CALCULATOR
// -------------------------------------------------------------
@Composable
fun SmartCalculatorTool() {
    var expr by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }

    fun calculate(expression: String): String {
        return try {
            val sanitized = expression.replace("×", "*").replace("÷", "/")
            val parts = sanitized.split(Regex("(?<=[-+*/])|(?=[-+*/])")).filter { it.isNotBlank() }
            if (parts.isEmpty()) return "0"
            var total = parts[0].toDoubleOrNull() ?: 0.0
            var i = 1
            while (i < parts.size - 1) {
                val op = parts[i]
                val next = parts[i + 1].toDoubleOrNull() ?: 0.0
                when (op) {
                    "+" -> total += next
                    "-" -> total -= next
                    "*" -> total *= next
                    "/" -> if (next != 0.0) total /= next else return "Error"
                }
                i += 2
            }
            if (total % 1.0 == 0.0) total.toLong().toString() else "%.4f".format(total).trimEnd('0').trimEnd('.')
        } catch (e: Exception) {
            "Error"
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.End) {
                Text(text = if (expr.isEmpty()) "0" else expr, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Text(text = result, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(Modifier.height(16.dp))

        val buttons = listOf(
            listOf("C", "(", ")", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "⌫", "=")
        )

        buttons.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { btn ->
                    val isOp = btn in listOf("÷", "×", "-", "+", "=")
                    val isAction = btn in listOf("C", "⌫")
                    Button(
                        onClick = {
                            when (btn) {
                                "C" -> { expr = ""; result = "0" }
                                "⌫" -> { if (expr.isNotEmpty()) expr = expr.dropLast(1) }
                                "=" -> { result = calculate(expr) }
                                else -> {
                                    expr += btn
                                    if (!isOp) result = calculate(expr)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                btn == "=" -> MaterialTheme.colorScheme.primary
                                isOp -> MaterialTheme.colorScheme.secondaryContainer
                                isAction -> MaterialTheme.colorScheme.errorContainer
                                else -> MaterialTheme.colorScheme.surface
                            },
                            contentColor = when {
                                btn == "=" -> MaterialTheme.colorScheme.onPrimary
                                isOp -> MaterialTheme.colorScheme.onSecondaryContainer
                                isAction -> MaterialTheme.colorScheme.onErrorContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = btn, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. SCIENTIFIC CALCULATOR
// -------------------------------------------------------------
@Composable
fun ScientificCalculatorTool() {
    var input by remember { mutableStateOf("45") }
    var output by remember { mutableStateOf("") }
    var isRad by remember { mutableStateOf(false) }

    fun compute(func: String) {
        val num = input.toDoubleOrNull() ?: 0.0
        val angle = if (isRad) num else Math.toRadians(num)
        val res = when (func) {
            "sin" -> sin(angle)
            "cos" -> cos(angle)
            "tan" -> tan(angle)
            "sqrt" -> if (num >= 0) sqrt(num) else Double.NaN
            "log" -> if (num > 0) log10(num) else Double.NaN
            "ln" -> if (num > 0) ln(num) else Double.NaN
            "sqr" -> num * num
            "cube" -> num * num * num
            "inv" -> if (num != 0.0) 1.0 / num else Double.NaN
            "abs" -> abs(num)
            else -> num
        }
        output = if (res.isNaN()) "Error" else "%.5f".format(res).trimEnd('0').trimEnd('.')
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter Number / Angle") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilterChip(
                selected = !isRad,
                onClick = { isRad = false },
                label = { Text("Degrees (DEG)") }
            )
            Spacer(Modifier.width(8.dp))
            FilterChip(
                selected = isRad,
                onClick = { isRad = true },
                label = { Text("Radians (RAD)") }
            )
        }

        Spacer(Modifier.height(12.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Computed Result", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = if (output.isEmpty()) "Tap a function below" else output,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        val sciFuncs = listOf(
            listOf("sin", "cos", "tan"),
            listOf("sqrt", "log", "ln"),
            listOf("sqr", "cube", "inv")
        )

        sciFuncs.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { fn ->
                    Button(
                        onClick = { compute(fn) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(fn.uppercase(), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. AGE CALCULATOR
// -------------------------------------------------------------
@Composable
fun AgeCalculatorTool() {
    var birthDay by remember { mutableStateOf("15") }
    var birthMonth by remember { mutableStateOf("8") }
    var birthYear by remember { mutableStateOf("1998") }

    val calendar = Calendar.getInstance()
    val curYear = calendar.get(Calendar.YEAR)
    val curMonth = calendar.get(Calendar.MONTH) + 1
    val curDay = calendar.get(Calendar.DAY_OF_MONTH)

    var years by remember { mutableStateOf(0) }
    var months by remember { mutableStateOf(0) }
    var days by remember { mutableStateOf(0) }
    var nextBdayDays by remember { mutableStateOf(0) }

    LaunchedEffect(birthDay, birthMonth, birthYear) {
        val d = birthDay.toIntOrNull() ?: 1
        val m = birthMonth.toIntOrNull() ?: 1
        val y = birthYear.toIntOrNull() ?: 2000

        var ageY = curYear - y
        var ageM = curMonth - m
        var ageD = curDay - d

        if (ageD < 0) {
            ageM -= 1
            ageD += 30
        }
        if (ageM < 0) {
            ageY -= 1
            ageM += 12
        }

        years = max(0, ageY)
        months = max(0, ageM)
        days = max(0, ageD)

        // Next birthday
        val nextBday = Calendar.getInstance().apply {
            set(Calendar.MONTH, m - 1)
            set(Calendar.DAY_OF_MONTH, d)
            if (before(calendar)) add(Calendar.YEAR, 1)
        }
        val diffMs = nextBday.timeInMillis - calendar.timeInMillis
        nextBdayDays = max(0, (diffMs / (1000 * 60 * 60 * 24)).toInt())
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Enter Date of Birth", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = birthDay,
                onValueChange = { if (it.length <= 2) birthDay = it },
                label = { Text("Day") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = birthMonth,
                onValueChange = { if (it.length <= 2) birthMonth = it },
                label = { Text("Month") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = birthYear,
                onValueChange = { if (it.length <= 4) birthYear = it },
                label = { Text("Year") },
                modifier = Modifier.weight(1.5f)
            )
        }

        Spacer(Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Your Exact Age", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AgeStatBox("$years", "Years")
                    AgeStatBox("$months", "Months")
                    AgeStatBox("$days", "Days")
                }
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    "🎂 Next Birthday in $nextBdayDays days",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun AgeStatBox(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

// -------------------------------------------------------------
// 4. WORLD CLOCK
// -------------------------------------------------------------
@Composable
fun WorldClockTool() {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000)
        }
    }

    val cities = listOf(
        Pair("Dhaka, Bangladesh", "Asia/Dhaka"),
        Pair("London, United Kingdom", "Europe/London"),
        Pair("New York, USA", "America/New_York"),
        Pair("Tokyo, Japan", "Asia/Tokyo"),
        Pair("Dubai, UAE", "Asia/Dubai"),
        Pair("Sydney, Australia", "Australia/Sydney"),
        Pair("Paris, France", "Europe/Paris")
    )

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Global Timezones (Live)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 400.dp)) {
            items(cities) { (city, zoneId) ->
                val tz = TimeZone.getTimeZone(zoneId)
                val sdfTime = SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH).apply { timeZone = tz }
                val sdfDate = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH).apply { timeZone = tz }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(city, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                            Text(sdfDate.format(Date(currentTime)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        }
                        Text(
                            sdfTime.format(Date(currentTime)),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 5. QUICK NOTES & TO-DO
// -------------------------------------------------------------
data class NoteItem(val id: Long, val text: String, val isDone: Boolean = false)

@Composable
fun QuickNotesTool() {
    val context = LocalContext.current
    var notes by remember {
        mutableStateOf(
            listOf(
                NoteItem(1, "Check grocery list for today", false),
                NoteItem(2, "Review daily utility app features", true),
                NoteItem(3, "Read documentation and prayer time", false)
            )
        )
    }
    var newText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredNotes = remember(notes, searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) notes
        else notes.filter { it.text.lowercase().contains(q) }
    }

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    val sb = StringBuilder()
                    sb.append("==================================================\n")
                    sb.append("         MEGA UTILITY - QUICK NOTES & TO-DO       \n")
                    sb.append("==================================================\n")
                    sb.append("Export Date: ").append(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())).append("\n")
                    sb.append("Total Items: ").append(notes.size).append("\n")
                    sb.append("Completed:   ").append(notes.count { it.isDone }).append("\n")
                    sb.append("Pending:     ").append(notes.count { !it.isDone }).append("\n\n")
                    sb.append("----------------- TASK LIST ----------------------\n")
                    notes.forEachIndexed { index, note ->
                        val mark = if (note.isDone) "[x] " else "[ ] "
                        val status = if (note.isDone) "(Completed)" else "(Pending)"
                        sb.append("${index + 1}. $mark${note.text} $status\n")
                    }
                    sb.append("\n==================================================\n")
                    sb.append("Exported securely 100% offline from Mega Utility\n")
                    stream.write(sb.toString().toByteArray(Charsets.UTF_8))
                }
                Toast.makeText(context, "Notes downloaded as .txt successfully!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to export: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newText,
                onValueChange = { newText = it },
                label = { Text("Add quick note or task...") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (newText.isNotBlank()) {
                        notes = listOf(NoteItem(System.currentTimeMillis(), newText.trim(), false)) + notes
                        newText = ""
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Spacer(Modifier.height(10.dp))

        // Real-time Search Bar inside Quick Notes modal
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search notes in real-time...") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quick_notes_search_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (searchQuery.isNotBlank()) "Search Results (${filteredNotes.size} of ${notes.size})"
                else "Task List (${notes.size})",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedButton(
                onClick = {
                    if (notes.isEmpty()) {
                        Toast.makeText(context, "No notes to export!", Toast.LENGTH_SHORT).show()
                    } else {
                        val fileName = "Mega_Utility_Notes_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())}.txt"
                        exportLauncher.launch(fileName)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("download_as_text_button")
            ) {
                Icon(
                    Icons.Default.Download,
                    contentDescription = "Download Text",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Download as Text (.txt)", style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(Modifier.height(10.dp))

        if (filteredNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchQuery.isNotBlank()) "No notes matching \"$searchQuery\"" else "No notes added yet.",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 350.dp)) {
                items(filteredNotes, key = { it.id }) { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (note.isDone) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = note.isDone,
                                onCheckedChange = { checked ->
                                    notes = notes.map { if (it.id == note.id) it.copy(isDone = checked) else it }
                                }
                            )
                            Text(
                                text = note.text,
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (note.isDone) FontWeight.Normal else FontWeight.Medium
                            )
                            IconButton(onClick = { notes = notes.filter { it.id != note.id } }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. DIGITAL TASBIH
// -------------------------------------------------------------
@Composable
fun DigitalTasbihTool() {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }
    var count by remember { mutableStateOf(0) }
    var target by remember { mutableStateOf(33) }
    var lap by remember { mutableStateOf(0) }

    fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(40)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(33, 99, 100, 1000).forEach { t ->
                FilterChip(
                    selected = target == t,
                    onClick = { target = t },
                    label = { Text("Target: $t") }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Lap Count: $lap", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.secondary)
        Spacer(Modifier.height(8.dp))

        // Big Count display circle
        Surface(
            modifier = Modifier.size(190.dp).clip(CircleShape).clickable {
                count++
                vibrate()
                if (count >= target) {
                    lap++
                    count = 0
                }
            },
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 6.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text("of $target", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("TAP HERE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = { count = 0; lap = 0 },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Reset All")
            }
            Button(
                onClick = { if (count > 0) count-- },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Undo -1")
            }
        }
    }
}

// -------------------------------------------------------------
// 7. STOPWATCH
// -------------------------------------------------------------
@Composable
fun StopwatchTool() {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedMs by remember { mutableStateOf(0L) }
    var laps by remember { mutableStateOf(listOf<Long>()) }

    LaunchedEffect(isRunning) {
        var prev = System.currentTimeMillis()
        while (isRunning) {
            delay(10)
            val now = System.currentTimeMillis()
            elapsedMs += (now - prev)
            prev = now
        }
    }

    val minutes = (elapsedMs / 60000) % 60
    val seconds = (elapsedMs / 1000) % 60
    val millis = (elapsedMs % 1000) / 10

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "%02d:%02d.%02d".format(minutes, seconds, millis),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { isRunning = !isRunning },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }

            OutlinedButton(
                onClick = {
                    if (isRunning) {
                        laps = listOf(elapsedMs) + laps
                    } else {
                        elapsedMs = 0L
                        laps = emptyList()
                    }
                }
            ) {
                Text(if (isRunning) "Lap" else "Reset")
            }
        }

        Spacer(Modifier.height(16.dp))

        if (laps.isNotEmpty()) {
            Text("Lap Times (${laps.size})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)) {
                itemsIndexed(laps) { index, lapTime ->
                    val lapM = (lapTime / 60000) % 60
                    val lapS = (lapTime / 1000) % 60
                    val lapMs = (lapTime % 1000) / 10
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Lap ${laps.size - index}", color = MaterialTheme.colorScheme.outline)
                        Text("%02d:%02d.%02d".format(lapM, lapS, lapMs), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 8. TIMER
// -------------------------------------------------------------
@Composable
fun TimerTool() {
    var totalSeconds by remember { mutableStateOf(60) }
    var remainingSeconds by remember { mutableStateOf(60) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        } else if (remainingSeconds == 0) {
            isRunning = false
        }
    }

    val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds else 0f

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(60 to "1m", 180 to "3m", 300 to "5m", 600 to "10m").forEach { (sec, label) ->
                FilterChip(
                    selected = totalSeconds == sec,
                    onClick = {
                        totalSeconds = sec
                        remainingSeconds = sec
                        isRunning = false
                    },
                    label = { Text(label) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 10.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val m = remainingSeconds / 60
                val s = remainingSeconds % 60
                Text("%02d:%02d".format(m, s), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text(if (isRunning) "Remaining" else if (remainingSeconds == 0) "Finished!" else "Ready", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { isRunning = !isRunning }) {
                Text(if (isRunning) "Pause" else "Start Timer")
            }
            OutlinedButton(onClick = {
                isRunning = false
                remainingSeconds = totalSeconds
            }) {
                Text("Reset")
            }
        }
    }
}

// -------------------------------------------------------------
// 9. PASSWORD GENERATOR & VAULT
// -------------------------------------------------------------
@Composable
fun PasswordGeneratorTool() {
    val context = LocalContext.current
    var length by remember { mutableStateOf(14f) }
    var includeUpper by remember { mutableStateOf(true) }
    var includeLower by remember { mutableStateOf(true) }
    var includeNumbers by remember { mutableStateOf(true) }
    var includeSymbols by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }

    fun generate() {
        val uppers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lowers = "abcdefghijklmnopqrstuvwxyz"
        val numbers = "0123456789"
        val symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?"

        var pool = ""
        if (includeUpper) pool += uppers
        if (includeLower) pool += lowers
        if (includeNumbers) pool += numbers
        if (includeSymbols) pool += symbols

        if (pool.isEmpty()) pool = lowers

        val rand = java.security.SecureRandom()
        val result = StringBuilder()
        for (i in 0 until length.toInt()) {
            result.append(pool[rand.nextInt(pool.length)])
        }
        password = result.toString()
    }

    LaunchedEffect(Unit) { generate() }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = password,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText("Password", password))
                    Toast.makeText(context, "Password copied to clipboard!", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Password Length: ${length.toInt()}", fontWeight = FontWeight.SemiBold)
        Slider(
            value = length,
            onValueChange = { length = it; generate() },
            valueRange = 6f..32f,
            steps = 26
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Uppercase (A-Z)")
            Switch(checked = includeUpper, onCheckedChange = { includeUpper = it; generate() })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Lowercase (a-z)")
            Switch(checked = includeLower, onCheckedChange = { includeLower = it; generate() })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Numbers (0-9)")
            Switch(checked = includeNumbers, onCheckedChange = { includeNumbers = it; generate() })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Symbols (!@#)")
            Switch(checked = includeSymbols, onCheckedChange = { includeSymbols = it; generate() })
        }

        Spacer(Modifier.height(12.dp))
        Button(onClick = { generate() }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Generate New Password")
        }
    }
}

// -------------------------------------------------------------
// 10. RANDOM NUMBER & SPIN WHEEL
// -------------------------------------------------------------
@Composable
fun RandomSpinWheelTool() {
    var minVal by remember { mutableStateOf("1") }
    var maxVal by remember { mutableStateOf("100") }
    var generatedNumber by remember { mutableStateOf<Int?>(null) }

    var diceValue by remember { mutableStateOf(1) }
    var decisionResult by remember { mutableStateOf("YES") }
    var spinAngle by remember { mutableStateOf(0f) }
    val animatedAngle by animateFloatAsState(targetValue = spinAngle, animationSpec = tween(600), label = "spin")

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Text("1. Random Number Generator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = minVal, onValueChange = { minVal = it }, label = { Text("Min") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = maxVal, onValueChange = { maxVal = it }, label = { Text("Max") }, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                val min = minVal.toIntOrNull() ?: 1
                val max = maxVal.toIntOrNull() ?: 100
                if (max >= min) generatedNumber = (min..max).random()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pick Random Number")
        }
        if (generatedNumber != null) {
            Text(
                "Result: $generatedNumber",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text("2. Roll Dice & Decision Spinner", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("$diceValue", fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { diceValue = (1..6).random() }) {
                    Text("Roll Dice")
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape,
                    modifier = Modifier.size(72.dp).rotate(animatedAngle)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(decisionResult, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    spinAngle += 720f
                    decisionResult = listOf("YES", "NO", "MAYBE", "TRY AGAIN").random()
                }) {
                    Text("Spin Wheel")
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 11. POS / CASH DENOMINATION COUNTER
// -------------------------------------------------------------
@Composable
fun CashDenominationTool() {
    val denominations = listOf(1000, 500, 200, 100, 50, 20, 10, 5, 2, 1)
    val counts = remember { mutableStateMapOf<Int, Int>() }

    denominations.forEach { if (!counts.containsKey(it)) counts[it] = 0 }

    val totalAmount = denominations.sumOf { (counts[it] ?: 0) * it }
    val totalNotes = denominations.sumOf { counts[it] ?: 0 }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Cash Amount", style = MaterialTheme.typography.labelMedium)
                    Text("৳ $totalAmount", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Text("Total Notes: $totalNotes", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { denominations.forEach { counts[it] = 0 } }) {
                Icon(Icons.Default.Clear, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Clear All")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.heightIn(max = 350.dp)) {
            items(denominations) { denom ->
                val currentCount = counts[denom] ?: 0
                val subtotal = currentCount * denom
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("৳ $denom", fontWeight = FontWeight.Bold, modifier = Modifier.width(64.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (currentCount > 0) counts[denom] = currentCount - 1 }) {
                                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Minus")
                            }
                            Text("$currentCount", modifier = Modifier.width(36.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { counts[denom] = currentCount + 1 }) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = "Add")
                            }
                        }
                        Text("= ৳ $subtotal", fontWeight = FontWeight.SemiBold, modifier = Modifier.width(80.dp), textAlign = TextAlign.End)
                    }
                }
            }
        }
    }
}
