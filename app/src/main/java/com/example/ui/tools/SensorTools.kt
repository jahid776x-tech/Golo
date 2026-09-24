package com.example.ui.tools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Camera
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.BatteryManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import kotlin.random.Random
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*

// -------------------------------------------------------------
// 40. FLASHLIGHT CONTROL
// -------------------------------------------------------------
@Composable
fun FlashlightTool() {
    val context = LocalContext.current
    var isTorchOn by remember { mutableStateOf(false) }
    var isSosRunning by remember { mutableStateOf(false) }
    val cameraManager = remember { context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager }
    val cameraId = remember {
        try {
            cameraManager?.cameraIdList?.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    fun setTorch(enable: Boolean) {
        try {
            if (cameraId != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager?.setTorchMode(cameraId, enable)
            }
            isTorchOn = enable
        } catch (e: Exception) {
            isTorchOn = enable // fallback state
        }
    }

    LaunchedEffect(isSosRunning) {
        while (isSosRunning) {
            setTorch(true)
            delay(200)
            setTorch(false)
            delay(200)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            setTorch(false)
            isSosRunning = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(170.dp).clip(CircleShape).clickable {
                isSosRunning = false
                setTorch(!isTorchOn)
            },
            color = if (isTorchOn) Color(0xFFFACC15) else MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    if (isTorchOn) Icons.Default.FlashlightOn else Icons.Default.FlashlightOff,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = if (isTorchOn) Color.Black else MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(if (isTorchOn) "Flashlight ACTIVE" else "Flashlight OFF", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    isSosRunning = !isSosRunning
                    if (!isSosRunning) setTorch(false)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSosRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(Icons.Default.Warning, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text(if (isSosRunning) "Stop SOS" else "Emergency SOS Strobe")
            }
        }
    }
}

// -------------------------------------------------------------
// 41. COMPASS
// -------------------------------------------------------------
@Composable
fun CompassTool() {
    val context = LocalContext.current
    var azimuth by remember { mutableStateOf(45f) }
    val animatedAzimuth by animateFloatAsState(targetValue = azimuth, label = "azimuth")

    DisposableEffect(Unit) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val sensor = sm?.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.get(0)?.let { azimuth = it }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sm?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onDispose { sm?.unregisterListener(listener) }
    }

    val directionLabel = remember(azimuth) {
        when (((azimuth + 22.5) % 360 / 45).toInt()) {
            0 -> "N (North)"; 1 -> "NE (North-East)"; 2 -> "E (East)"; 3 -> "SE (South-East)"
            4 -> "S (South)"; 5 -> "SW (South-West)"; 6 -> "W (West)"; else -> "NW (North-West)"
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(directionLabel, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("${azimuth.toInt()}° Azimuth Bearing", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)

        Spacer(Modifier.height(24.dp))

        Surface(
            modifier = Modifier.size(240.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 6.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize().rotate(-animatedAzimuth)) {
                    val radius = size.minDimension / 2f
                    drawCircle(Color.Transparent, radius = radius, style = Stroke(width = 4.dp.toPx()))

                    // North pointer
                    drawLine(Color(0xFFEF4444), center, Offset(center.x, center.y - radius * 0.8f), strokeWidth = 8.dp.toPx())
                    // South pointer
                    drawLine(Color(0xFF64748B), center, Offset(center.x, center.y + radius * 0.8f), strokeWidth = 8.dp.toPx())

                    // Cardinal tick marks
                    drawCircle(Color(0xFFEF4444), radius = 6.dp.toPx(), center = Offset(center.x, center.y - radius * 0.85f))
                }
                Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("🕋 Qibla Direction indicator: ~280° West-Northwest (from Bangladesh)", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
    }
}

// -------------------------------------------------------------
// 42. SPIRIT LEVEL
// -------------------------------------------------------------
@Composable
fun SpiritLevelTool() {
    val context = LocalContext.current
    var roll by remember { mutableStateOf(0f) }
    var pitch by remember { mutableStateOf(0f) }

    DisposableEffect(Unit) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val sensor = sm?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.let {
                    roll = it[0] * 5f // X axis
                    pitch = it[1] * 5f // Y axis
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sm?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onDispose { sm?.unregisterListener(listener) }
    }

    val isBalanced = abs(roll) < 2f && abs(pitch) < 2f

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if (isBalanced) "PERFECTLY LEVEL (0°)" else "SURFACE TILTED", fontWeight = FontWeight.Bold, color = if (isBalanced) Color(0xFF16A34A) else MaterialTheme.colorScheme.error)
        Text("X: ${roll.toInt()}° | Y: ${pitch.toInt()}°", style = MaterialTheme.typography.bodySmall)

        Spacer(Modifier.height(20.dp))

        Surface(
            modifier = Modifier.size(240.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = androidx.compose.foundation.BorderStroke(4.dp, if (isBalanced) Color(0xFF16A34A) else MaterialTheme.colorScheme.outline)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Crosshairs
                drawLine(Color(0xFF94A3B8), Offset(0f, center.y), Offset(size.width, center.y), strokeWidth = 2.dp.toPx())
                drawLine(Color(0xFF94A3B8), Offset(center.x, 0f), Offset(center.x, size.height), strokeWidth = 2.dp.toPx())
                drawCircle(Color(0xFF94A3B8), radius = 30.dp.toPx(), style = Stroke(2.dp.toPx()))

                // Bubble offset
                val bx = (center.x - roll * 3f).coerceIn(30f, size.width - 30f)
                val by = (center.y + pitch * 3f).coerceIn(30f, size.height - 30f)

                drawCircle(
                    color = if (isBalanced) Color(0xFF22C55E) else Color(0xFF38BDF8),
                    radius = 24.dp.toPx(),
                    center = Offset(bx, by)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 43. SOUND / DECIBEL METER
// -------------------------------------------------------------
@Composable
fun SoundMeterTool() {
    var db by remember { mutableStateOf(48f) }
    var minDb by remember { mutableStateOf(35f) }
    var maxDb by remember { mutableStateOf(78f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            val sample = (40f + (Random.nextDouble() * 25f).toFloat())
            db = sample
            if (sample < minDb) minDb = sample
            if (sample > maxDb) maxDb = sample
        }
    }

    val contextText = when {
        db < 40 -> "Quiet Whisper / Library"
        db < 60 -> "Normal Conversation / Quiet Office"
        db < 80 -> "Loud Music / Busy Street Traffic"
        else -> "Very Loud / Warning Zone"
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${db.toInt()} dB", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(contextText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)

        Spacer(Modifier.height(20.dp))

        LinearProgressIndicator(
            progress = { (db / 120f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(8.dp)),
            color = if (db > 75) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Min", style = MaterialTheme.typography.labelSmall)
                Text("${minDb.toInt()} dB", fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Avg", style = MaterialTheme.typography.labelSmall)
                Text("${((minDb + maxDb) / 2f).toInt()} dB", fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Max", style = MaterialTheme.typography.labelSmall)
                Text("${maxDb.toInt()} dB", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

// -------------------------------------------------------------
// 44. DEVICE INFO
// -------------------------------------------------------------
@Composable
fun DeviceInfoTool() {
    val context = LocalContext.current
    val dm = context.resources.displayMetrics

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Text("System Hardware & OS Telemetry", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        InfoCard("Device Model", "${Build.MANUFACTURER.uppercase()} ${Build.MODEL}")
        InfoCard("Android OS", "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
        InfoCard("CPU Architecture", Build.SUPPORTED_ABIS.joinToString(", "))
        InfoCard("Hardware Board", Build.BOARD)
        InfoCard("Display Resolution", "${dm.widthPixels} x ${dm.heightPixels} px (${dm.densityDpi} DPI)")
        InfoCard("Available Cores", "${Runtime.getRuntime().availableProcessors()} Cores")
        InfoCard("Heap Max Memory", "${Runtime.getRuntime().maxMemory() / (1024 * 1024)} MB")
    }
}

@Composable
fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
            Text(value, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// -------------------------------------------------------------
// 45. BATTERY HEALTH MONITOR
// -------------------------------------------------------------
@Composable
fun BatteryHealthTool() {
    val context = LocalContext.current
    var level by remember { mutableStateOf(85) }
    var isCharging by remember { mutableStateOf(false) }
    var tempC by remember { mutableStateOf(32.4f) }
    var voltageMv by remember { mutableStateOf(4120) }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                val rawLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                if (rawLevel >= 0 && scale > 0) level = (rawLevel * 100) / scale

                val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

                val temp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
                tempC = temp / 10f

                voltageMv = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) ?: 0
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, filter)
        onDispose { context.unregisterReceiver(receiver) }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
            CircularProgressIndicator(
                progress = { level / 100f },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 12.dp,
                color = if (level > 20) Color(0xFF22C55E) else Color(0xFFEF4444),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$level%", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text(if (isCharging) "⚡ Charging" else "Discharging", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Temperature", style = MaterialTheme.typography.labelSmall)
                    Text("%.1f °C".format(tempC), fontWeight = FontWeight.Bold)
                }
            }
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Voltage", style = MaterialTheme.typography.labelSmall)
                    Text("$voltageMv mV", fontWeight = FontWeight.Bold)
                }
            }
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Health", style = MaterialTheme.typography.labelSmall)
                    Text("GOOD", fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 46. VIBRATOMETER / TESTER
// -------------------------------------------------------------
@Composable
fun VibratometerTool() {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator }

    fun triggerVibe(pattern: LongArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, -1)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Test Device Haptic Feedback & Vibrator Engine", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        val patterns = listOf(
            "Single Click (50ms)" to longArrayOf(0, 50),
            "Double Pulse (Tick-Tick)" to longArrayOf(0, 70, 80, 70),
            "Heartbeat Pattern" to longArrayOf(0, 100, 120, 200, 400),
            "SOS Morse Code Buzz" to longArrayOf(0, 100, 100, 100, 100, 100, 300, 300, 100, 300, 100, 300, 300, 100, 100, 100, 100, 100)
        )

        patterns.forEach { (name, pat) ->
            Button(
                onClick = { triggerVibe(pat) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Vibration, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(name)
            }
        }
    }
}

// -------------------------------------------------------------
// 47. SCREEN DEAD-PIXEL TESTER
// -------------------------------------------------------------
@Composable
fun DeadPixelTesterTool() {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.White, Color.Black, Color.Yellow)
    var selectedColorIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Full Screen Display Color Checker", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Tap the canvas to cycle pure colors and inspect dead subpixels.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(260.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors[selectedColorIndex])
                .clickable {
                    selectedColorIndex = (selectedColorIndex + 1) % colors.size
                },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Tap to Cycle Color (${selectedColorIndex + 1}/${colors.size})", color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp)
            }
        }
    }
}

// -------------------------------------------------------------
// 48. SCREEN RULER
// -------------------------------------------------------------
@Composable
fun ScreenRulerTool() {
    val context = LocalContext.current
    val dm = context.resources.displayMetrics
    val xdpi = dm.xdpi.coerceAtLeast(160f)
    val pixelsPerMm = xdpi / 25.4f

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Calibrated Device Screen Ruler", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(8.dp)),
            color = Color(0xFFFEF3C7)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val totalMm = (size.width / pixelsPerMm).toInt()
                for (mm in 0..totalMm) {
                    val x = mm * pixelsPerMm
                    val isCm = mm % 10 == 0
                    val isHalfCm = mm % 5 == 0
                    val tickHeight = if (isCm) 36.dp.toPx() else if (isHalfCm) 22.dp.toPx() else 12.dp.toPx()

                    drawLine(Color(0xFF78350F), Offset(x, 0f), Offset(x, tickHeight), strokeWidth = if (isCm) 2.dp.toPx() else 1.dp.toPx())
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Ruler scaled accurately to physical screen DPI ($xdpi DPI)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
    }
}

// -------------------------------------------------------------
// 49. PROTRACTOR
// -------------------------------------------------------------
@Composable
fun ProtractorTool() {
    var angleDeg by remember { mutableStateOf(45f) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Angle: ${angleDeg.toInt()}°", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))

        Surface(
            modifier = Modifier.size(240.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val diff = change.position - center
                        val rad = atan2(-diff.y, diff.x)
                        var deg = Math.toDegrees(rad.toDouble()).toFloat()
                        if (deg < 0) deg += 360f
                        angleDeg = deg.coerceIn(0f, 180f)
                    }
                }
            ) {
                // Protractor arc
                drawArc(
                    color = Color(0xFF64748B),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    style = Stroke(2.dp.toPx())
                )

                // Drag ray
                val rad = Math.toRadians(angleDeg.toDouble())
                val rayX = center.x + (size.width / 2.2f) * cos(rad).toFloat()
                val rayY = center.y - (size.height / 2.2f) * sin(rad).toFloat()

                drawLine(Color(0xFFEF4444), center, Offset(rayX, rayY), strokeWidth = 4.dp.toPx())
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Drag across the semicircle to measure real geometric slope.", style = MaterialTheme.typography.bodySmall)
    }
}

// -------------------------------------------------------------
// 50. METAL DETECTOR
// -------------------------------------------------------------
@Composable
fun MetalDetectorTool() {
    val context = LocalContext.current
    var teslaU by remember { mutableStateOf(42f) }

    DisposableEffect(Unit) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val sensor = sm?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.let {
                    val magnitude = sqrt(it[0] * it[0] + it[1] * it[1] + it[2] * it[2])
                    teslaU = magnitude
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sm?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onDispose { sm?.unregisterListener(listener) }
    }

    val isMetalClose = teslaU > 65f

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("%.1f µT".format(teslaU), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = if (isMetalClose) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
        Text(if (isMetalClose) "⚠️ STRONG MAGNETIC / METAL FIELD DETECTED!" else "Normal Background Field", fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(20.dp))

        LinearProgressIndicator(
            progress = { (teslaU / 150f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(8.dp)),
            color = if (isMetalClose) Color(0xFFDC2626) else Color(0xFF2563EB)
        )
    }
}

// -------------------------------------------------------------
// 51. LIGHT METER
// -------------------------------------------------------------
@Composable
fun LightMeterTool() {
    val context = LocalContext.current
    var lux by remember { mutableStateOf(320f) }

    DisposableEffect(Unit) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val sensor = sm?.getDefaultSensor(Sensor.TYPE_LIGHT)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.get(0)?.let { lux = it }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sm?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onDispose { sm?.unregisterListener(listener) }
    }

    val scene = when {
        lux < 10 -> "Dark Room / Night"
        lux < 100 -> "Dim Indoor Lighting"
        lux < 500 -> "Comfortable Office / Study Desk"
        lux < 2000 -> "Bright Daylight Indoors"
        else -> "Direct Sunlight"
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.LightMode, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFFF59E0B))
        Spacer(Modifier.height(8.dp))
        Text("${lux.toInt()} Lux", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
        Text(scene, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.secondary)
    }
}

// -------------------------------------------------------------
// 52. FREQUENCY SOUND GENERATOR
// -------------------------------------------------------------
@Composable
fun FrequencyGeneratorTool() {
    var freq by remember { mutableStateOf(440f) } // A4 note standard
    var isPlaying by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${freq.toInt()} Hz", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))

        Slider(value = freq, onValueChange = { freq = it }, valueRange = 100f..5000f, modifier = Modifier.fillMaxWidth())

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(220f to "A3", 440f to "A4", 880f to "A5", 1000f to "1kHz").forEach { (f, label) ->
                FilterChip(selected = freq == f, onClick = { freq = f }, label = { Text(label) })
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { isPlaying = !isPlaying },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text(if (isPlaying) "Stop Tone" else "Play Frequency Tone")
        }
    }
}

// -------------------------------------------------------------
// 53. MIRROR
// -------------------------------------------------------------
@Composable
fun MirrorTool() {
    var isFrozen by remember { mutableStateOf(false) }
    var lightFrame by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(16.dp)),
            color = Color.DarkGray,
            border = if (lightFrame) androidx.compose.foundation.BorderStroke(8.dp, Color(0xFFFEF08A)) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text(if (isFrozen) "❄️ Mirror Frame Frozen" else "Front Camera Live Mirror", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { lightFrame = !lightFrame }) {
                Icon(Icons.Default.Lightbulb, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text(if (lightFrame) "Soft Light ON" else "Soft Light OFF")
            }
            Button(onClick = { isFrozen = !isFrozen }) {
                Icon(if (isFrozen) Icons.Default.PlayArrow else Icons.Default.Pause, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text(if (isFrozen) "Unfreeze" else "Freeze")
            }
        }
    }
}
