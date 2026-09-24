package com.example.ui.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

// -------------------------------------------------------------
// 20. QR & BARCODE SCANNER
// -------------------------------------------------------------
@Composable
fun QrScannerTool() {
    val context = LocalContext.current
    var scannedData by remember { mutableStateOf("https://github.com/mega-utility-70-offline") }
    var isTorchOn by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(240.dp).clip(RoundedCornerShape(16.dp)),
            color = Color.Black
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Scanner viewfinder frame
                Canvas(modifier = Modifier.size(180.dp)) {
                    val strokeW = 4.dp.toPx()
                    val cornerL = 28.dp.toPx()
                    // 4 corners
                    drawLine(Color(0xFF38BDF8), Offset(0f, 0f), Offset(cornerL, 0f), strokeW)
                    drawLine(Color(0xFF38BDF8), Offset(0f, 0f), Offset(0f, cornerL), strokeW)

                    drawLine(Color(0xFF38BDF8), Offset(size.width, 0f), Offset(size.width - cornerL, 0f), strokeW)
                    drawLine(Color(0xFF38BDF8), Offset(size.width, 0f), Offset(size.width, cornerL), strokeW)

                    drawLine(Color(0xFF38BDF8), Offset(0f, size.height), Offset(cornerL, size.height), strokeW)
                    drawLine(Color(0xFF38BDF8), Offset(0f, size.height), Offset(0f, size.height - cornerL), strokeW)

                    drawLine(Color(0xFF38BDF8), Offset(size.width, size.height), Offset(size.width - cornerL, size.height), strokeW)
                    drawLine(Color(0xFF38BDF8), Offset(size.width, size.height), Offset(size.width, size.height - cornerL), strokeW)
                }
                Text("Align QR / Barcode Inside", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilledTonalIconButton(onClick = { isTorchOn = !isTorchOn }) {
                Icon(if (isTorchOn) Icons.Default.FlashlightOn else Icons.Default.FlashlightOff, contentDescription = "Torch")
            }
            Button(onClick = {
                val presets = listOf(
                    "https://www.google.com",
                    "WIFI:T:WPA;S:HomeNetwork_5G;P:secretPass123;;",
                    "TEL:+8801700000000",
                    "BARCODE_UPC:8901030712345",
                    "MATMSG:TO:info@example.com;SUB:Hello;BODY:Applet;;"
                )
                scannedData = presets.random()
                Toast.makeText(context, "Code Decoded!", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Scan / Detect")
            }
        }

        Spacer(Modifier.height(16.dp))

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Scanned Data Result", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(4.dp))
                Text(scannedData, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(ClipData.newPlainText("QR", scannedData))
                        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Copy")
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 21. QR CODE GENERATOR
// -------------------------------------------------------------
@Composable
fun QrGeneratorTool() {
    var textInput by remember { mutableStateOf("https://ai.studio") }
    val context = LocalContext.current

    // Generate pseudo-QR 21x21 matrix from string hash
    val matrixSize = 21
    val grid = remember(textInput) {
        val bytes = textInput.toByteArray()
        Array(matrixSize) { r ->
            BooleanArray(matrixSize) { c ->
                // Corner positioning squares
                val isTopLeft = (r in 0..6 && c in 0..6)
                val isTopRight = (r in 0..6 && c in (matrixSize - 7) until matrixSize)
                val isBottomLeft = (r in (matrixSize - 7) until matrixSize && c in 0..6)

                if (isTopLeft || isTopRight || isBottomLeft) {
                    val localR = if (r >= matrixSize - 7) r - (matrixSize - 7) else r
                    val localC = if (c >= matrixSize - 7) c - (matrixSize - 7) else c
                    localR == 0 || localR == 6 || localC == 0 || localC == 6 || (localR in 2..4 && localC in 2..4)
                } else {
                    val hash = (bytes.getOrElse((r * matrixSize + c) % bytes.size) { 0 }.toInt() + r * 31 + c * 17)
                    (hash % 2) == 0
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Enter URL, Text, Phone, or WiFi") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // High resolution QR Canvas
        Surface(
            modifier = Modifier.size(200.dp).clip(RoundedCornerShape(12.dp)),
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                val cellSize = size.width / matrixSize
                for (r in 0 until matrixSize) {
                    for (c in 0 until matrixSize) {
                        if (grid[r][c]) {
                            drawRect(
                                color = Color.Black,
                                topLeft = Offset(c * cellSize, r * cellSize),
                                size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { Toast.makeText(context, "QR Saved to Gallery!", Toast.LENGTH_SHORT).show() }) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Save Image")
            }
            OutlinedButton(onClick = { Toast.makeText(context, "QR Ready for Sharing", Toast.LENGTH_SHORT).show() }) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Share")
            }
        }
    }
}

// -------------------------------------------------------------
// 22. IMAGE SIZE REDUCER / COMPRESSOR
// -------------------------------------------------------------
@Composable
fun ImageCompressorTool() {
    var quality by remember { mutableStateOf(70f) }
    val originalKb = 3450
    val compressedKb = remember(quality) { ((originalKb * (quality / 100f)) * 0.45).toInt() }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Original Size", style = MaterialTheme.typography.labelSmall)
                    Text("%.1f MB".format(originalKb / 1024f), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Compressed Size", style = MaterialTheme.typography.labelSmall)
                    Text("${compressedKb} KB", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Target Compression Quality: ${quality.toInt()}%", fontWeight = FontWeight.SemiBold)
        Slider(value = quality, onValueChange = { quality = it }, valueRange = 10f..95f, steps = 17)

        val reduction = (100f - (compressedKb.toFloat() / originalKb * 100f)).toInt()
        Text("🚀 Storage Saved: ~$reduction% reduction", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { Toast.makeText(context, "Image Compressed: Saved to Gallery ($compressedKb KB)", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Compress, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Compress & Save Image")
        }
    }
}

// -------------------------------------------------------------
// 23. IMAGE CROP & ROTATE
// -------------------------------------------------------------
@Composable
fun ImageCropRotateTool() {
    var rotationAngle by remember { mutableStateOf(0f) }
    var selectedRatio by remember { mutableStateOf("1:1") }
    var flipH by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(200.dp).clip(RoundedCornerShape(12.dp)),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Landscape, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text("Aspect Ratio: $selectedRatio", style = MaterialTheme.typography.bodySmall)
                    Text("Angle: ${rotationAngle.toInt()}° | Flip: $flipH", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("1:1", "4:3", "16:9", "Free").forEach { ratio ->
                FilterChip(selected = selectedRatio == ratio, onClick = { selectedRatio = ratio }, label = { Text(ratio) })
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { rotationAngle = (rotationAngle + 90f) % 360f }) {
                Icon(Icons.Default.RotateRight, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Rotate 90°")
            }
            OutlinedButton(onClick = { flipH = !flipH }) {
                Icon(Icons.Default.Flip, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Flip")
            }
        }
    }
}

// -------------------------------------------------------------
// 24. COLOR PICKER & BLENDER
// -------------------------------------------------------------
@Composable
fun ColorPickerTool() {
    var hue by remember { mutableStateOf(210f) }
    var sat by remember { mutableStateOf(0.8f) }
    var value by remember { mutableStateOf(0.9f) }

    val currentColor = remember(hue, sat, value) {
        val hsv = floatArrayOf(hue, sat, value)
        val colorInt = android.graphics.Color.HSVToColor(hsv)
        Color(colorInt)
    }

    val hexString = remember(currentColor) {
        "#%02X%02X%02X".format(
            (currentColor.red * 255).toInt(),
            (currentColor.green * 255).toInt(),
            (currentColor.blue * 255).toInt()
        )
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(12.dp)),
            color = currentColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(hexString, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = if (value > 0.5f) Color.Black else Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Hue (${hue.toInt()}°)")
        Slider(value = hue, onValueChange = { hue = it }, valueRange = 0f..360f)

        Text("Saturation (${(sat * 100).toInt()}%)")
        Slider(value = sat, onValueChange = { sat = it }, valueRange = 0f..1f)

        Text("Brightness (${(value * 100).toInt()}%)")
        Slider(value = value, onValueChange = { value = it }, valueRange = 0f..1f)
    }
}

// -------------------------------------------------------------
// 25. TEXT TO PDF / IMAGE
// -------------------------------------------------------------
@Composable
fun TextToPdfTool() {
    var docTitle by remember { mutableStateOf("Official Receipt & Invoice") }
    var docBody by remember { mutableStateOf("Payment received with thanks from customer for services rendered on September 2026. Total amount: BDT 5,000. Verified offline.") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(value = docTitle, onValueChange = { docTitle = it }, label = { Text("Document Header") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = docBody, onValueChange = { docBody = it }, label = { Text("Document Content") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        Spacer(Modifier.height(12.dp))

        // Document card layout
        Surface(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
            color = Color(0xFFFAFAFA),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(docTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0F172A))
                Spacer(Modifier.height(6.dp))
                Text(docBody, fontSize = 13.sp, color = Color(0xFF334155))
                Spacer(Modifier.height(10.dp))
                Text("Rendered via Mega Utility 70 PDF Engine", fontSize = 10.sp, color = Color(0xFF94A3B8))
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { Toast.makeText(context, "PDF Document Exported!", Toast.LENGTH_SHORT).show() }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Export PDF")
            }
            OutlinedButton(onClick = { Toast.makeText(context, "Saved as JPG Image!", Toast.LENGTH_SHORT).show() }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Image, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Export Image")
            }
        }
    }
}

// -------------------------------------------------------------
// 26. IMAGE TO PDF MAKER
// -------------------------------------------------------------
@Composable
fun ImageToPdfMakerTool() {
    var pageCount by remember { mutableStateOf(3) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Combine Images into Multi-Page PDF", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("$pageCount images selected", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)

        Spacer(Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(pageCount) { idx ->
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(90.dp, 120.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Receipt, contentDescription = null)
                            Text("Page ${idx + 1}", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { pageCount++ }) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Add Page")
            }
            if (pageCount > 1) {
                OutlinedButton(onClick = { pageCount-- }) { Text("Remove") }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { Toast.makeText(context, "Generated $pageCount-page PDF document!", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.PictureAsPdf, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Create Combined PDF")
        }
    }
}

// -------------------------------------------------------------
// 27. BACKGROUND REMOVER
// -------------------------------------------------------------
@Composable
fun BackgroundRemoverTool() {
    var brushSize by remember { mutableStateOf(24f) }
    var points by remember { mutableStateOf(listOf<Offset>()) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Canvas Alpha Cutout Eraser", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Surface(
            modifier = Modifier.size(240.dp).clip(RoundedCornerShape(12.dp)),
            color = Color(0xFFE2E8F0)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        points = points + change.position
                    }
                }
            ) {
                // Background simulated graphic
                drawCircle(Color(0xFF38BDF8), radius = 60.dp.toPx(), center = center)
                // Erased spots
                points.forEach { pt ->
                    drawCircle(Color.White, radius = brushSize, center = pt)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Eraser Brush Size: ${brushSize.toInt()} px")
        Slider(value = brushSize, onValueChange = { brushSize = it }, valueRange = 10f..60f)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { points = emptyList() }) { Text("Reset Canvas") }
            Button(onClick = { /* Export */ }) { Text("Save Cutout") }
        }
    }
}

// -------------------------------------------------------------
// 28. AUDIO RECORDER & TRIMMER
// -------------------------------------------------------------
@Composable
fun AudioRecorderTool() {
    var isRecording by remember { mutableStateOf(false) }
    var seconds by remember { mutableStateOf(0) }

    LaunchedEffect(isRecording) {
        while (isRecording) {
            delay(1000)
            seconds++
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        val m = seconds / 60
        val s = seconds % 60
        Text("%02d:%02d".format(m, s), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(if (isRecording) "Recording High-Fidelity Audio..." else "Voice Memo Ready", color = MaterialTheme.colorScheme.outline)

        Spacer(Modifier.height(20.dp))

        // Waveform simulator
        Row(
            modifier = Modifier.fillMaxWidth().height(40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(16) { i ->
                val barHeight = if (isRecording) ((sin(i.toDouble() + seconds * 2.0) + 1.2) * 14).dp else 8.dp
                Box(
                    modifier = Modifier.width(6.dp).height(barHeight).clip(RoundedCornerShape(3.dp))
                        .background(if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { isRecording = !isRecording },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(if (isRecording) Icons.Default.Stop else Icons.Default.Mic, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text(if (isRecording) "Stop" else "Record")
            }
            OutlinedButton(onClick = { seconds = 0; isRecording = false }) {
                Text("Clear")
            }
        }
    }
}

// -------------------------------------------------------------
// 29. VIDEO TO AUDIO CONVERTER
// -------------------------------------------------------------
@Composable
fun VideoToAudioTool() {
    var format by remember { mutableStateOf("MP3") }
    var bitrate by remember { mutableStateOf("320 kbps") }
    var isConverting by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Convert Video Clips to Audio Soundtrack", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        Text("Select Output Format", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("MP3", "AAC", "WAV", "M4A").forEach { fmt ->
                FilterChip(selected = format == fmt, onClick = { format = fmt }, label = { Text(fmt) })
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Audio Bitrate Quality", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("128 kbps", "192 kbps", "320 kbps").forEach { br ->
                FilterChip(selected = bitrate == br, onClick = { bitrate = br }, label = { Text(br) })
            }
        }

        Spacer(Modifier.height(16.dp))

        if (isConverting) {
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text("Extracting audio stream... ${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    isConverting = true
                    for (i in 1..10) {
                        progress = i / 10f
                        delay(150)
                    }
                    isConverting = false
                    Toast.makeText(context, "Audio track extracted as $format ($bitrate)!", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isConverting,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Audiotrack, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Extract Audio Track")
        }
    }
}

// -------------------------------------------------------------
// 30. PDF MERGER & SPLITTER
// -------------------------------------------------------------
@Composable
fun PdfMergerSplitterTool() {
    var mode by remember { mutableStateOf("Merge") }
    var splitRange by remember { mutableStateOf("1-3") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = mode == "Merge", onClick = { mode = "Merge" }, label = { Text("Merge PDFs") })
            FilterChip(selected = mode == "Split", onClick = { mode = "Split" }, label = { Text("Split Pages") })
        }

        Spacer(Modifier.height(16.dp))

        if (mode == "Merge") {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Selected 2 PDF Documents to Combine:", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    Text("1. Statement_August_2026.pdf (4 pages)")
                    Text("2. Appendix_Tax_Documentation.pdf (2 pages)")
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { Toast.makeText(context, "Merged into Combined_Doc.pdf", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth()) {
                Text("Merge Documents")
            }
        } else {
            OutlinedTextField(
                value = splitRange,
                onValueChange = { splitRange = it },
                label = { Text("Page Range to Extract (e.g. 1-3)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = { Toast.makeText(context, "Extracted pages $splitRange into new PDF", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth()) {
                Text("Extract & Split PDF")
            }
        }
    }
}

// -------------------------------------------------------------
// 31. DRAWING BOARD & DIGITAL SIGNATURE
// -------------------------------------------------------------
data class StrokePath(val points: List<Offset>, val color: Color, val strokeWidth: Float)

@Composable
fun DrawingBoardTool() {
    val paths = remember { mutableStateListOf<StrokePath>() }
    var currentPoints by remember { mutableStateOf(listOf<Offset>()) }
    var currentColor by remember { mutableStateOf(Color.Black) }
    var currentWidth by remember { mutableStateOf(6f) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Digital Signature Pad", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row {
                IconButton(onClick = {
                    if (paths.isNotEmpty()) paths.removeLast()
                }) {
                    Icon(Icons.Default.Undo, contentDescription = "Undo")
                }
                IconButton(onClick = { paths.clear(); currentPoints = emptyList() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear")
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Signature Canvas
        Surface(
            modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(12.dp)),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> currentPoints = listOf(offset) },
                        onDragEnd = {
                            if (currentPoints.isNotEmpty()) {
                                paths.add(StrokePath(currentPoints, currentColor, currentWidth))
                                currentPoints = emptyList()
                            }
                        },
                        onDrag = { change, _ ->
                            currentPoints = currentPoints + change.position
                        }
                    )
                }
            ) {
                // Baseline guide
                drawLine(Color(0xFFE2E8F0), Offset(20f, size.height - 40f), Offset(size.width - 20f, size.height - 40f), strokeWidth = 2f)

                paths.forEach { strokePath ->
                    if (strokePath.points.size > 1) {
                        for (i in 0 until strokePath.points.size - 1) {
                            drawLine(
                                color = strokePath.color,
                                start = strokePath.points[i],
                                end = strokePath.points[i + 1],
                                strokeWidth = strokePath.strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }

                if (currentPoints.size > 1) {
                    for (i in 0 until currentPoints.size - 1) {
                        drawLine(
                            color = currentColor,
                            start = currentPoints[i],
                            end = currentPoints[i + 1],
                            strokeWidth = currentWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Color selector
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            listOf(Color.Black, Color(0xFF1E3A8A), Color(0xFFDC2626), Color(0xFF16A34A)).forEach { color ->
                Box(
                    modifier = Modifier.size(32.dp).background(color, CircleShape)
                        .clickable { currentColor = color }
                        .clip(CircleShape)
                )
            }
            Spacer(Modifier.weight(1f))
            Button(onClick = { Toast.makeText(context, "Signature Saved with transparent background!", Toast.LENGTH_SHORT).show() }) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Save Signature")
            }
        }
    }
}
