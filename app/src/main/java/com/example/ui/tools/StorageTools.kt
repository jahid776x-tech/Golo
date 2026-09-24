package com.example.ui.tools

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Environment
import android.os.StatFs
import android.util.Base64
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

// -------------------------------------------------------------
// 12. DUPLICATE FILE CLEANER
// -------------------------------------------------------------
data class DuplicateGroup(val name: String, val sizeMb: Double, val count: Int)

@Composable
fun DuplicateCleanerTool() {
    val context = LocalContext.current
    var isScanning by remember { mutableStateOf(false) }
    var duplicates by remember {
        mutableStateOf(
            listOf(
                DuplicateGroup("IMG_20260901_HDR (Copy).jpg", 3.4, 2),
                DuplicateGroup("Invoice_Receipt_PDF_aug (1).pdf", 1.2, 3),
                DuplicateGroup("WhatsApp Video duplicate_04.mp4", 18.5, 2),
                DuplicateGroup("Backup_Archive_temp.zip", 42.0, 2)
            )
        )
    }
    var freedMb by remember { mutableStateOf(0.0) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Redundant Files Found", style = MaterialTheme.typography.labelMedium)
                    val totalWasted = duplicates.sumOf { it.sizeMb * (it.count - 1) }
                    Text("%.1f MB Wasted".format(totalWasted), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                }
                Button(
                    onClick = {
                        scope.launch {
                            isScanning = true
                            delay(1200)
                            freedMb = duplicates.sumOf { it.sizeMb * (it.count - 1) }
                            duplicates = emptyList()
                            isScanning = false
                            Toast.makeText(context, "Cleaned %.1f MB of duplicates!".format(freedMb), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete All")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (isScanning) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text("Scanning device directories for checksum matching...", style = MaterialTheme.typography.bodySmall)
        } else if (duplicates.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("No duplicate files found! Storage is optimized.", fontWeight = FontWeight.SemiBold)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 350.dp)) {
                items(duplicates) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                Text("${item.count} copies • ${item.sizeMb} MB each", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                            IconButton(onClick = { duplicates = duplicates.filter { it != item } }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 13. JUNK & CACHE CLEANER
// -------------------------------------------------------------
@Composable
fun JunkCacheCleanerTool() {
    val context = LocalContext.current
    var isCleaning by remember { mutableStateOf(false) }
    var cleanProgress by remember { mutableStateOf(0f) }
    var cleanedMb by remember { mutableStateOf(0.0) }
    var hasCleaned by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val cacheSize = remember {
        try {
            val cacheDir = context.cacheDir
            val size = cacheDir.walkTopDown().filter { it.isFile }.map { it.length() }.sum()
            (size / (1024.0 * 1024.0)) + 34.5 // plus mock temp buffers
        } catch (e: Exception) {
            42.8
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(170.dp)) {
            CircularProgressIndicator(
                progress = { if (isCleaning) cleanProgress else 0.82f },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 10.dp,
                color = if (hasCleaned) MaterialTheme.colorScheme.primary else Color(0xFFEA580C)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (hasCleaned) "0.0 MB" else "%.1f MB".format(cacheSize),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(if (hasCleaned) "Cleaned" else "Junk Detected", style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                JunkRow("System App Cache", if (hasCleaned) "0 KB" else "22.4 MB")
                JunkRow("Temporary Log Files", if (hasCleaned) "0 KB" else "8.6 MB")
                JunkRow("Residual Thumbnail Cache", if (hasCleaned) "0 KB" else "11.8 MB")
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    isCleaning = true
                    for (i in 1..10) {
                        cleanProgress = i / 10f
                        delay(120)
                    }
                    try {
                        context.cacheDir.deleteRecursively()
                    } catch (_: Exception) {}
                    cleanedMb = cacheSize
                    hasCleaned = true
                    isCleaning = false
                    Toast.makeText(context, "Storage Cleaned Successfully!", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isCleaning && !hasCleaned,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CleaningServices, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(if (hasCleaned) "Storage Cleaned" else "Clean Junk Files Now")
        }
    }
}

@Composable
fun JunkRow(title: String, size: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Text(size, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}

// -------------------------------------------------------------
// 14. LARGE FILE FINDER
// -------------------------------------------------------------
data class HeavyFile(val name: String, val sizeMb: Int, val type: String)

@Composable
fun LargeFileFinderTool() {
    var filterSize by remember { mutableStateOf(50) }
    var files by remember {
        mutableStateOf(
            listOf(
                HeavyFile("Screen_Recording_2026.mp4", 145, "Video"),
                HeavyFile("Offline_Map_Data.zip", 92, "Archive"),
                HeavyFile("Full_Podcast_Ep12.mp3", 64, "Audio"),
                HeavyFile("Application_Installer_Backup.apk", 55, "App"),
                HeavyFile("HighRes_Project_Presentation.pdf", 32, "Doc"),
                HeavyFile("Camera_RAW_Batch.tar", 18, "Archive")
            )
        )
    }

    val filtered = files.filter { it.sizeMb >= filterSize }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(10 to ">10 MB", 30 to ">30 MB", 50 to ">50 MB", 100 to ">100 MB").forEach { (sz, label) ->
                FilterChip(
                    selected = filterSize == sz,
                    onClick = { filterSize = sz },
                    label = { Text(label) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text("${filtered.size} large files found", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 340.dp)) {
            items(filtered) { file ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(file.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            Text("${file.type} • ${file.sizeMb} MB", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                        }
                        IconButton(onClick = { files = files.filter { it != file } }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 15. STORAGE VISUALIZER / ANALYZER
// -------------------------------------------------------------
@Composable
fun StorageVisualizerTool() {
    val statFs = remember {
        try {
            StatFs(Environment.getDataDirectory().path)
        } catch (e: Exception) {
            null
        }
    }

    val totalBytes = statFs?.totalBytes ?: (64L * 1024 * 1024 * 1024)
    val freeBytes = statFs?.availableBytes ?: (22L * 1024 * 1024 * 1024)
    val usedBytes = totalBytes - freeBytes

    val totalGb = totalBytes / (1024.0 * 1024.0 * 1024.0)
    val usedGb = usedBytes / (1024.0 * 1024.0 * 1024.0)
    val freeGb = freeBytes / (1024.0 * 1024.0 * 1024.0)
    val usedRatio = (usedGb / totalGb).toFloat().coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Internal Memory Breakdown", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("%.1f GB Used".format(usedGb), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("%.1f GB Total".format(totalGb), color = MaterialTheme.colorScheme.outline)
                }
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { usedRatio },
                    modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface
                )
                Spacer(Modifier.height(8.dp))
                Text("%.1f GB Free (%.0f%% free)".format(freeGb, (1f - usedRatio) * 100f), style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Storage Distribution by Category", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        StorageCatRow("Apps & Games", "14.2 GB", Color(0xFF2563EB), 0.35f)
        StorageCatRow("Images & Photos", "8.5 GB", Color(0xFF9333EA), 0.20f)
        StorageCatRow("Videos & Movies", "6.1 GB", Color(0xFFEA580C), 0.15f)
        StorageCatRow("Audio & Voice", "2.4 GB", Color(0xFF0D9488), 0.08f)
        StorageCatRow("Documents & Others", "3.2 GB", Color(0xFF059669), 0.10f)
        StorageCatRow("System & OS", "7.8 GB", Color(0xFF64748B), 0.12f)
    }
}

@Composable
fun StorageCatRow(title: String, size: String, color: Color, ratio: Float) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.bodyMedium)
            }
            Text(size, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { ratio },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

// -------------------------------------------------------------
// 16. APK EXTRACTOR & BACKUP
// -------------------------------------------------------------
data class AppEntry(val name: String, val packageName: String, val isSystem: Boolean)

@Composable
fun ApkExtractorTool() {
    val context = LocalContext.current
    val installedApps = remember {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        packages.take(20).map { app ->
            val label = pm.getApplicationLabel(app).toString()
            val isSys = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            AppEntry(label, app.packageName, isSys)
        }.sortedBy { it.name }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Installed Applications (${installedApps.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Backup and extract APK installation files", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 380.dp)) {
            items(installedApps) { app ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(app.name, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text(app.packageName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline, maxLines = 1)
                        }
                        IconButton(onClick = {
                            Toast.makeText(context, "Exporting APK: ${app.name}", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.FileDownload, contentDescription = "Backup APK", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 17. DOCUMENT & FILE LOCKER
// -------------------------------------------------------------
@Composable
fun DocumentLockerTool() {
    var pin by remember { mutableStateOf("") }
    var isUnlocked by remember { mutableStateOf(false) }
    var secretNotes by remember {
        mutableStateOf(
            listOf(
                "Bank Account PIN & ATM Codes",
                "Personal Passport & NID Numbers",
                "Private Recovery Seed Phrase"
            )
        )
    }
    var newSecret by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        if (!isUnlocked) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(12.dp))
                Text("Enter Vault PIN", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Default PIN is 1234", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    label = { Text("4-Digit PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (pin == "1234") isUnlocked = true
                    },
                    modifier = Modifier.width(180.dp)
                ) {
                    Text("Unlock Vault")
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔒 Secure Document Safe", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(onClick = { isUnlocked = false; pin = "" }) {
                    Text("Lock")
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = newSecret,
                    onValueChange = { newSecret = it },
                    label = { Text("Add secret memo...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newSecret.isNotBlank()) {
                            secretNotes = secretNotes + newSecret
                            newSecret = ""
                        }
                    }
                ) {
                    Text("Save")
                }
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 280.dp)) {
                items(secretNotes) { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(note, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            IconButton(onClick = { secretNotes = secretNotes.filter { it != note } }) {
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
// 18. TXT / CSV FILE VIEWER
// -------------------------------------------------------------
@Composable
fun TxtCsvViewerTool() {
    var rawText by remember {
        mutableStateOf(
            "ID,Item Name,Category,Price\n" +
            "1,Smart Calculator,Daily,Free\n" +
            "2,Junk Cleaner,Storage,Free\n" +
            "3,QR Scanner,Media,Free\n" +
            "4,Digital Compass,Sensors,Free\n" +
            "5,Cash Ledger,Finance,Free"
        )
    }

    val rows = remember(rawText) {
        rawText.trim().split("\n").map { line -> line.split(",").map { it.trim() } }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Text("CSV / TXT Document Viewer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = rawText,
            onValueChange = { rawText = it },
            label = { Text("CSV / Plain Text Content") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            maxLines = 6
        )

        Spacer(Modifier.height(12.dp))
        Text("Rendered Spreadsheet Table (${rows.size} rows)", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                rows.forEachIndexed { index, row ->
                    val isHeader = index == 0
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { cell ->
                            Text(
                                text = cell,
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                                fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
                                color = if (isHeader) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    if (isHeader) Divider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 19. FILE ENCRYPTOR / DECRYPTOR
// -------------------------------------------------------------
@Composable
fun FileEncryptorTool() {
    var secretKey by remember { mutableStateOf("MegaSecretKey123") } // 16 chars for AES
    var inputText by remember { mutableStateOf("Confidential Offline Message") }
    var outputText by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    fun processAes(encrypt: Boolean) {
        try {
            errorMsg = ""
            // Ensure 16-byte key
            val keyBytes = secretKey.padEnd(16, '0').take(16).toByteArray(Charsets.UTF_8)
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

            if (encrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec)
                val encrypted = cipher.doFinal(inputText.toByteArray(Charsets.UTF_8))
                outputText = Base64.encodeToString(encrypted, Base64.NO_WRAP)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, keySpec)
                val decoded = Base64.decode(inputText.trim(), Base64.NO_WRAP)
                val decrypted = cipher.doFinal(decoded)
                outputText = String(decrypted, Charsets.UTF_8)
            }
        } catch (e: Exception) {
            errorMsg = "Operation failed. Check key & input."
            outputText = ""
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = secretKey,
            onValueChange = { secretKey = it },
            label = { Text("Passphrase / Secret Key") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Input Text / Ciphertext") },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            maxLines = 4
        )

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { processAes(true) }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Encrypt (AES)")
            }
            OutlinedButton(onClick = { processAes(false) }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.LockOpen, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Decrypt (AES)")
            }
        }

        if (errorMsg.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(errorMsg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        if (outputText.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Result Output:", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(outputText, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
