package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class ToolCategory(
    val title: String,
    val banglaTitle: String,
    val icon: ImageVector,
    val primaryColor: Color,
    val containerColor: Color,
    val onContainerColor: Color
) {
    DAILY(
        title = "Daily Utilities",
        banglaTitle = "দৈনন্দিন ইউটিলিটি",
        icon = Icons.Default.Calculate,
        primaryColor = Color(0xFF2563EB),     // Accent Blue
        containerColor = Color(0xFFEFF6FF),
        onContainerColor = Color(0xFF1E40AF)
    ),
    STORAGE(
        title = "File & Cleaner",
        banglaTitle = "স্টোরেজ ও ফাইল ক্লিনার",
        icon = Icons.Default.CleaningServices,
        primaryColor = Color(0xFFEA580C),     // Red / Orange
        containerColor = Color(0xFFFFF7ED),
        onContainerColor = Color(0xFF9A3412)
    ),
    MEDIA(
        title = "Media & Graphics",
        banglaTitle = "মিডিয়া ও গ্রাফিক্স",
        icon = Icons.Default.Image,
        primaryColor = Color(0xFF9333EA),     // Purple / Violet
        containerColor = Color(0xFFFAF5FF),
        onContainerColor = Color(0xFF6B21A8)
    ),
    TEXT(
        title = "Text & Content",
        banglaTitle = "টেক্সট ও কন্টেন্ট",
        icon = Icons.Default.TextFields,
        primaryColor = Color(0xFF0D9488),     // Green / Teal
        containerColor = Color(0xFFF0FDFA),
        onContainerColor = Color(0xFF115E59)
    ),
    SENSORS(
        title = "Sensors & Hardware",
        banglaTitle = "সেন্সর ও হার্ডওয়্যার",
        icon = Icons.Default.Sensors,
        primaryColor = Color(0xFF0284C7),     // Cyan / Amber
        containerColor = Color(0xFFF0F9FF),
        onContainerColor = Color(0xFF075985)
    ),
    UNITS(
        title = "Unit Converters",
        banglaTitle = "একক রূপান্তরকারী",
        icon = Icons.Default.Straighten,
        primaryColor = Color(0xFF4F46E5),     // Indigo / Deep Blue
        containerColor = Color(0xFFEEF2FF),
        onContainerColor = Color(0xFF3730A3)
    ),
    FINANCE(
        title = "Finance & Health",
        banglaTitle = "অর্থ ও স্বাস্থ্য ট্র্যাকিং",
        icon = Icons.Default.Payments,
        primaryColor = Color(0xFF059669),     // Emerald Green
        containerColor = Color(0xFFECFDF5),
        onContainerColor = Color(0xFF065F46)
    );

    val accentColor: Color get() = primaryColor
}

data class ToolItem(
    val id: Int,
    val name: String,
    val banglaName: String,
    val category: ToolCategory,
    val icon: ImageVector,
    val description: String,
    val isPinned: Boolean = false,
    val tags: List<String> = emptyList()
)
