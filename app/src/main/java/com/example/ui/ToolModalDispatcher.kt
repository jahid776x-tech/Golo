package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ToolItem
import com.example.ui.tools.*

@Composable
fun ToolModalDispatcher(tool: ToolItem, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tool.banglaName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Dispatch tool by ID
        when (tool.id) {
            // Category 1: Daily Utilities (1..11)
            1 -> SmartCalculatorTool()
            2 -> ScientificCalculatorTool()
            3 -> AgeCalculatorTool()
            4 -> WorldClockTool()
            5 -> QuickNotesTool()
            6 -> DigitalTasbihTool()
            7 -> StopwatchTool()
            8 -> TimerTool()
            9 -> PasswordGeneratorTool()
            10 -> RandomSpinWheelTool()
            11 -> CashDenominationTool()

            // Category 2: File Manager & Storage (12..19)
            12 -> DuplicateCleanerTool()
            13 -> JunkCacheCleanerTool()
            14 -> LargeFileFinderTool()
            15 -> StorageVisualizerTool()
            16 -> ApkExtractorTool()
            17 -> DocumentLockerTool()
            18 -> TxtCsvViewerTool()
            19 -> FileEncryptorTool()

            // Category 3: Media & Graphics (20..31)
            20 -> QrScannerTool()
            21 -> QrGeneratorTool()
            22 -> ImageCompressorTool()
            23 -> ImageCropRotateTool()
            24 -> ColorPickerTool()
            25 -> TextToPdfTool()
            26 -> ImageToPdfMakerTool()
            27 -> BackgroundRemoverTool()
            28 -> AudioRecorderTool()
            29 -> VideoToAudioTool()
            30 -> PdfMergerSplitterTool()
            31 -> DrawingBoardTool()

            // Category 4: Text & Content (32..39)
            32 -> WordCounterTool()
            33 -> CaseConverterTool()
            34 -> FancyTextTool()
            35 -> TextRepeaterTool()
            36 -> DuplicateLineRemoverTool()
            37 -> Base64MorseBinaryTool()
            38 -> UnicodeBijoyTool()
            39 -> OcrScannerTool()

            // Category 5: Measurement, Hardware & Sensors (40..53)
            40 -> FlashlightTool()
            41 -> CompassTool()
            42 -> SpiritLevelTool()
            43 -> SoundMeterTool()
            44 -> DeviceInfoTool()
            45 -> BatteryHealthTool()
            46 -> VibratometerTool()
            47 -> DeadPixelTesterTool()
            48 -> ScreenRulerTool()
            49 -> ProtractorTool()
            50 -> MetalDetectorTool()
            51 -> LightMeterTool()
            52 -> FrequencyGeneratorTool()
            53 -> MirrorTool()

            // Category 6: Unit Converters (54..61)
            54 -> LengthConverterTool()
            55 -> WeightConverterTool()
            56 -> TemperatureConverterTool()
            57 -> LandMeasurementTool()
            58 -> DataStorageConverterTool()
            59 -> SpeedConverterTool()
            60 -> VolumeConverterTool()
            61 -> FuelMileageTool()

            // Category 7: Finance, Health & Daily Tracking (62..70)
            62 -> CashbookTool()
            63 -> EmiCalculatorTool()
            64 -> VatCalculatorTool()
            65 -> DiscountCalculatorTool()
            66 -> CompoundInterestTool()
            67 -> BmiCalculatorTool()
            68 -> WaterTrackerTool()
            69 -> BreatheRelaxationTool()
            70 -> NetworkMonitorTool()

            else -> {
                Text("Tool #${tool.id} is ready for use.")
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
