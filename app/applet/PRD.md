# Mega Utility (মেগা ইউটিলিটি) - Comprehensive Master PRD & System Prompt Specification

> **Project Name:** Mega Utility (মেগা ইউটিলিটি)  
> **Platform Target:** Progressive Web / Standalone Single-File Suite (`index.html`) & Android (Jetpack Compose M3)  
> **Primary Locale:** Bengali (বাংলা) & English (Bilingual UX)  
> **Core Principle:** 100% Offline-First, Zero Ads, Zero Data Tracking, LocalStorage Persistence  
> **Design Language:** Google Material Design 3 (M3) Mobile App Shell (Max width 500px, 18px Card Radius)

---

## 1. Master System Prompt (যেকোনো AI-তে কপি-পেস্ট করে হুবহু অ্যাপ বানানোর প্রম্পট)

> *নিচের এই প্রম্পটটি কপি করে Claude, ChatGPT, v0, Bolt বা যেকোনো AI-তে পেস্ট করলে এই অ্যাপের হুবহু লেটেস্ট প্রিভিউ এবং কোড তৈরি হয়ে যাবে:*

```markdown
Act as a Senior Principal Frontend & Android Engineer. Build a complete, single-file, production-ready, 100% offline utility web application named "Mega Utility (মেগা ইউটিলিটি)" in valid HTML5, CSS3, and modern vanilla JavaScript (ES6+). It must replicate the exact UI/UX, responsive mobile container, Material Design 3 styling, color palette, navigation tabs, and all 70 functional tools described below.

### 🎨 Exact Color Palette & CSS Variables:
- Primary Blue: #2563eb (Hover: #1d4ed8, Light: #eff6ff)
- Background: #f8fafc | Surface: #ffffff | Surface Variant: #f1f5f9
- Text Main: #0f172a | Text Muted: #64748b | Border: #e2e8f0
- Danger: #ef4444 | Success: #16a34a
- Category Colors:
  * Daily: #2563eb
  * Storage: #ea580c
  * Media: #9333ea
  * Text: #0d9488
  * Sensors: #0284c7
  * Units: #4f46e5
  * Finance: #059669
- Dark Mode Tokens:
  * Surface: #0f172a, Background: #020617, Surface Variant: #1e293b, Text Main: #f8fafc, Text Muted: #94a3b8, Border: #334155

### 📱 UI / UX Shell & Layout Structure:
1. Mobile-First Frame: Centered container `.app-container` (max-width: 500px, min-height: 100vh, box-shadow).
2. Sticky Header: Title "Mega Utility" with subtitle "৭০টি অফলাইন স্মার্ট টুলস" and a pill badge showing total count "৭০".
3. Sticky Search Bar: Input with search icon (🔍), placeholder "টুল খুঁজুন (যেমন: ক্যালকুলেটর, নোট, কিউআর)...", and instant Clear button (`✕`). Filters tools in real-time across English name, Bengali name, category, and tags.
4. Horizontal Category Scroll Chips: "সব (70)", "দৈনন্দিন (11)", "স্টোরেজ (8)", "মিডিয়া (12)", "টেক্সট (8)", "সেন্সর (14)", "কনভার্টার (8)", "হিসাব ও স্বাস্থ্য (9)". Active chip has category accent background.
5. 2-Column Responsive Grid: Cards with 18px border radius, subtle hover lift, category-colored circular icon badge (42x42px), title in Bengali, subtitle in English, and a bookmark heart toggle button (❤️ / 🤍) in the top-right corner.
6. Bottom Sheet Modal: Slides up from bottom when any tool card is tapped. Includes drag handle bar, modal header with tool icon, title, and close button (`✕`), dynamic interactive body, and smooth animation.
7. Bottom Navigation Bar (Fixed 3 Tabs):
   - Tab 1: "সব টুলস" (Tools - Grid with Search & Filters)
   - Tab 2: "পছন্দের" (Favorites - Displays only bookmarked tools, dynamic count badge)
   - Tab 3: "তথ্য ও সেটিংস" (About - App info, offline guarantee, Reset / Clear App Data button)

### 🚀 Critical Features & Recent Implementations:
1. Quick Notes & To-Do Tool (Tool ID: 5):
   - Input field + "+ যোগ করুন" button (and Enter key support).
   - Real-time search bar inside modal: "🔍 নোট খুঁজুন (Search notes in real-time)..." with clear button `✕`.
   - Real-time counter: "নোট তালিকা (ফলাফল: X / Y)".
   - "Download as Text (.txt)" button: Exports all notes into a formatted `.txt` file using Blob & URL.createObjectURL.
   - Checkbox toggle for marking completed (strikethrough text) and delete button (`🗑️`).
   - LocalStorage persistence under key `mega_quick_notes`.
2. Clear App Data & Cache (About Tab):
   - Red-accented card with confirmation prompt: "আপনার ফেভারিট বুকমার্ক, থিম সেটিংস ও লোকাল স্টোরেজ ক্যাশ মুছে ডিফল্ট অবস্থায় ফেরাতে চান?".
   - Button "Clear App Data / Cache" that runs `localStorage.clear()`, resets state, and reloads clean.
3. Localized Bangladeshi Calculations:
   - Land Calculator (Tool 57): শতক (435.6 sq ft), কাঠা (720 sq ft = 1.65 শতক), বিঘা (20 কাঠা = 14,400 sq ft), একর (100 শতক = 43,560 sq ft).
   - Cash Denomination Counter (Tool 11): ৳1000, ৳500, ৳200, ৳100, ৳50, ৳20, ৳10, ৳5, ৳2, ৳1 with live grand total in Taka.
   - Daily Cashbook POS (Tool 62): Cash-in & Cash-out transaction history with running balance and timestamp.
   - EMI Loan Calculator (Tool 63), VAT/Tax Calculator (Tool 64), Discount Calculator (Tool 65), Compound Interest (Tool 66).
   - Health: BMI Calculator (Tool 67) with gauge categories, Water Tracker (Tool 68) with +250ml/+500ml quick add and progress bar, 4-7-8 Breathing Meditation (Tool 69) with animated breathing pulse circle.
```

---

## 2. Complete Catalog of All 70 Tools (টুলস মাস্টার ইনভেন্টরি)

### Category 1: দৈনন্দিন ও সাধারণ জীবন (Daily Utilities - ১১টি)
1. **স্মার্ট ক্যালকুলেটর (Smart Calculator)** - সাধারণ গাণিতিক হিসাব, রিয়েল-টাইম সমাধান।
2. **সাইন্টিফিক ক্যালকুলেটর (Scientific Calculator)** - ত্রিকোণমিতি, লগ, ঘাত, রুট, $\pi$, $e$।
3. **বয়স ক্যালকুলেটর (Age Calculator)** - বছর, মাস, দিন এবং পরবর্তী জন্মদিনের কাউন্টডাউন।
4. **বিশ্ব ঘড়ি (World Clock)** - ঢাকা, লন্ডন, নিউ ইয়র্ক, টোকিও, দুবাই, সিডনি লোকাল সময়।
5. **কুইক নোটস ও করণীয় (Quick Notes & To-Do)** - নোট লেখা, রিয়েল-টাইম সার্চ, টেক্সট ডাউনলোড (.txt), চেকলিস্ট ও স্টোরেজ।
6. **ডিজিটাল তাসবিহ (Digital Tasbih)** - ভাইব্রেশনসহ কাউন্টার, টার্গেট সেট, রিসেট।
7. **স্টপওয়াচ (Stopwatch)** - ল্যাপ রেকর্ডিংসহ মিলিসেকেন্ড নির্ভুল টাইমার।
8. **কাউন্টডাউন টাইমার (Countdown Timer)** - মিনিট/সেকেন্ড সেট করে অ্যালার্ম নোটিফিকেশন।
9. **ক্যালেন্ডার ও ছুটির দিন (Calendar & Holidays)** - দিন গণনা ও ক্যালেন্ডার ভিউ।
10. **টাকা কাউন্টার (Cash Denomination Counter)** - ১০০০, ৫০০, ২০০, ১০০, ৫০, ২০, ১০, ৫, ২, ১ টাকার লাইভ হিসাব।
11. **পাসওয়ার্ড জেনারেটর (Strong Password Generator)** - সিম্বল, ডিজিট, বড়-ছোট হাতের অক্ষরসহ হাই-সিকিউরিটি পাসওয়ার্ড।

### Category 2: ফাইল ও স্টোরেজ টুলস (File & Storage Utilities - ৮টি)
12. **ডুপ্লিকেট ফাইল স্ক্যানার (Duplicate File Cleaner)** - স্টোরেজের নকল ফাইল শনাক্তকরণ নির্দেশিকা।
13. **ক্যাশ ও জঙ্ক ক্লিনার (Junk & Cache Cleaner)** - ডিভাইস ক্যাশ ও ব্রাউজার লোকাল ক্যাশ ক্লিয়ারিং।
14. **বড় ফাইল ফাইন্ডার (Large File Finder)** - ৫০MB/১০০MB/৫০০MB এর বড় ফাইল শনাক্তকরণ।
15. **স্টোরেজ বিশ্লেষক (Storage Visualizer)** - মেমরি ব্যবহার ও ক্যাটাগরিভিত্তিক পরিসংখ্যান।
16. **এপিকে এক্সট্র্যাক্টর গাইড (APK Extractor & Backup)** - ইনস্টল থাকা অ্যাপ ব্যাকআপ নেওয়ার কৌশল।
17. **গোপন ফাইল ও ভল্ট (Document & File Locker)** - পিন কোড সিকিউর প্রাইভেট ভল্ট গাইড।
18. **টেক্সট ও সিএসভি রিডার (TXT / CSV File Viewer)** - সরাসরি ফাইল আপলোড করে অফলাইন পড়ার সুবিধা।
19. **ফাইল এনক্রিপশন (File Encryptor / Decryptor)** - টেক্সট ও ফাইল পাসওয়ার্ড দিয়ে লক করার ব্যবস্থা।

### Category 3: মিডিয়া ও গ্রাফিক্স টুলস (Media & Graphics - ১২টি)
20. **কিউআর কোড স্ক্যানার (QR Code Scanner)** - ক্যামেরা ও ইমেজ আপলোড দিয়ে কিউআর পড়ার টুল।
21. **কিউআর কোড জেনারেটর (QR Code Generator)** - টেক্সট, ইউআরএল, ওয়াইফাই কিউআর কোড তৈরি ও ডাউনলোড।
22. **ছবি কম্প্রেশার (Image Size Reducer)** - ছবির সাইজ কমিয়ে KB নির্দিষ্টকরণ।
23. **ছবি ক্রপ ও রোটেট (Image Crop & Rotate)** - ১:১, ৪:৩, ১৬:৯ সাইজ ক্রপিং।
24. **কালার পিকার ও ব্লেন্ডার (Color Picker & Palette)** - হেক্স, আরজিবি, এইচএসএল কোড সংগ্রহ।
25. **টেক্সট থেকে পিডিএফ (Text to PDF Maker)** - লেখা থেকে সরাসরি পিডিএফ ফাইল তৈরি।
26. **ছবি থেকে পিডিএফ (Image to PDF Converter)** - একাধিক ছবি যুক্ত করে ১টি পিডিএফ ফাইল ডাউনলোড।
27. **ব্যাকগ্রাউন্ড রিমুভার গাইড (Background Remover)** - ছবির পেছনের অংশ কাটানোর অফলাইন ক্যানভাস পদ্ধতি।
28. **অডিও রেকর্ডার (Audio Recorder)** - অফলাইনে সরাসরি মাইক্রোফোন দিয়ে অডিও রেকর্ড ও প্লেব্যাক।
29. **ভিডিও থেকে অডিও (Video to Audio Extractor)** - ভিডিও ফাইল থেকে MP3/WAV সাউন্ড আলাদা করা।
30. **পিডিএফ মার্জ ও স্প্লিট (PDF Merge & Split Tool)** - একাধিক ফাইল জোড়া দেওয়া বা পৃষ্ঠা আলাদা করা।
31. **ডিজিটাল ড্রয়িং ও স্বাক্ষর (Drawing & Signature Pad)** - টাচ স্ক্রিনে স্বাক্ষর করে পিএনজি ডাউনলোড।

### Category 4: টেক্সট ও কনটেন্ট টুলস (Text & Content - ৮টি)
32. **শব্দ ও অক্ষর কাউন্টার (Word & Character Counter)** - শব্দ, অক্ষর, লাইন, বাক্য ও পড়ার সময় গণনা।
33. **টেক্সট কেস কনভার্টার (Text Case Converter)** - UPPERCASE, lowercase, Title Case, camelCase ইত্যাদি।
34. **ফ্যান্সি স্টাইলিশ টেক্সট (Fancy Stylized Text)** - ইউনিকোড স্টাইলিশ ফন্ট সোশ্যাল মিডিয়া বায়োর জন্য।
35. **টেক্সট রিপিটার (Text Repeater)** - যেকোনো বাক্য হাজার বার রিপিট ও কপি।
36. **ডুপ্লিকেট লাইন রিমুভার (Duplicate Line Remover)** - তালিকা থেকে ডুপ্লিকেট নাম বা লাইন মুছে ফেলা।
37. **বেস৬৪ ও বাইনারি এনকোডার (Base64 & Binary Encoder)** - টেক্সট থেকে বাইনারি, হেক্স ও বেস৬৪ রূপান্তর।
38. **ইউনিকোড টু বিজয় কনভার্টার (Unicode to Bijoy Converter)** - বাংলা ফন্ট কনভার্সন গাইড ও নিয়মাবলী।
39. **ছবি থেকে লেখা (OCR Text Extractor)** - ছবি আপলোড করে লেখা কপি করার টুল।

### Category 5: সেন্সর ও মেজারমেন্ট টুলস (Sensors & Measurement - ১৪টি)
40. **ফ্ল্যাশলাইট ও এসওএস (Flashlight & SOS Strobe)** - স্ক্রিন টর্চ ও ক্যামেরা ফ্ল্যাশ এসওএস সিগন্যাল।
41. **ডিজিটাল কম্পাস (Digital Compass)** - ৩৬০ ডিগ্রি দিকদর্শন ও কিবলা নির্ণয়।
42. **স্পিরিট লেভেল (Bubble / Spirit Level)** - সারফেস সমান কিনা তা মাপার বাবল লেভেলার।
43. **শব্দ ও ডেসিবেল মিটার (Sound & Noise Meter)** - মাইক্রোফোন দিয়ে পরিবেশের শব্দ পরিমাপ (dB)।
44. **ডিভাইস তথ্য ও স্পেক্স (Device Info & Hardware)** - ডিসপ্লে রেজুলেশন, প্ল্যাটফর্ম, ব্রাউজার ইঞ্জিন।
45. **ব্যাটারি হেলথ মনিটর (Battery Health & Stats)** - ব্যাটারি চার্জ শতাংশ ও পাওয়ার সেভিং টিপস।
46. **ভাইব্রেশন ও কম্পন টেস্টার (Vibratometer)** - ডিভাইসের হ্যাপটিক ভাইব্রেশন প্যাটার্ন টেস্টিং।
47. **স্ক্রিন ডেড পিক্সেল টেস্টার (Dead Pixel Screen Tester)** - লাল, সবুজ, নীল, সাদা, কালো ফুলস্ক্রিন সাইকেল।
48. **স্ক্রিন স্কেল / রুলার (Screen Ruler)** - স্ক্রিনের ওপর সেমি (cm) ও ইঞ্চি (inch) স্কেল।
49. **১৮০° চাঁদা / প্রোট্রাক্টর (180° Protractor)** - স্ক্রিনে জ্যামিতিক কোণ মাপার চাঁদা।
50. **মেটাল ও চৌম্বক ডিটেক্টর (Metal & Magnetic Field)** - ম্যাগনেটোমিটার সেন্সরের সাহায্যে মেটাল ডিটেকশন।
51. **আলোর তীব্রতা মিটার (Ambient Light Meter)** - লাক্স (Lux) এককে আলোর পরিমাণ পরিমাপ।
52. **শব্দ কম্পাঙ্ক জেনারেটর (Audio Tone Frequency)** - ২০ Hz থেকে ২০,০০০ Hz পর্যন্ত সাইন ওয়েভ সাউন্ড জেনারেশন।
53. **পকেট আয়না (Pocket Mirror)** - ফ্রন্ট ক্যামেরা চালু করে পকেট আয়না হিসেবে ব্যবহার।

### Category 6: একক রূপান্তর ক্যালকুলেটর (Unit Converters - ৮টি)
54. **দৈর্ঘ্য ও দূরত্ব কনভার্টার (Length Converter)** - মিটার, কিলোমিটার, ফুট, ইঞ্চি, মাইল ইত্যাদি।
55. **ওজন ও ভর কনভার্টার (Weight & Mass)** - কেজি, গ্রাম, পাউন্ড, আউন্স, মণ, টন।
56. **তাপমাত্রা কনভার্টার (Temperature Converter)** - সেলসিয়াস (°C), ফারেনহাইট (°F), কেলভিন (K)।
57. **জমি ও ভূমির পরিমাপ (Land Area Measurement - শতক, কাঠা, বিঘা)**:
    - ১ শতক / ডেসিমেল = ৪৩৫.৬ বর্গফুট
    - ১ কাঠা = ৭২০ বর্গফুট = ১.৬৫ শতক
    - ১ বিঘা = ২০ কাঠা = ১৪,৪০০ বর্গফুট = ৩৩.০৬ শতক
    - ১ একর = ১০০ শতক = ৩.০২৫ বিঘা = ৪৩,৫৬০ বর্গফুট
58. **ডেটা স্টোরেজ কনভার্টার (Data Storage Converter)** - Byte, KB, MB, GB, TB, PB (1024 base)।
59. **গতি কনভার্টার (Speed Converter)** - km/h, mph, m/s, knots।
60. **তরল আয়তন কনভার্টার (Volume Converter)** - লিটার, মিলিলিটার, গ্যালন, কাপ।
61. **তেল খরচ ও মাইলেজ (Fuel & Mileage Calculator)** - কিলোমিটার প্রতি তেলের খরচ ও ট্রিপ কস্ট।

### Category 7: ফিন্যান্স, স্বাস্থ্য ও হিসাবরক্ষণ (Finance, Health & POS - ৯টি)
62. **দৈনিক ক্যাশবুক খাতা (Daily POS & Cashbook)** - ক্যাশ ইন / ক্যাশ আউট হিসাব, ব্যালেন্স ট্র্যাকিং।
63. **লোন / ইএমআই ক্যালকুলেটর (EMI Loan Calculator)** - আসল, সুদের হার ও মেয়াদের ভিত্তিতে মাসিক কিস্তি ও মোট সুদ।
64. **ভ্যাট ও ট্যাক্স ক্যালকুলেটর (VAT / GST Calculator)** - ৫%, ৭.৫%, ১০%, ১৫% ভ্যাটসহ ও ভ্যাট বাদে মোট মূল্য।
65. **ডিসকাউন্ট ও সেভিংস (Discount & Savings)** - আসল মূল্য ও শতকরা ছাড় হিসাব।
66. **চক্রবৃদ্ধি সুদ (Compound Interest)** - বছর ও কিস্তির ভিত্তিতে মোট মুনাফা ও ব্যালেন্স।
67. **বিএমআই স্বাস্থ্য ক্যালকুলেটর (BMI Calculator)** - ওজন (কেজি) ও উচ্চতা অনুযায়ী আন্ডারওয়েট/স্বাভাবিক/ওভারওয়েট।
68. **দৈনিক পানি পানের ট্র্যাকার (Daily Water Tracker)** - দৈনিক লক্ষ্য (যেমন ২৫০০ মিলি), +২৫০ml/+৫০০ml কুইক বোতাম ও প্রগ্রেস বার।
69. **৪-৭-৮ রিল্যাক্সেশন শ্বাস প্রশ্বাস (4-7-8 Breathing Guide)** - ৪ সেকেন্ড নিঃশ্বাস নেওয়া, ৭ সেকেন্ড আটকে রাখা, ৮ সেকেন্ড ছাড়া (অ্যানিমেটেড পালসিং সার্কেল)।
70. **নেটওয়ার্ক ডায়াগনস্টিক (Network Diagnostic)** - অনলাইন/অফলাইন কানেক্টিভিটি স্ট্যাটাস ও তথ্য।

---

## 3. UI/UX Interaction Flows & Micro-Details

### ১. হোমপেজ টুল সার্চিং ও ফিল্টারিং ফ্লো:
- ব্যবহারকারী সার্চ বারে যেকোনো শব্দ (বাংলা বা ইংরেজি) লিখলেই সাথে সাথে গ্রিড ফিল্টার হয়।
- সার্চ বক্সের ডানে '✕' বোতামে চাপ দিলে তৎক্ষণাৎ সব টুলস আবার চলে আসে।
- ক্যাটাগরি চিপসে ক্লিক করলে নির্দিষ্ট ক্যাটাগরির টুলগুলো ফিল্টার হয়ে আসে এবং চিপের রঙ ক্যাটাগরির নির্দিষ্ট রঙে হাইলাইট হয়।

### ২. বুকমার্ক ও ফেভারিটস ফ্লো:
- যেকোনো টুল কার্ডের ওপর ডানপাশের হার্ট আইকন (❤️) এ চাপ দিলে টুলটি প্রিয় তালিকায় সেভ হয়।
- বটম নেভিগেশনের "পছন্দের" ট্যাবে গেলে শুধু বুকমার্ক করা টুলগুলো পাওয়া যায়।
- সব ডেটা ব্রাউজারের `localStorage`-এ সেভ থাকে, রিফ্রেশ করলেও অক্ষুণ্ণ থাকে।

### ৩. কুইক নোটস সার্চ ও টেক্সট ডাউনলোড ফ্লো:
- কুইক নোটস ওপেন করে ইনপুট ফিল্ডে নোট লিখে এন্টার চাপলে বা "+ যোগ করুন" চাপলে নোট যুক্ত হয়।
- নোট তালিকায় অনেকগুলো নোট থাকলে "🔍 নোট খুঁজুন" বক্সে টাইপ করার সাথে সাথে রিয়েল-টাইমে ফিল্টার হয়।
- "Download as Text (.txt)" বাটনে চাপ দিলে সবগুলো নোট স্বয়ংক্রিয়ভাবে তারিখসহ একটি `.txt` ফাইল আকারে ডাউনলোড হয়ে যায়।

### ৪. অ্যাপ ডেটা ও ক্যাশ ক্লিয়ার ফ্লো:
- "তথ্য ও সেটিংস" ট্যাবে গেলে একটি লাল বর্ডারের ডেডিকেটেড কার্ড আছে।
- "Clear App Data / Cache" বোতামে চাপ দিলে একটি সুন্দর কনফার্মেশন প্রম্পট আসে। ব্যবহারকারী সম্মতি দিলে সব ক্যাশ ও লোকাল স্টোরেজ মুছে অ্যাপ ফ্রেশ রিস্টার্ট নেয়।
