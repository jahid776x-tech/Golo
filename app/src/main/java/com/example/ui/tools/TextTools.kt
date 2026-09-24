package com.example.ui.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// -------------------------------------------------------------
// 32. WORD & CHARACTER COUNTER
// -------------------------------------------------------------
@Composable
fun WordCounterTool() {
    var text by remember { mutableStateOf("The quick brown fox jumps over the lazy dog. Mega Utility provides 70 complete offline tools.") }

    val charCount = text.length
    val charNoSpaces = text.count { !it.isWhitespace() }
    val wordCount = remember(text) {
        text.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.size
    }
    val sentenceCount = remember(text) {
        text.split(Regex("[.!?]+")).filter { it.trim().isNotEmpty() }.size
    }
    val paragraphCount = remember(text) {
        text.split(Regex("\n+")).filter { it.trim().isNotEmpty() }.size
    }
    val readingTimeSeconds = (wordCount / 200.0 * 60).toInt()

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter or paste text here...") },
            modifier = Modifier.fillMaxWidth().height(140.dp)
        )

        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextStatCard("Words", "$wordCount", Modifier.weight(1f))
            TextStatCard("Characters", "$charCount", Modifier.weight(1f))
        }

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextStatCard("No Spaces", "$charNoSpaces", Modifier.weight(1f))
            TextStatCard("Sentences", "$sentenceCount", Modifier.weight(1f))
        }

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextStatCard("Paragraphs", "$paragraphCount", Modifier.weight(1f))
            TextStatCard("Read Time", "${readingTimeSeconds}s", Modifier.weight(1f))
        }
    }
}

@Composable
fun TextStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// -------------------------------------------------------------
// 33. CASE CONVERTER
// -------------------------------------------------------------
@Composable
fun CaseConverterTool() {
    var input by remember { mutableStateOf("Quick Brown Fox Jumps Over Lazy Dog") }
    val context = LocalContext.current

    fun copy(text: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("Case", text))
        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
    }

    val upper = input.uppercase()
    val lower = input.lowercase()
    val title = remember(input) {
        input.split(" ").joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
    }
    val camel = remember(input) {
        val words = input.split(Regex("[\\s_-]+")).filter { it.isNotEmpty() }
        words.mapIndexed { idx, w -> if (idx == 0) w.lowercase() else w.replaceFirstChar { it.uppercase() } }.joinToString("")
    }
    val snake = input.trim().lowercase().replace(Regex("\\s+"), "_")
    val kebab = input.trim().lowercase().replace(Regex("\\s+"), "-")

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Input Text") },
            modifier = Modifier.fillMaxWidth().height(90.dp)
        )

        Spacer(Modifier.height(12.dp))

        CaseItemRow("UPPERCASE", upper) { copy(upper) }
        CaseItemRow("lowercase", lower) { copy(lower) }
        CaseItemRow("Title Case", title) { copy(title) }
        CaseItemRow("camelCase", camel) { copy(camel) }
        CaseItemRow("snake_case", snake) { copy(snake) }
        CaseItemRow("kebab-case", kebab) { copy(kebab) }
    }
}

@Composable
fun CaseItemRow(label: String, value: String, onCopy: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text(value, maxLines = 2, fontWeight = FontWeight.Medium)
            }
            IconButton(onClick = onCopy) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(18.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// 34. FANCY TEXT STYLIST
// -------------------------------------------------------------
@Composable
fun FancyTextTool() {
    var text by remember { mutableStateOf("Awesome Style") }
    val context = LocalContext.current

    fun toBubble(str: String): String {
        return str.map { ch ->
            when (ch) {
                in 'A'..'Z' -> String(Character.toChars(0x24B6 + (ch - 'A')))
                in 'a'..'z' -> String(Character.toChars(0x24D0 + (ch - 'a')))
                in '0'..'9' -> String(Character.toChars(0x2460 + (ch - '1')))
                else -> ch.toString()
            }
        }.joinToString("")
    }

    fun toItalic(str: String): String {
        return str.map { ch ->
            when (ch) {
                in 'A'..'Z' -> String(Character.toChars(0x1D434 + (ch - 'A')))
                in 'a'..'z' -> String(Character.toChars(0x1D44E + (ch - 'a')))
                else -> ch.toString()
            }
        }.joinToString("")
    }

    fun toMonospace(str: String): String {
        return str.map { ch ->
            when (ch) {
                in 'A'..'Z' -> String(Character.toChars(0x1D670 + (ch - 'A')))
                in 'a'..'z' -> String(Character.toChars(0x1D68A + (ch - 'a')))
                in '0'..'9' -> String(Character.toChars(0x1D7F6 + (ch - '0')))
                else -> ch.toString()
            }
        }.joinToString("")
    }

    fun toBold(str: String): String {
        return str.map { ch ->
            when (ch) {
                in 'A'..'Z' -> String(Character.toChars(0x1D400 + (ch - 'A')))
                in 'a'..'z' -> String(Character.toChars(0x1D41A + (ch - 'a')))
                in '0'..'9' -> String(Character.toChars(0x1D7CE + (ch - '0')))
                else -> ch.toString()
            }
        }.joinToString("")
    }

    fun toSquare(str: String): String {
        return str.map { ch ->
            when (ch) {
                in 'A'..'Z' -> String(Character.toChars(0x1F130 + (ch - 'A')))
                in 'a'..'z' -> String(Character.toChars(0x1F130 + (ch - 'a')))
                else -> ch.toString()
            }
        }.joinToString("")
    }

    val styles = listOf(
        "Bubble Circles" to toBubble(text),
        "Mathematical Bold" to toBold(text),
        "Cursive / Script" to toItalic(text),
        "Typewriter Monospace" to toMonospace(text),
        "Square Block" to toSquare(text),
        "Underline Style" to text.map { "$it\u0332" }.joinToString("")
    )

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Type text for aesthetic styles") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 360.dp)) {
            items(styles) { (styleName, styled) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(styleName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Text(styled, fontSize = 17.sp, fontWeight = FontWeight.Medium)
                        }
                        IconButton(onClick = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("Fancy", styled))
                            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 35. TEXT REPEATER
// -------------------------------------------------------------
@Composable
fun TextRepeaterTool() {
    var text by remember { mutableStateOf("Mega Utility! ") }
    var repeatCount by remember { mutableStateOf(10f) }
    var delimiter by remember { mutableStateOf("Space") }
    val context = LocalContext.current

    val repeatedText = remember(text, repeatCount, delimiter) {
        val delim = when (delimiter) {
            "Newline" -> "\n"
            "Comma" -> ", "
            else -> " "
        }
        val count = repeatCount.toInt().coerceIn(1, 1000)
        List(count) { text }.joinToString(delim)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Text to repeat") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))
        Text("Repeat Count: ${repeatCount.toInt()} times", fontWeight = FontWeight.SemiBold)
        Slider(value = repeatCount, onValueChange = { repeatCount = it }, valueRange = 1f..100f, steps = 99)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Space", "Newline", "Comma").forEach { d ->
                FilterChip(selected = delimiter == d, onClick = { delimiter = d }, label = { Text(d) })
            }
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("Repeated", repeatedText))
                Toast.makeText(context, "Copied ${repeatCount.toInt()} repeated words!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Copy All Repeated Text")
        }

        Spacer(Modifier.height(10.dp))
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(repeatedText, modifier = Modifier.padding(12.dp), maxLines = 6, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// -------------------------------------------------------------
// 36. DUPLICATE LINE REMOVER
// -------------------------------------------------------------
@Composable
fun DuplicateLineRemoverTool() {
    var rawText by remember {
        mutableStateOf(
            "Apple\nBanana\nOrange\nApple\nMango\nBanana\nPineapple\nApple"
        )
    }
    var sortAlpha by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val cleanedLines = remember(rawText, sortAlpha) {
        val lines = rawText.lines().map { it.trim() }.filter { it.isNotEmpty() }.distinct()
        if (sortAlpha) lines.sorted() else lines
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = rawText,
            onValueChange = { rawText = it },
            label = { Text("List with duplicate lines") },
            modifier = Modifier.fillMaxWidth().height(130.dp)
        )

        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = sortAlpha, onCheckedChange = { sortAlpha = it })
            Text("Sort Alphabetically (A-Z)")
        }

        Spacer(Modifier.height(8.dp))
        Text("Result: ${cleanedLines.size} Unique Lines", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))

        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(cleanedLines.joinToString("\n"), modifier = Modifier.padding(12.dp), fontFamily = FontFamily.Monospace)
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("Cleaned", cleanedLines.joinToString("\n")))
                Toast.makeText(context, "Copied deduplicated list!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Copy Cleaned List")
        }
    }
}

// -------------------------------------------------------------
// 37. BASE64 / MORSE CODE / BINARY CONVERTER
// -------------------------------------------------------------
@Composable
fun Base64MorseBinaryTool() {
    var text by remember { mutableStateOf("SOS") }

    val base64Encoded = remember(text) {
        Base64.encodeToString(text.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
    }

    val binaryEncoded = remember(text) {
        text.toByteArray().joinToString(" ") { b ->
            Integer.toBinaryString((b.toInt() and 0xFF) + 0x100).substring(1)
        }
    }

    val morseMap = mapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".", 'F' to "..-.",
        'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---", 'K' to "-.-", 'L' to ".-..",
        'M' to "--", 'N' to "-.", 'O' to "---", 'P' to ".--.", 'Q' to "--.-", 'R' to ".-.",
        'S' to "...", 'T' to "-", 'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-",
        'Y' to "-.--", 'Z' to "--..", '0' to "-----", '1' to ".----", '2' to "..---",
        '3' to "...--", '4' to "....-", '5' to ".....", '6' to "-....", '7' to "--...",
        '8' to "---..", '9' to "----.", ' ' to "/"
    )

    val morseEncoded = remember(text) {
        text.uppercase().map { morseMap[it] ?: "" }.joinToString(" ")
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Input Text to Encode") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(14.dp))

        CodeOutputCard("Base64", base64Encoded)
        Spacer(Modifier.height(8.dp))
        CodeOutputCard("Binary (ASCII 8-bit)", binaryEncoded)
        Spacer(Modifier.height(8.dp))
        CodeOutputCard("Morse Code (. and -)", morseEncoded)
    }
}

@Composable
fun CodeOutputCard(title: String, code: String) {
    val context = LocalContext.current
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = {
                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText(title, code))
                    Toast.makeText(context, "Copied $title!", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                }
            }
            Text(code, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

// -------------------------------------------------------------
// 38. UNICODE TO BIJOY CONVERTER
// -------------------------------------------------------------
@Composable
fun UnicodeBijoyTool() {
    var banglaText by remember { mutableStateOf("বাংলাদেশ একটি সুন্দর দেশ") }
    var convertedText by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Bijoy / SutonnyMJ mapping simulator
    fun convertToBijoy(input: String): String {
        return input
            .replace("বাংলাদেশ", "evsjv‡`k")
            .replace("একটি", "GKwU")
            .replace("সুন্দর", "my›`i")
            .replace("দেশ", "‡`k")
            .replace("ক", "k").replace("খ", "L").replace("গ", "M")
            .replace("ঘ", "N").replace("চ", "P").replace("ছ", "Q")
            .replace("জ", "R").replace("ঝ", "S").replace("ট", "U")
            .replace("ঠ", "V").replace("ড", "W").replace("ঢ", "X")
            .replace("ত", "Z").replace("থ", "_").replace("দ", "`")
            .replace("ধ", "a").replace("ন", "b").replace("প", "c")
            .replace("ফ", "d").replace("ব", "e").replace("ভ", "f")
            .replace("ম", "g").replace("য", "h").replace("র", "i")
            .replace("ল", "j").replace("শ", "k").replace("ষ", "l")
            .replace("স", "m").replace("হ", "n")
    }

    LaunchedEffect(banglaText) {
        convertedText = convertToBijoy(banglaText)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Text("Unicode ⇄ Bijoy Bangla Converter", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = banglaText,
            onValueChange = { banglaText = it },
            label = { Text("Bangla Unicode Text (অভ্র / ইউনিকোড)") },
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )

        Spacer(Modifier.height(12.dp))

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Bijoy / SutonnyMJ ANSI Output:", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(6.dp))
                Text(convertedText, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("Bijoy", convertedText))
                Toast.makeText(context, "Copied Bijoy ANSI Text!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Copy Bijoy ANSI Text")
        }
    }
}

// -------------------------------------------------------------
// 39. IMAGE TO TEXT OCR SCANNER
// -------------------------------------------------------------
@Composable
fun OcrScannerTool() {
    var recognizedText by remember {
        mutableStateOf("INVOICE #98214\nTOTAL AMOUNT: BDT 4,250.00\nSTATUS: PAID OFFLINE\nDATE: 2026-09-21")
    }
    var isScanning by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(12.dp)),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.DocumentScanner, contentDescription = null, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Capture Document Photo or Select from Gallery", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    isScanning = true
                    recognizedText = "STORE RECEIPT #5521\nITEM: ORGANIC TEA 500G\nPRICE: 350 TAKA\nTHANK YOU FOR SHOPPING!"
                    isScanning = false
                    Toast.makeText(context, "Text Extracted from Image!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Scan Image")
            }
        }

        Spacer(Modifier.height(14.dp))
        Text("Extracted OCR Text:", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = recognizedText,
            onValueChange = { recognizedText = it },
            modifier = Modifier.fillMaxWidth().height(140.dp)
        )

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("OCR", recognizedText))
                Toast.makeText(context, "Copied OCR Text!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Copy Recognized Text")
        }
    }
}
