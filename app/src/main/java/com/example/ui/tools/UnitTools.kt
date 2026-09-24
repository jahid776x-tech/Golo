package com.example.ui.tools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// -------------------------------------------------------------
// 54. LENGTH CONVERTER
// -------------------------------------------------------------
@Composable
fun LengthConverterTool() {
    var inputVal by remember { mutableStateOf("1") }
    var inputUnit by remember { mutableStateOf("Meters") }

    val meters = remember(inputVal, inputUnit) {
        val v = inputVal.toDoubleOrNull() ?: 0.0
        when (inputUnit) {
            "Meters" -> v
            "Kilometers" -> v * 1000.0
            "Centimeters" -> v / 100.0
            "Feet" -> v * 0.3048
            "Inches" -> v * 0.0254
            "Yards" -> v * 0.9144
            "Miles" -> v * 1609.34
            else -> v
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = inputVal,
            onValueChange = { inputVal = it },
            label = { Text("Length Value") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Meters", "Kilometers", "Feet", "Inches", "Miles").forEach { u ->
                FilterChip(selected = inputUnit == u, onClick = { inputUnit = u }, label = { Text(u) })
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Conversion Results", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        UnitResultRow("Meters (m)", "%.4f m".format(meters))
        UnitResultRow("Kilometers (km)", "%.6f km".format(meters / 1000.0))
        UnitResultRow("Centimeters (cm)", "%.2f cm".format(meters * 100.0))
        UnitResultRow("Feet (ft)", "%.4f ft".format(meters / 0.3048))
        UnitResultRow("Inches (in)", "%.2f in".format(meters / 0.0254))
        UnitResultRow("Yards (yd)", "%.4f yd".format(meters / 0.9144))
        UnitResultRow("Miles (mi)", "%.6f mi".format(meters / 1609.34))
    }
}

@Composable
fun UnitResultRow(unit: String, result: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(unit, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
            Text(result, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// -------------------------------------------------------------
// 55. WEIGHT & MASS CONVERTER
// -------------------------------------------------------------
@Composable
fun WeightConverterTool() {
    var inputVal by remember { mutableStateOf("1") }
    var inputUnit by remember { mutableStateOf("Kilograms") }

    val kg = remember(inputVal, inputUnit) {
        val v = inputVal.toDoubleOrNull() ?: 0.0
        when (inputUnit) {
            "Kilograms" -> v
            "Grams" -> v / 1000.0
            "Pounds (lbs)" -> v * 0.453592
            "Ounces (oz)" -> v * 0.0283495
            "Metric Tons" -> v * 1000.0
            else -> v
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = inputVal,
            onValueChange = { inputVal = it },
            label = { Text("Mass / Weight") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Kilograms", "Grams", "Pounds (lbs)", "Ounces (oz)").forEach { u ->
                FilterChip(selected = inputUnit == u, onClick = { inputUnit = u }, label = { Text(u) })
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Converted Units", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        UnitResultRow("Kilograms (kg)", "%.4f kg".format(kg))
        UnitResultRow("Grams (g)", "%.2f g".format(kg * 1000.0))
        UnitResultRow("Pounds (lbs)", "%.4f lbs".format(kg / 0.453592))
        UnitResultRow("Ounces (oz)", "%.2f oz".format(kg / 0.0283495))
        UnitResultRow("Metric Tons (t)", "%.6f t".format(kg / 1000.0))
    }
}

// -------------------------------------------------------------
// 56. TEMPERATURE CONVERTER
// -------------------------------------------------------------
@Composable
fun TemperatureConverterTool() {
    var celsiusVal by remember { mutableStateOf("25") }
    val c = celsiusVal.toDoubleOrNull() ?: 0.0
    val f = (c * 9.0 / 5.0) + 32.0
    val k = c + 273.15

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = celsiusVal,
            onValueChange = { celsiusVal = it },
            label = { Text("Temperature in Celsius (°C)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🌡️ Temperature Scales", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Celsius: %.1f °C".format(c), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Fahrenheit: %.1f °F".format(f), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Kelvin: %.2f K".format(k), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// -------------------------------------------------------------
// 57. LAND / AREA MEASUREMENT CALCULATOR (BANGLADESH & MODERN)
// -------------------------------------------------------------
@Composable
fun LandMeasurementTool() {
    var sqFtVal by remember { mutableStateOf("14400") } // Default 1 Bigha in Bangladesh
    val sqFt = sqFtVal.toDoubleOrNull() ?: 0.0

    val shatak = sqFt / 435.6 // 1 Shatak / Decimal = 435.6 sq ft
    val katha = sqFt / 720.0  // 1 Katha = 720 sq ft
    val bigha = katha / 20.0  // 1 Bigha = 20 Katha = 14,400 sq ft
    val acre = sqFt / 43560.0 // 1 Acre = 43,560 sq ft
    val sqMeter = sqFt * 0.092903

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Text("Traditional & Modern Land Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("জমি ও ভূমির সঠিক হিসাব (কাঠা, বিঘা, শতক)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = sqFtVal,
            onValueChange = { sqFtVal = it },
            label = { Text("Total Area in Square Feet (বর্গফুট)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        UnitResultRow("শতক / ডেসিমেল (Shatak)", "%.2f শতক".format(shatak))
        UnitResultRow("কাঠা (Katha)", "%.2f কাঠা".format(katha))
        UnitResultRow("বিঘা (Bigha)", "%.3f বিঘা".format(bigha))
        UnitResultRow("একর (Acre)", "%.4f একর".format(acre))
        UnitResultRow("বর্গমিটার (Square Meter)", "%.2f m²".format(sqMeter))
    }
}

// -------------------------------------------------------------
// 58. DATA STORAGE CONVERTER
// -------------------------------------------------------------
@Composable
fun DataStorageConverterTool() {
    var mbVal by remember { mutableStateOf("1024") }
    val mb = mbVal.toDoubleOrNull() ?: 0.0

    val bytes = mb * 1024.0 * 1024.0
    val kb = mb * 1024.0
    val gb = mb / 1024.0
    val tb = gb / 1024.0

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = mbVal,
            onValueChange = { mbVal = it },
            label = { Text("Value in Megabytes (MB)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        UnitResultRow("Kilobytes (KB)", "%.0f KB".format(kb))
        UnitResultRow("Gigabytes (GB)", "%.3f GB".format(gb))
        UnitResultRow("Terabytes (TB)", "%.5f TB".format(tb))
        UnitResultRow("Total Bytes", "%.0f Bytes".format(bytes))
    }
}

// -------------------------------------------------------------
// 59. SPEED CONVERTER
// -------------------------------------------------------------
@Composable
fun SpeedConverterTool() {
    var kmhVal by remember { mutableStateOf("60") }
    val kmh = kmhVal.toDoubleOrNull() ?: 0.0

    val ms = kmh / 3.6
    val mph = kmh * 0.621371
    val knots = kmh * 0.539957

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = kmhVal,
            onValueChange = { kmhVal = it },
            label = { Text("Speed in km/h") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        UnitResultRow("Meters per second (m/s)", "%.2f m/s".format(ms))
        UnitResultRow("Miles per hour (mph)", "%.2f mph".format(mph))
        UnitResultRow("Knots (kn)", "%.2f knots".format(knots))
    }
}

// -------------------------------------------------------------
// 60. VOLUME CONVERTER
// -------------------------------------------------------------
@Composable
fun VolumeConverterTool() {
    var litersVal by remember { mutableStateOf("5") }
    val liters = litersVal.toDoubleOrNull() ?: 0.0

    val ml = liters * 1000.0
    val usGallons = liters * 0.264172
    val cups = liters * 4.22675

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = litersVal,
            onValueChange = { litersVal = it },
            label = { Text("Volume in Liters (L)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        UnitResultRow("Milliliters (ml)", "%.0f ml".format(ml))
        UnitResultRow("US Gallons (gal)", "%.3f gal".format(usGallons))
        UnitResultRow("Cooking Cups", "%.2f cups".format(cups))
    }
}

// -------------------------------------------------------------
// 61. FUEL & MILEAGE CALCULATOR
// -------------------------------------------------------------
@Composable
fun FuelMileageTool() {
    var distanceKm by remember { mutableStateOf("120") }
    var fuelLiters by remember { mutableStateOf("8") }
    var pricePerLiter by remember { mutableStateOf("135") }

    val dist = distanceKm.toDoubleOrNull() ?: 0.0
    val liters = fuelLiters.toDoubleOrNull() ?: 1.0
    val price = pricePerLiter.toDoubleOrNull() ?: 0.0

    val mileageKmL = if (liters > 0) dist / liters else 0.0
    val totalCost = liters * price
    val costPerKm = if (dist > 0) totalCost / dist else 0.0

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp).verticalScroll(rememberScrollState())) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = distanceKm, onValueChange = { distanceKm = it }, label = { Text("Distance (km)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = fuelLiters, onValueChange = { fuelLiters = it }, label = { Text("Fuel (Liters)") }, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = pricePerLiter, onValueChange = { pricePerLiter = it }, label = { Text("Fuel Price / Liter (৳)") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Mileage & Economy", style = MaterialTheme.typography.labelLarge)
                Text("%.2f km / Liter".format(mileageKmL), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("Total Trip Cost: ৳ %.2f".format(totalCost), fontWeight = FontWeight.SemiBold)
                Text("Cost per Kilometer: ৳ %.2f / km".format(costPerKm), color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}
