package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.model.ToolCategory
import com.example.model.ToolItem

object ToolRegistry {
    val allTools: List<ToolItem> = listOf(
        // ==================== Category 1: Daily Utilities (11 tools) ====================
        ToolItem(
            id = 1,
            name = "Smart Calculator",
            banglaName = "স্মার্ট ক্যালকুলেটর (সাধারণ হিসাব)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.Calculate,
            description = "Standard everyday arithmetic calculator with expression preview and history.",
            isPinned = true,
            tags = listOf("math", "calc", "addition", "sum")
        ),
        ToolItem(
            id = 2,
            name = "Scientific Calculator",
            banglaName = "সায়েন্টিফিক ক্যালকুলেটর (ত্রিকোণমিতি)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.Functions,
            description = "Trigonometric, logarithmic, powers, square root, pi and exponential functions.",
            tags = listOf("sin", "cos", "tan", "log", "sqrt", "algebra")
        ),
        ToolItem(
            id = 3,
            name = "Age Calculator",
            banglaName = "বয়স ক্যালকুলেটর (বছর, মাস ও দিন)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.Cake,
            description = "Calculate exact age in years, months, days, hours, and next birthday countdown.",
            isPinned = true,
            tags = listOf("birthday", "dob", "years", "months", "days")
        ),
        ToolItem(
            id = 4,
            name = "World Clock",
            banglaName = "ওয়ার্ল্ড ক্লক (বিশ্বের বিভিন্ন শহরের সময়)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.Public,
            description = "Live world timezones: Dhaka, London, New York, Tokyo, Dubai, Sydney, Paris.",
            tags = listOf("time", "zone", "gmt", "utc", "clock")
        ),
        ToolItem(
            id = 5,
            name = "Quick Notes & To-Do",
            banglaName = "কুইক নোটস ও টু-ডু (অফলাইন নোটপ্যাড)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.Checklist,
            description = "Fast offline sticky notes, to-do checklist, and quick memo organizer.",
            isPinned = true,
            tags = listOf("task", "memo", "checklist", "notepad")
        ),
        ToolItem(
            id = 6,
            name = "Digital Tasbih",
            banglaName = "ডিজিটাল তসবিহ (ট্যালি কাউন্টার)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.TouchApp,
            description = "Islamic zikr counter with customizable target, lap reset, and tactile haptic vibration.",
            isPinned = true,
            tags = listOf("dhikr", "counter", "zikr", "tally")
        ),
        ToolItem(
            id = 7,
            name = "Stopwatch",
            banglaName = "স্টপওয়াচ (ল্যাপ টাইমিং সহ)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.Timer,
            description = "Precision millisecond stopwatch with split lap records and export.",
            tags = listOf("lap", "time", "racing", "speed")
        ),
        ToolItem(
            id = 8,
            name = "Timer",
            banglaName = "টাইমার (কাউন্টডাউন অ্যালার্ম)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.HourglassBottom,
            description = "Countdown interval timer with visual progress ring and completion alert.",
            tags = listOf("countdown", "alarm", "cooking", "focus")
        ),
        ToolItem(
            id = 9,
            name = "Password Generator & Vault",
            banglaName = "পাসওয়ার্ড জেনারেটর ও ভল্ট",
            category = ToolCategory.DAILY,
            icon = Icons.Default.Key,
            description = "Generate strong cryptographically random passwords with strength analysis.",
            tags = listOf("security", "passcode", "safety", "cipher")
        ),
        ToolItem(
            id = 10,
            name = "Random Number / Spin Wheel",
            banglaName = "র্যান্ডম নাম্বার ও স্পিন ডিসিশন",
            category = ToolCategory.DAILY,
            icon = Icons.Default.Casino,
            description = "Dice roll, custom min/max random number picker, and yes/no decision spinner.",
            tags = listOf("dice", "lottery", "spin", "decision", "picker")
        ),
        ToolItem(
            id = 11,
            name = "POS / Cash Denomination Counter",
            banglaName = "টাকার নোট গণনার হিসাব (Cash Counter)",
            category = ToolCategory.DAILY,
            icon = Icons.Default.PointOfSale,
            description = "Count 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1 cash notes with instant grand total.",
            isPinned = true,
            tags = listOf("money", "notes", "taka", "cash", "bank")
        ),

        // ==================== Category 2: File Manager & Cleaner (8 tools) ====================
        ToolItem(
            id = 12,
            name = "Duplicate File Cleaner",
            banglaName = "ডুপ্লিকেট ফাইল ক্লিনার (জোড়া ফাইল মোছা)",
            category = ToolCategory.STORAGE,
            icon = Icons.Default.ContentCopy,
            description = "Detect redundant duplicate files across cache and folders to reclaim storage.",
            isPinned = true,
            tags = listOf("duplicates", "cleanup", "storage", "reclaim")
        ),
        ToolItem(
            id = 13,
            name = "Junk & Cache Cleaner",
            banglaName = "জাঙ্ক ও ক্যাশে ক্লিনার (অপ্রয়োজনীয় ফাইল ক্লিন)",
            category = ToolCategory.STORAGE,
            icon = Icons.Default.CleaningServices,
            description = "Scan app cache, temporary logs, residual files, and clean storage safely.",
            isPinned = true,
            tags = listOf("junk", "cache", "temp", "speedup")
        ),
        ToolItem(
            id = 14,
            name = "Large File Finder",
            banglaName = "লার্জ ফাইল ফাইন্ডার (বড় ফাইল ফিল্টার)",
            category = ToolCategory.STORAGE,
            icon = Icons.Default.FolderZip,
            description = "Scan and filter files larger than 10MB, 50MB, or 100MB to free storage.",
            tags = listOf("heavy", "video", "zip", "space")
        ),
        ToolItem(
            id = 15,
            name = "Storage Visualizer / Analyzer",
            banglaName = "স্টোরেজ ভিজ্যুয়ালাইজার (পাই-চার্ট মেমোরি হিসাব)",
            category = ToolCategory.STORAGE,
            icon = Icons.Default.PieChart,
            description = "Graphical memory breakdown of internal storage, used, free, and file categories.",
            isPinned = true,
            tags = listOf("chart", "memory", "sdcard", "capacity")
        ),
        ToolItem(
            id = 16,
            name = "APK Extractor & Backup",
            banglaName = "APK এক্সট্র্যাক্টর ও ব্যাকআপ",
            category = ToolCategory.STORAGE,
            icon = Icons.Default.Android,
            description = "List installed applications, inspect package details, permissions, and create backups.",
            tags = listOf("package", "app", "installer", "backup")
        ),
        ToolItem(
            id = 17,
            name = "Document & File Locker",
            banglaName = "ডকুমেন্ট ও ফাইল লকার (পিন দিয়ে হাইড)",
            category = ToolCategory.STORAGE,
            icon = Icons.Default.Lock,
            description = "PIN-protected private secure vault for sensitive files, memos, and credentials.",
            tags = listOf("vault", "hide", "secret", "pin")
        ),
        ToolItem(
            id = 18,
            name = "TXT / CSV File Viewer",
            banglaName = "TXT ও CSV ফাইল ভিউয়ার (স্প্রেডশীট দেখা)",
            category = ToolCategory.STORAGE,
            icon = Icons.Default.Description,
            description = "Offline viewer for plain text and structured tabular CSV spreadsheet data.",
            tags = listOf("spreadsheet", "table", "excel", "reader")
        ),
        ToolItem(
            id = 19,
            name = "File Encryptor / Decryptor",
            banglaName = "ফাইল এনক্রিপ্টর ও ডিক্রিপ্টর (লক/আনলক)",
            category = ToolCategory.STORAGE,
            icon = Icons.Default.Security,
            description = "AES-256 standard password encryption and decryption for files and text.",
            tags = listOf("aes", "crypto", "cipher", "decrypt")
        ),

        // ==================== Category 3: Media & Graphics Utilities (12 tools) ====================
        ToolItem(
            id = 20,
            name = "QR & Barcode Scanner",
            banglaName = "QR ও বারকোড স্ক্যানার (অফলাইন স্ক্যান)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.QrCodeScanner,
            description = "Scan any QR code or retail barcode offline with instant decoded data actions.",
            isPinned = true,
            tags = listOf("scan", "code", "barcode", "upc")
        ),
        ToolItem(
            id = 21,
            name = "QR Code Generator",
            banglaName = "QR কোড জেনারেটর (কাস্টম কিউআর তৈরি)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.QrCode,
            description = "Generate high-resolution QR codes for websites, WiFi, contacts, and text.",
            tags = listOf("create", "matrix", "wifi", "link")
        ),
        ToolItem(
            id = 22,
            name = "Image Size Reducer / Compressor",
            banglaName = "ইমেজ সাইজ রিডিউসার (ছবি সাইজ ছোট করা)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.Compress,
            description = "Compress images down to targeted KB size while preserving visual clarity.",
            tags = listOf("photo", "shrink", "kb", "resize")
        ),
        ToolItem(
            id = 23,
            name = "Image Crop & Rotate",
            banglaName = "ইমেজ ক্রপ ও রোটেট (ছবি এডিটর)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.Crop,
            description = "Crop to 1:1, 4:3, 16:9 aspect ratios, flip horizontal/vertical, and rotate.",
            tags = listOf("edit", "ratio", "angle", "transform")
        ),
        ToolItem(
            id = 24,
            name = "Color Picker & Blender",
            banglaName = "কালার পিকার ও ব্লেন্ডার (HEX কোড)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.Palette,
            description = "Interactive color wheel, HEX, RGB, HSV converter, and palette harmony mixer.",
            tags = listOf("rgb", "hex", "hsv", "design", "palette")
        ),
        ToolItem(
            id = 25,
            name = "Text to PDF / Image",
            banglaName = "টেক্সট টু PDF / ইমেজ কনভার্টার",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.PictureAsPdf,
            description = "Convert written text, certificates, or documents into styled image and PDF layout.",
            tags = listOf("document", "convert", "export", "doc")
        ),
        ToolItem(
            id = 26,
            name = "Image to PDF Maker",
            banglaName = "ইমেজ টু PDF মেকার (ছবি জুড়ে PDF)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.Collections,
            description = "Combine multiple gallery photos or receipts into a single clean PDF document.",
            tags = listOf("merge", "album", "pages", "scan")
        ),
        ToolItem(
            id = 27,
            name = "Background Remover",
            banglaName = "ব্যাকগ্রাউন্ড রিমুভার (ক্যানভাসভিত্তিক ইরেজার)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.AutoFixHigh,
            description = "Canvas-based touch eraser brush and transparent alpha background cutout.",
            tags = listOf("cutout", "transparent", "png", "eraser")
        ),
        ToolItem(
            id = 28,
            name = "Audio Recorder & Trimmer",
            banglaName = "অডিও রেকর্ডার ও ট্রিমার (ভয়েস রেকর্ডার)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.Mic,
            description = "High-fidelity offline voice memo recorder with live audio wave and duration monitor.",
            tags = listOf("voice", "sound", "wav", "playback")
        ),
        ToolItem(
            id = 29,
            name = "Video to Audio Converter",
            banglaName = "ভিডিও টু অডিও কনভার্টার (MP3 এক্সট্র্যাক্ট)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.Audiotrack,
            description = "Extract high quality audio tracks (MP3, AAC, WAV) from video clips.",
            tags = listOf("mp3", "soundtrack", "extract", "music")
        ),
        ToolItem(
            id = 30,
            name = "PDF Merger & Splitter",
            banglaName = "PDF মার্জার ও স্প্লিটার (জোড়া দেওয়া ও ভাঙা)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.MergeType,
            description = "Combine multiple PDF files into one or split large documents into individual pages.",
            tags = listOf("combine", "separate", "pages", "binder")
        ),
        ToolItem(
            id = 31,
            name = "Drawing Board & Digital Signature",
            banglaName = "ড্রয়িং বোর্ড ও ডিজিটাল স্বাক্ষর (স্কেচপ্যাড)",
            category = ToolCategory.MEDIA,
            icon = Icons.Default.Draw,
            description = "Interactive touch sketchpad for quick handwriting, sketching, and exportable digital e-signatures.",
            isPinned = true,
            tags = listOf("signature", "paint", "sketch", "canvas")
        ),

        // ==================== Category 4: Text & Content Utilities (8 tools) ====================
        ToolItem(
            id = 32,
            name = "Word & Character Counter",
            banglaName = "শব্দ ও অক্ষর গণনা (Word & Char Counter)",
            category = ToolCategory.TEXT,
            icon = Icons.Default.Numbers,
            description = "Real-time count of words, characters, spaces, sentences, paragraphs, and reading time.",
            isPinned = true,
            tags = listOf("words", "chars", "stats", "reading")
        ),
        ToolItem(
            id = 33,
            name = "Case Converter",
            banglaName = "কেস কনভার্টার (Upper, Lower, Title Case)",
            category = ToolCategory.TEXT,
            icon = Icons.Default.FormatSize,
            description = "Transform text into UPPERCASE, lowercase, Title Case, camelCase, snake_case, etc.",
            tags = listOf("caps", "lower", "title", "syntax")
        ),
        ToolItem(
            id = 34,
            name = "Fancy Text Stylist",
            banglaName = "ফ্যান্সি টেক্সট স্টাইলিস্ট (স্টাইলিশ ফন্ট মেকার)",
            category = ToolCategory.TEXT,
            icon = Icons.Default.AutoAwesome,
            description = "Convert normal text into 15+ aesthetic Unicode font styles for social bios and chats.",
            tags = listOf("fonts", "aesthetic", "gothic", "bubble", "symbols")
        ),
        ToolItem(
            id = 35,
            name = "Text Repeater",
            banglaName = "টেক্সট রিপিটার (বারবার কপি করা)",
            category = ToolCategory.TEXT,
            icon = Icons.Default.Repeat,
            description = "Repeat any text message up to 10,000 times with custom delimiters in one tap.",
            tags = listOf("spam", "multiply", "duplicate", "batch")
        ),
        ToolItem(
            id = 36,
            name = "Duplicate Line Remover",
            banglaName = "ডুপ্লিকেট লাইন রিমুভার (তালিকা পরিষ্কার)",
            category = ToolCategory.TEXT,
            icon = Icons.Default.PlaylistRemove,
            description = "Deduplicate lists, sort alphabetically, remove empty lines, and trim whitespace.",
            tags = listOf("clean", "list", "filter", "unique")
        ),
        ToolItem(
            id = 37,
            name = "Base64 / Morse / Binary",
            banglaName = "Base64 / Morse কোড / Binary কনভার্টার",
            category = ToolCategory.TEXT,
            icon = Icons.Default.Code,
            description = "Encode and decode Base64, ASCII Binary (01001...), and audio Morse code sequences.",
            tags = listOf("binary", "morse", "base64", "encode", "decode")
        ),
        ToolItem(
            id = 38,
            name = "Unicode to Bijoy Converter",
            banglaName = "ইউনিকোড টু বিজয় কনভার্টার (Bangla Typist)",
            category = ToolCategory.TEXT,
            icon = Icons.Default.Translate,
            description = "Convert Bangla Unicode text to SutonnyMJ / Bijoy layout and vice versa.",
            tags = listOf("bangla", "bijoy", "unicode", "sutonnymj", "avro")
        ),
        ToolItem(
            id = 39,
            name = "Image to Text OCR Scanner",
            banglaName = "ইমেজ টু টেক্সট OCR স্ক্যানার",
            category = ToolCategory.TEXT,
            icon = Icons.Default.DocumentScanner,
            description = "Extract and copy editable text from photos, documents, and paper receipts.",
            tags = listOf("ocr", "extract", "scan", "copy")
        ),

        // ==================== Category 5: Measurement, Hardware & Sensors (14 tools) ====================
        ToolItem(
            id = 40,
            name = "Flashlight Control",
            banglaName = "ফ্ল্যাশলাইট কন্ট্রোল (টর্চ ও স্ট্রোব ব্লিঙ্কার)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.FlashlightOn,
            description = "High-powered camera torchlight with SOS emergency strobe and blinking frequencies.",
            isPinned = true,
            tags = listOf("torch", "sos", "light", "strobe")
        ),
        ToolItem(
            id = 41,
            name = "Compass",
            banglaName = "কম্পাস (অফলাইন দিক নির্ণয়)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.Explore,
            description = "Precision 360-degree magnetic compass with cardinal bearings and azimuth degrees.",
            isPinned = true,
            tags = listOf("direction", "north", "qibla", "navigation")
        ),
        ToolItem(
            id = 42,
            name = "Spirit Level",
            banglaName = "স্পিরিট লেভেল (সারফেস সমতল লেভেলার)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.AlignHorizontalCenter,
            description = "2D bubble spirit leveler using device accelerometer for surface balance and tilt angles.",
            tags = listOf("level", "bubble", "tilt", "surface")
        ),
        ToolItem(
            id = 43,
            name = "Sound / Decibel Meter",
            banglaName = "সাউন্ড / ডেসিবেল মিটার (শব্দের তীব্রতা)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.VolumeUp,
            description = "Real-time acoustic sound meter displaying min, max, avg noise levels in decibels (dB).",
            tags = listOf("decibel", "db", "noise", "mic")
        ),
        ToolItem(
            id = 44,
            name = "Device Info",
            banglaName = "ডিভাইস ইনফো (র্যাম, প্রসেসর ও ওএস)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.PermDeviceInformation,
            description = "Complete system telemetry: hardware, manufacturer, CPU cores, RAM, screen DPI, OS version.",
            tags = listOf("specs", "ram", "cpu", "system", "hardware")
        ),
        ToolItem(
            id = 45,
            name = "Battery Health Monitor",
            banglaName = "ব্যাটারি হেলথ মনিটর (ভোল্টেজ ও তাপমাত্রা)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.BatteryChargingFull,
            description = "Real-time battery percentage, charging state, temperature in °C, voltage, and health status.",
            tags = listOf("power", "charge", "voltage", "temperature")
        ),
        ToolItem(
            id = 46,
            name = "Vibratometer / Tester",
            banglaName = "ভাইব্রেটোমিটার ও মোটর টেস্টার",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.Vibration,
            description = "Test device haptic actuators with heartbeat, click, double pulse, and SOS vibration waveforms.",
            tags = listOf("haptic", "motor", "buzz", "vibe")
        ),
        ToolItem(
            id = 47,
            name = "Screen Dead-Pixel Tester",
            banglaName = "স্ক্রিন ডেড-পিক্সেল টেস্টার (ডিসপ্লে টেস্ট)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.Tv,
            description = "Full-screen RGB, pure black, white, and yellow colors to inspect dead pixels and backlight bleed.",
            tags = listOf("display", "pixel", "burnin", "lcd", "oled")
        ),
        ToolItem(
            id = 48,
            name = "Screen Ruler",
            banglaName = "স্ক্রিন রুলার (স্কেল ও মিলিমিটার মাপা)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.LinearScale,
            description = "Calibrated on-screen ruler measuring accurately in centimeters (cm), millimeters (mm), and inches (in).",
            tags = listOf("scale", "inches", "cm", "measure")
        ),
        ToolItem(
            id = 49,
            name = "Protractor",
            banglaName = "প্রোট্র্যাক্টর (কোণ ও ডিগ্রী মাপার স্কেল)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.ChangeHistory,
            description = "Interactive 180° geometric protractor with touch-draggable ray for measuring slope and angles.",
            tags = listOf("angle", "degree", "geometry", "slope")
        ),
        ToolItem(
            id = 50,
            name = "Metal Detector",
            banglaName = "মেটাল ডিটেক্টর (ম্যাগনেটোমিটার দিয়ে ধাতু খোঁজা)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.Sensors,
            description = "Detect electromagnetic and ferromagnetic metal fields via internal magnetometer (µT).",
            tags = listOf("magnetic", "ferrous", "gold", "sensor")
        ),
        ToolItem(
            id = 51,
            name = "Light Meter",
            banglaName = "লাইট মিটার (আলোর তীব্রতা / LUX মাপা)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.LightMode,
            description = "Measures environmental illuminance in Lux (lx) with contextual scene indicators.",
            tags = listOf("lux", "brightness", "lighting", "photography")
        ),
        ToolItem(
            id = 52,
            name = "Frequency Sound Generator",
            banglaName = "ফ্রিকোয়েন্সি সাউন্ড জেনারেটর (Hz টোন)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.GraphicEq,
            description = "Generate pure sine audio frequencies from 20 Hz up to 20,000 Hz with fine pitch slider.",
            tags = listOf("tone", "hz", "audio", "pitch", "speaker")
        ),
        ToolItem(
            id = 53,
            name = "Mirror",
            banglaName = "মিরর (ফ্রন্ট ক্যামেরা দিয়ে অফলাইন আয়না)",
            category = ToolCategory.SENSORS,
            icon = Icons.Default.Face,
            description = "Use front camera as a personal pocket mirror with digital zoom, brightness boost, and freeze frame.",
            tags = listOf("frontcam", "reflection", "zoom", "freeze")
        ),

        // ==================== Category 6: Unit Converters (8 tools) ====================
        ToolItem(
            id = 54,
            name = "Length & Distance Converter",
            banglaName = "দৈর্ঘ্য ও দূরত্ব রূপান্তর (Length Converter)",
            category = ToolCategory.UNITS,
            icon = Icons.Default.Straighten,
            description = "Convert meters, kilometers, centimeters, feet, inches, miles, yards, and nautical miles.",
            isPinned = true,
            tags = listOf("meter", "km", "mile", "foot", "inch")
        ),
        ToolItem(
            id = 55,
            name = "Weight & Mass Converter",
            banglaName = "ওজন ও ভর রূপান্তর (Weight & Mass)",
            category = ToolCategory.UNITS,
            icon = Icons.Default.Scale,
            description = "Convert kilograms, grams, milligrams, pounds (lbs), ounces (oz), tons, and stones.",
            tags = listOf("kg", "gram", "lbs", "pound", "ounce")
        ),
        ToolItem(
            id = 56,
            name = "Temperature Converter",
            banglaName = "তাপমাত্রা রূপান্তর (°C, °F, Kelvin)",
            category = ToolCategory.UNITS,
            icon = Icons.Default.Thermostat,
            description = "Instant conversion between Celsius (°C), Fahrenheit (°F), Kelvin (K), and Rankine.",
            tags = listOf("celsius", "fahrenheit", "kelvin", "heat")
        ),
        ToolItem(
            id = 57,
            name = "Land / Area Measurement Calc",
            banglaName = "জমি মাপার হিসাব (কাঠা, বিঘা, শতক, একর)",
            category = ToolCategory.UNITS,
            icon = Icons.Default.SquareFoot,
            description = "Traditional and modern land calculations: কাঠা (Katha), বিঘা (Bigha), শতক (Decimal), একর (Acre), Sq Ft.",
            isPinned = true,
            tags = listOf("katha", "bigha", "shatak", "decimal", "acre", "land")
        ),
        ToolItem(
            id = 58,
            name = "Data Storage Converter",
            banglaName = "ডাটা স্টোরেজ কনভার্টার (MB, GB, KB, TB)",
            category = ToolCategory.UNITS,
            icon = Icons.Default.Storage,
            description = "Convert digital storage units: Bits, Bytes, KB, MB, GB, TB, and Petabytes.",
            tags = listOf("byte", "megabyte", "gigabyte", "terabyte", "bits")
        ),
        ToolItem(
            id = 59,
            name = "Speed Converter",
            banglaName = "গতির একক রূপান্তর (Speed Converter)",
            category = ToolCategory.UNITS,
            icon = Icons.Default.Speed,
            description = "Convert speed units: km/h, m/s, mph, knots, and feet per second.",
            tags = listOf("velocity", "mph", "kmh", "knots")
        ),
        ToolItem(
            id = 60,
            name = "Volume & Capacity Converter",
            banglaName = "আয়তন ও ক্যাপাসিটি কনভার্টার (Liter, Gallon)",
            category = ToolCategory.UNITS,
            icon = Icons.Default.WaterDrop,
            description = "Convert liters, milliliters, US gallons, quarts, pints, cups, and cubic meters.",
            tags = listOf("liter", "gallon", "ml", "liquid")
        ),
        ToolItem(
            id = 61,
            name = "Fuel & Mileage Calculator",
            banglaName = "মাইলেজ ও ফুয়েল হিসাব (Mileage & Fuel)",
            category = ToolCategory.UNITS,
            icon = Icons.Default.LocalGasStation,
            description = "Calculate fuel economy (km/L, L/100km, MPG), total journey cost, and fuel required.",
            tags = listOf("petrol", "diesel", "mileage", "trip", "cost")
        ),

        // ==================== Category 7: Finance, Health & Daily Tracking (9 tools) ====================
        ToolItem(
            id = 62,
            name = "POS / Simple Cashbook",
            banglaName = "দৈনিক আয়-ব্যয়ের হিসাব (Simple Cashbook)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.AccountBalanceWallet,
            description = "Offline cash in / cash out ledger with running balance, categories, and date stamps.",
            isPinned = true,
            tags = listOf("income", "expense", "ledger", "tally", "wallet")
        ),
        ToolItem(
            id = 63,
            name = "EMI Loan Calculator",
            banglaName = "ইএমআই লোন ক্যালকুলেটর (লোনের কিস্তি)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.CreditCard,
            description = "Calculate monthly loan EMI installments, total payable interest, and overall loan cost.",
            isPinned = true,
            tags = listOf("loan", "emi", "bank", "interest", "mortgage")
        ),
        ToolItem(
            id = 64,
            name = "GST / VAT Calculator",
            banglaName = "জিএসটি ও ভ্যাট ক্যালকুলেটর (ট্যাক্স হিসাব)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.Receipt,
            description = "Calculate inclusive and exclusive VAT/GST with customizable tax percentage rates.",
            tags = listOf("tax", "vat", "gst", "invoice")
        ),
        ToolItem(
            id = 65,
            name = "Discount & Percentage Calc",
            banglaName = "ছাড় ও শতকরার হিসাব (Discount Calculator)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.Percent,
            description = "Find discount savings, final sale price, percentage increase, and markup values.",
            tags = listOf("sale", "discount", "offer", "savings")
        ),
        ToolItem(
            id = 66,
            name = "Compound Interest Calc",
            banglaName = "চক্রবৃদ্ধি সুদের হিসাব (Compound Interest)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.TrendingUp,
            description = "Compute compounded interest returns over time with annual, monthly, or quarterly compounding.",
            tags = listOf("investment", "savings", "interest", "compound")
        ),
        ToolItem(
            id = 67,
            name = "BMI Calculator",
            banglaName = "বিএমআই ক্যালকুলেটর (ওজন ও উচ্চতার অনুপাত)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.FitnessCenter,
            description = "Calculate Body Mass Index (BMI) with healthy weight target guidance and status category.",
            isPinned = true,
            tags = listOf("health", "weight", "height", "diet", "fitness")
        ),
        ToolItem(
            id = 68,
            name = "Water Intake Tracker",
            banglaName = "ওয়াটার ট্র্যাকার (দৈনিক পানি পানের হিসেব)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.LocalDrink,
            description = "Track daily hydration target (e.g. 2500 ml) with animated visual water cup progress.",
            isPinned = true,
            tags = listOf("hydration", "drink", "water", "health")
        ),
        ToolItem(
            id = 69,
            name = "Breathe & Relaxation Control",
            banglaName = "শ্বাস-প্রশ্বাসের ব্যায়াম (Breathe Relaxation)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.Spa,
            description = "Guided 4-7-8 and Box breathing mindfulness exercises with soothing pulsing visual rhythm.",
            tags = listOf("meditation", "calm", "relax", "pranayama", "stress")
        ),
        ToolItem(
            id = 70,
            name = "Internet Speed & Data Monitor",
            banglaName = "ইন্টারনেট স্পিড ও ডেটা ট্র্যাকার (Network Monitor)",
            category = ToolCategory.FINANCE,
            icon = Icons.Default.NetworkCheck,
            description = "Monitor live offline network connectivity state, WiFi signal strength, IP address, and ping latency.",
            tags = listOf("wifi", "speed", "latency", "ping", "data")
        )
    )

    fun searchTools(query: String, selectedCategory: ToolCategory? = null): List<ToolItem> {
        val trimmed = query.trim().lowercase()
        return allTools.filter { tool ->
            val matchesCategory = selectedCategory == null || tool.category == selectedCategory
            val matchesQuery = trimmed.isEmpty() ||
                tool.name.lowercase().contains(trimmed) ||
                tool.banglaName.lowercase().contains(trimmed) ||
                tool.description.lowercase().contains(trimmed) ||
                tool.tags.any { it.contains(trimmed) }
            matchesCategory && matchesQuery
        }
    }
}
