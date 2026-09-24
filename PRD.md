# Product Requirements Document (PRD)
## Project Name: Mega Utility (মেগা ইউটিলিটি - ৭০টি অফলাইন স্মার্ট টুলস)
**Document Version:** 1.0.0  
**Status:** Approved / Active Implementation  
**Product Category:** Utility, Productivity & System Tools  
**Platforms:** Android (Native Jetpack Compose) & Progressive Web / HTML5 Offline  
**Language Support:** Bengali (বাংলা) & English (Primary Bilingual UX)  
**Last Updated:** September 2026  

---

## 1. Executive Summary & Vision

### 1.1 Problem Statement
Modern smartphone users currently face acute application fragmentation and privacy degradation:
- **App Bloat & Storage Exhaustion:** Users install 15–30 separate single-purpose apps (calculators, compass, unit converters, QR scanners, voice recorders, note-pads, flashlights) which consume gigabytes of storage and constant background RAM.
- **Intrusive Advertisements & Privacy Risks:** Free utility apps on app stores are typically laden with full-screen interstitial ads, mandatory internet tracking, and intrusive permissions harvesting personal data.
- **Connectivity Fragility:** Many utility apps fail or degrade when offline, creating critical failures during field work, travel, or power outages.
- **Localization Gap:** Traditional global utility applications do not support regional requirements in developing markets (e.g., Bangladesh traditional land measurement units like *শতক*, *কাঠা*, *বিঘা*, cash denomination currency counting in *টাকা*, or Bengali font transliteration).

### 1.2 Product Vision
**Mega Utility** is an ultra-lightweight, 100% offline-first, privacy-respecting "Swiss Army Knife" utility suite offering **70 indispensable tools** across 7 primary categories within a single unified application. Built on Google Material Design 3, it offers zero advertisements, instant sub-second tool discovery, haptic feedback, and local-only data persistence.

### 1.3 Key Value Propositions
1. **All-in-One Consolidation:** 70 standalone tools replacing dozens of fragmented apps.
2. **100% Offline by Design:** Zero external server dependencies, zero tracking, zero mandatory cloud sign-ins.
3. **Instant 1-Click Discovery:** Unified live search (English, Bengali, and tags) and customizable pinned/favorites shelf.
4. **Localized Precision:** Native support for regional finance (Bangladeshi cash denomination, EMI, VAT) and land measurement (*শতক*, *কাঠা*, *বিঘা*, *একর*).
5. **Modern M3 Aesthetics:** Dynamic color theming, smooth bottom-sheet modals, accessible 48dp touch targets, and high-contrast dark/light modes.

---

## 2. Target Audience & User Personas

| Persona | Profile & Demographics | Primary Use Cases | Key Pain Points Solved |
| :--- | :--- | :--- | :--- |
| **P1: Everyday Consumer / Student** | Ages 16–35, high mobile usage, variable internet access. | Quick calculations, age finder, QR codes, word counters, stopwatch, digital tasbih, fancy text. | Intrusive ads, app clutter, slow device performance. |
| **P2: Small Business Owner / Trader** | Retailers, grocery shop owners, freelancers in Bangladesh. | Cash denomination counter, daily cashbook (POS), VAT/tax calculator, discount finder, EMI estimator. | Need for fast, reliable cash counting and receipt math without expensive POS software. |
| **P3: Field Engineer & Land Surveyor** | Real-estate brokers, surveyors, civil technicians, DIYers. | Land area calculator (*শতক*, *কাঠা*, *বিঘা*), spirit level, digital compass, protractor, screen ruler, lux light meter. | Carrying physical measuring gear or downloading unreliable separate tools. |
| **P4: Privacy-Conscious Tech Enthusiast** | Developers, security-minded individuals. | Strong password generator, document vault, file encryptor, Base64/Binary encoder, device specs monitor. | Avoiding third-party cloud data harvesting for sensitive calculations. |

---

## 3. Product Architecture & Technical Stack

### 3.1 Technology Matrix
- **Android Native Platform:**
  - **Language:** Kotlin 2.x
  - **UI Framework:** Jetpack Compose (Material 3 Declarative UI)
  - **Architecture:** Clean MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF)
  - **Hardware & Sensors:** Android Hardware Sensor API (Accelerometer, Magnetic Field, Light, Microphone, Camera Flashlight, Vibrator)
  - **State & Storage:** Android SharedPreferences & Local Keystore for pinned states and cached preferences
  - **Build Tool:** Gradle Kotlin DSL with strict compilation and Robolectric unit tests
- **Standalone Web Platform (Single-File Suite):**
  - **Language:** Pure Vanilla HTML5, CSS3, ES6+ JavaScript (`index.html`)
  - **Persistence:** Browser `localStorage`
  - **Portability:** Zero dependencies, runs directly in any browser offline

### 3.2 System Architecture Diagram
```
┌─────────────────────────────────────────────────────────────────┐
│                    User Interface Layer                         │
│  [Universal Search Bar]  [Category Chips]  [Pinned Favorites]   │
│  [2-Column Grid Layout]  [Bottom Navigation Bar (3 Tabs)]       │
└────────────────────────────────┬────────────────────────────────┘
                                 │ Tap Tool Card
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                  Tool Modal Dispatcher (M3)                     │
│               Slides up via ModalBottomSheet                    │
└───────┬────────────┬───────────┬───────────┬────────────┬───────┘
        │            │           │           │            │
        ▼            ▼           ▼           ▼            ▼
  ┌──────────┐ ┌──────────┐┌──────────┐┌──────────┐ ┌───────────┐
  │ Category │ │ Category ││ Category ││ Category │ │ Category  │
  │ 1: Daily │ │ 2: Storage│ 3: Media ││ 4: Text  │ │ 5: Sensors│ ...
  └──────────┘ └──────────┘└──────────┘└──────────┘ └───────────┘
        │            │           │           │            │
┌───────┴────────────┴───────────┴───────────┴────────────┴───────┐
│                    Core Engine & Platform APIs                  │
│ [Preferences/Cache] [Math Parser] [Hardware Sensor Event Loop]  │
│ [Bangla Locale Engines] [Audio/Vibration HAL] [File Sandboxing] │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4. Complete Taxonomy: The 70 Tools Master Catalog

The application organizes all 70 utilities into 7 distinct thematic categories:

### Category 1: Daily Utilities & Everyday Life (১১টি)
1. **Smart Calculator:** Standard arithmetic, instant parentheses calculation, real-time expression preview.
2. **Scientific Calculator:** Trigonometry ($\sin, \cos, \tan$), logarithmic functions, square root, powers ($x^y$), constants ($\pi, e$).
3. **Age Calculator:** Precise breakdown of current age in years, months, weeks, days, and countdown to next birthday.
4. **World Clock:** Live multi-timezone clocks (Dhaka, London, New York, Tokyo, Dubai, Sydney).
5. **Quick Notes & To-Do:** Lightweight offline scratchpad and task list with completion toggles.
6. **Digital Tasbih:** Digital tally counter with custom target increments, reset, and haptic feedback.
7. **Stopwatch:** Millisecond accuracy stopwatch with lap timing recorder.
8. **Countdown Timer:** Configurable minutes/seconds countdown with visual circular progress.
9. **Password Generator & Vault:** Cryptographically random passwords with customizable length, symbols, digits, and strength meter.
10. **Random Spin Wheel / Decider:** Custom random item decision maker for quick selections or lotteries.
11. **POS / Cash Denomination Counter:** Dedicated banknote multiplier for currency notes (1000, 500, 200, 100, 50, 20, 10, 5, 2, 1) with live grand total in ৳ (Taka).

### Category 2: File Manager & Storage Cleaner (৮টি)
12. **Duplicate File Cleaner:** Scans and pinpoints identical files by size/hash to free storage.
13. **Junk & Cache Cleaner:** Identifies temporary cache directories and stale logs.
14. **Large File Finder:** Filters files over 50MB/100MB/500MB to reclaim disk space.
15. **Storage Visualizer / Analyzer:** Categorized breakdown of internal storage usage (Media, Documents, Apps, System).
16. **APK Extractor & Backup:** Generates clean backup APKs of installed user applications.
17. **Document & File Locker:** Local PIN-protected private vault for documents and images.
18. **TXT / CSV File Viewer:** Offline plain text and structured CSV table viewer.
19. **File Encryptor / Decryptor:** Password-based file encryption/decryption for confidential data.

### Category 3: Media & Graphics Utilities (১২টি)
20. **QR & Barcode Scanner:** Real-time offline camera scanning for QR codes and product barcodes.
21. **QR Code Generator:** Custom QR creation for URLs, text, contacts, and Wi-Fi networks.
22. **Image Size Reducer / Compressor:** Compresses image file size to targeted kilobyte (KB) thresholds.
23. **Image Crop & Rotate:** Aspect ratio cropping (1:1, 4:3, 16:9) and 90° rotation.
24. **Color Picker & Blender:** Eyedropper and palette extractor with HEX, RGB, and HSL output.
25. **Text to PDF / Image:** Direct conversion of styled text notes into downloadable PDF documents.
26. **Image to PDF Maker:** Multi-image compiler into single compact multi-page PDF files.
27. **Background Remover:** Canvas-based contrast and boundary background removal.
28. **Audio Recorder & Trimmer:** High-fidelity offline voice recording with waveform visualization.
29. **Video to Audio Converter:** Audio track extractor from offline video clips.
30. **PDF Merge & Split Tool:** Combines multiple PDFs or extracts individual pages.
31. **Drawing & Signature Pad:** Smooth touch canvas for hand signatures and vector sketches with PNG export.

### Category 4: Text & Content Utilities (৮টি)
32. **Word & Character Counter:** Real-time counter for characters, words, sentences, paragraphs, and reading time.
33. **Text Case Converter:** UPPERCASE, lowercase, Title Case, camelCase, snake_case, and Sentence case.
34. **Fancy Stylized Text:** Generates aesthetic unicode stylized typography for social bios and headers.
35. **Text Repeater:** Repeats any string or emoji up to 5,000 times with optional newline/spacing delimiters.
36. **Duplicate Line Remover:** Cleans multi-line lists by removing duplicate entries and sorting alphabetically.
37. **Base64, Morse & Binary Encoder:** Two-way encoder/decoder for Base64, Binary, Hex, and Morse code.
38. **Unicode to Bijoy Converter:** Transliteration and encoding switcher between Unicode and traditional Bijoy Bangla.
39. **OCR Text Extractor:** Offline optical character recognition extracting text from camera photos.

### Category 5: Measurement, Hardware & Sensors (১৪টি)
40. **Flashlight & SOS Strobe:** High-intensity LED torch control with adjustable frequency strobe flasher.
41. **Digital Compass:** 360° magnetometer compass with true north heading and azimuth readout.
42. **Spirit Bubble Level:** Dual-axis inclinometer showing level balance for carpentry and installation.
43. **Sound & Decibel Meter:** Real-time microphone audio amplitude measuring ambient noise level in dB.
44. **Device Info & Specs:** Full telemetry display (CPU cores, RAM usage, display DPI, OS build, thermal status).
45. **Battery Health Monitor:** Real-time battery charge level, voltage, temperature (°C), and charging technology.
46. **Vibratometer:** Calibrated vibration and seismic shake detector with Richter-equivalent graph.
47. **Dead Pixel Screen Tester:** Fullscreen pure color cycling (Red, Green, Blue, White, Black) to detect stuck pixels.
48. **Screen Ruler / Scale:** Calibrated on-screen ruler in both centimeters (cm) and inches (in).
49. **180° Protractor:** On-screen angle measuring tool for geometric angles.
50. **Metal & EMF Detector:** Hardware magnetometer detector sensing electromagnetic flux in microteslas ($\mu T$).
51. **Ambient Light Meter:** Real-time ambient light sensor measuring illumination in Lux.
52. **Audio Frequency Generator:** Sine wave frequency generator outputting tones from 20 Hz to 20,000 Hz.
53. **Pocket Mirror:** Front camera viewfinder with high digital zoom and exposure freeze.

### Category 6: Unit & Conversion Calculators (৮টি)
54. **Length & Distance Converter:** Meters, kilometers, centimeters, millimeters, feet, inches, yards, miles, nautical miles.
55. **Weight & Mass Converter:** Kilograms, grams, milligrams, pounds (lbs), ounces (oz), metric tons.
56. **Temperature Converter:** Celsius (°C), Fahrenheit (°F), Kelvin (K) with interactive gauge.
57. **Land / Area Measurement (Bangladeshi Traditional & Global):**
    - $1\text{ Shatak (শতক / ডেসিমেল)} = 435.6\text{ sq ft}$
    - $1\text{ Katha (কাঠা)} = 720\text{ sq ft} = 1.65\text{ Shatak}$
    - $1\text{ Bigha (বিঘা)} = 20\text{ Katha} = 14,400\text{ sq ft} = 33.06\text{ Shatak}$
    - $1\text{ Acre (একর)} = 100\text{ Shatak} = 3.025\text{ Bigha} = 43,560\text{ sq ft}$
    - Square feet and square meters.
58. **Data Storage Converter:** Bytes, KB, MB, GB, TB, PB (Binary 1024 base).
59. **Speed Converter:** km/h, m/s, mph, knots, ft/s.
60. **Volume & Capacity Converter:** Liters, milliliters, US gallons, UK gallons, cups, cubic meters.
61. **Fuel & Mileage Calculator:** Distance (km), fuel consumed (L), price per liter $\rightarrow$ mileage (km/L), trip cost (৳), and cost per kilometer.

### Category 7: Finance, Health & Daily Tracking (৯টি)
62. **Daily POS & Cashbook:** Fast cash-in / cash-out ledger with running balance, timestamps, and note entries.
63. **EMI Loan Calculator:** Principal loan amount, interest rate (% p.a.), loan tenure (months/years) $\rightarrow$ monthly EMI, total interest, and total payable amount.
64. **GST / VAT Calculator:** Net/gross amount, custom VAT rate (5%, 7.5%, 10%, 15%), inclusive or exclusive toggle with live tax breakdown.
65. **Discount & Savings Calculator:** Original price, discount %, savings amount, final discounted price.
66. **Compound Interest Calculator:** Principal, annual rate %, period in years, monthly/quarterly/annual compounding $\rightarrow$ maturity balance and accrued interest.
67. **BMI Health Calculator:** Weight (kg) and height (cm/ft) $\rightarrow$ Body Mass Index value and health category (Underweight, Normal, Overweight, Obese).
68. **Daily Water Intake Tracker:** Configurable daily target (e.g., 2500 ml), quick intake loggers (+250 ml glass, +500 ml bottle), and animated hydration progress bar.
69. **4-7-8 Breathe Relaxation:** Guided 4s Inhale, 7s Hold, 8s Exhale meditation breathing guide with animated pulsing circle.
70. **Network & Speed Monitor:** Network diagnostic measuring Wi-Fi/Cellular connectivity status, link bandwidth, and local gateway latency.

---

## 5. UI/UX Design System Specifications

### 5.1 Material Design 3 (M3) Theming Tokens
- **Design Philosophy:** Clean, spacious, functional, thumb-friendly.
- **Card Styling:** $16\text{dp}$ corner radius, subtle borders ($1\text{dp}$ outline variant), category-tinted circular icon badges ($42\text{dp}\times42\text{dp}$).
- **Touch Target Integrity:** All interactive elements, buttons, and chips strictly maintain $\ge 48\text{dp}\times48\text{dp}$ interactive hit areas.
- **Color Palettes by Category:**
  - *Daily Utilities:* Royal Blue (`#2563EB`)
  - *File & Storage:* Deep Orange (`#EA580C`)
  - *Media & Graphics:* Vibrant Purple (`#9333EA`)
  - *Text & Content:* Deep Teal (`#0D9488`)
  - *Sensors & Hardware:* Sky Cyan (`#0284C7`)
  - *Unit Converters:* Indigo (`#4F46E5`)
  - *Finance & Health:* Emerald Green (`#059669`)

### 5.2 Navigation & Modal Architecture
1. **Persistent Top Bar & Sticky Search:** Search bar remains visible or readily accessible at the top with a clear button (`✕`) for immediate query reset.
2. **Category Carousel:** Horizontal chip bar allowing single-tap filtering with dynamic badge counts.
3. **2-Column Responsive Grid:** Adaptive tool grid supporting compact phones, foldables, and tablets (`Modifier.widthIn(max = 600.dp)` on tablets).
4. **Interactive Bottom Sheet Runner:** Tapping any tool opens a `ModalBottomSheet` displaying the tool's interactive UI, avoiding jarring full-screen navigation transitions and preserving the user's dashboard context.
5. **Bottom Navigation Bar (3 Tabs):**
   - **Tools (সব টুলস):** Full master catalog with search and category filters.
   - **Favorites (প্রিয় টুলস):** Bookmarked fast-access shelf with badged item count.
   - **About & Settings (তথ্য ও সেটিংস):** Offline privacy statement, app version, category breakdown statistics.

---

## 6. Functional & Non-Functional Requirements

### 6.1 Functional Requirements (FR)
- **FR-01 (Real-Time Search):** Live search filtering across English name, Bengali name, description, and keyword tags with latency $< 50\text{ms}$.
- **FR-02 (Bookmark Management):** Any tool can be starred/pinned from the grid card or detail sheet. Pinned states must persist across app relaunches via local storage.
- **FR-03 (Precision Arithmetic):** Calculators and financial tools must maintain IEEE 754 double precision without floating-point truncation artifacts (formatted to appropriate decimal places).
- **FR-04 (Hardware Sensor Graceful Fallback):** When a physical sensor is unavailable on a device (e.g. ambient light sensor or magnetometer on low-end hardware), the tool must display an informative notice rather than crashing.
- **FR-05 (Dynamic Theme Support):** Full automatic switching between Material 3 Light Mode and Dark Mode, responding to system configuration.

### 6.2 Non-Functional Requirements (NFR)
- **NFR-01 (Zero Network Mandate):** The core engine must function with 100% feature parity while the device is in Airplane Mode.
- **NFR-02 (Performance):** Cold app launch time $< 800\text{ms}$ on Android mid-range devices; consistent 60–120 FPS scrolling performance.
- **NFR-03 (Footprint):** Final compiled APK size $\le 20\text{MB}$.
- **NFR-04 (Zero Telemetry & Privacy):** No user tracking, no external API calls for user inputs, zero ad-network SDKs.
- **NFR-05 (Accessibility):** Full adherence to WCAG 2.1 AA standards; all vector icons and images provide localized `contentDescription` attributes for TalkBack.

---

## 7. Quality Assurance, Testing & Validation

### 7.1 Test Strategy
- **Unit & Robolectric Testing:**
  - Automated JVM tests verifying data models, conversion formulas (e.g. Land calculation math, BMI formulas, EMI equations).
  - Test task: `gradle :app:testDebugUnitTest` (Passing).
- **UI & Screenshot Regression:**
  - Roborazzi visual regression tests verifying layout integrity and Material 3 rendering (`GreetingScreenshotTest.kt` verifying `MegaUtilityApp`).
- **Compilation & Linting:**
  - Continuous integration checks via `compile_applet` with zero build warnings.

---

## 8. Release Milestones & Roadmap

| Milestone | Target Deliverable | Status |
| :--- | :--- | :--- |
| **M1: Core Architecture** | Project setup, M3 theme, Tool Registry, Preferences manager. | ✅ Completed |
| **M2: All 70 Tools Implementation** | Implementation of all 7 categories (Daily, Storage, Media, Text, Sensors, Units, Finance). | ✅ Completed |
| **M3: UI/UX & Navigation Polish** | Sticky search bar, category chips, 2-column cards, ModalBottomSheet runner, 3-tab bottom bar. | ✅ Completed |
| **M4: Standalone Single-File Web Suite** | Complete offline standalone `index.html` file embedding all 70 tools. | ✅ Completed |
| **M5: Production PRD Documentation** | Formal `PRD.md` covering vision, architecture, taxonomy, specs, and roadmaps. | ✅ Completed |
| **M6: v1.1 Enhancements (Planned)** | Export/Import backup for notes and cashbook; custom color accent selector. | ⏳ Planned |

---

## 9. Conclusion & Sign-Off
This PRD establishes the benchmark for **Mega Utility** as an all-in-one, privacy-first offline utility platform. All features defined in this specification have been engineered and verified across the native Android build and web distributions.
