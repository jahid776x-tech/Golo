package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PreferencesManager
import com.example.data.ToolRegistry
import com.example.model.ToolCategory
import com.example.model.ToolItem
import com.example.ui.ToolModalDispatcher
import com.example.ui.theme.MyApplicationTheme
import com.example.data.ThemeMode
import com.example.data.ThemePreferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val themePreferences = remember { ThemePreferences(context) }
            val currentThemeMode by themePreferences.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
            val coroutineScope = rememberCoroutineScope()

            MyApplicationTheme(themeMode = currentThemeMode) {
                MegaUtilityApp(
                    themeMode = currentThemeMode,
                    onThemeModeChange = { newMode ->
                        coroutineScope.launch {
                            themePreferences.setThemeMode(newMode)
                        }
                    }
                )
            }
        }
    }
}

enum class NavigationTab(val title: String, val banglaTitle: String) {
    TOOLS("Tools", "সব টুলস"),
    FAVORITES("Favorites", "প্রিয় টুলস"),
    ABOUT("About", "তথ্য ও সেটিংস")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MegaUtilityApp(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onThemeModeChange: (ThemeMode) -> Unit = {}
) {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }
    var favoriteIds by remember { mutableStateOf(prefsManager.getFavoriteIds()) }
    var selectedTab by remember { mutableStateOf(NavigationTab.TOOLS) }
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ToolCategory?>(null) }
    var activeModalTool by remember { mutableStateOf<ToolItem?>(null) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val allTools = remember { ToolRegistry.allTools }

    val filteredTools = remember(searchQuery, selectedCategory, selectedTab, favoriteIds) {
        val baseList = if (selectedTab == NavigationTab.FAVORITES) {
            allTools.filter { favoriteIds.contains(it.id) }
        } else {
            allTools
        }
        val query = searchQuery.trim().lowercase()
        baseList.filter { tool ->
            val matchesCat = selectedCategory == null || tool.category == selectedCategory
            val matchesQuery = query.isEmpty() ||
                tool.name.lowercase().contains(query) ||
                tool.banglaName.lowercase().contains(query) ||
                tool.description.lowercase().contains(query) ||
                tool.tags.any { it.contains(query) }
            matchesCat && matchesQuery
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            ) {
                NavigationBarItem(
                    selected = selectedTab == NavigationTab.TOOLS,
                    onClick = { selectedTab = NavigationTab.TOOLS },
                    icon = { Icon(Icons.Default.GridView, contentDescription = "Tools") },
                    label = { Text(NavigationTab.TOOLS.banglaTitle, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = selectedTab == NavigationTab.FAVORITES,
                    onClick = { selectedTab = NavigationTab.FAVORITES },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (favoriteIds.isNotEmpty()) {
                                    Badge { Text("${favoriteIds.size}") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                        }
                    },
                    label = { Text(NavigationTab.FAVORITES.banglaTitle, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = selectedTab == NavigationTab.ABOUT,
                    onClick = { selectedTab = NavigationTab.ABOUT },
                    icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                    label = { Text(NavigationTab.ABOUT.banglaTitle, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                NavigationTab.TOOLS, NavigationTab.FAVORITES -> {
                    ToolsCatalogScreen(
                        selectedTab = selectedTab,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        selectedCategory = selectedCategory,
                        onCategorySelect = { selectedCategory = it },
                        tools = filteredTools,
                        favoriteIds = favoriteIds,
                        onToggleFavorite = { id ->
                            prefsManager.toggleFavorite(id)
                            favoriteIds = prefsManager.getFavoriteIds()
                        },
                        onToolClick = { tool -> activeModalTool = tool }
                    )
                }
                NavigationTab.ABOUT -> {
                    AboutSettingsScreen(
                        totalTools = allTools.size,
                        favoriteCount = favoriteIds.size,
                        themeMode = themeMode,
                        onThemeModeChange = onThemeModeChange,
                        onClearData = {
                            prefsManager.clearAll()
                            favoriteIds = emptySet()
                            onThemeModeChange(ThemeMode.SYSTEM)
                            coroutineScope.launch {
                                ThemePreferences(context).clearAll()
                            }
                            android.widget.Toast.makeText(context, "App data & cache cleared successfully!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // Bottom Sheet Modal for active tool
            if (activeModalTool != null) {
                ModalBottomSheet(
                    onDismissRequest = { activeModalTool = null },
                    sheetState = bottomSheetState,
                    containerColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    ToolModalDispatcher(
                        tool = activeModalTool!!,
                        onDismiss = { activeModalTool = null }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsCatalogScreen(
    selectedTab: NavigationTab,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: ToolCategory?,
    onCategorySelect: (ToolCategory?) -> Unit,
    tools: List<ToolItem>,
    favoriteIds: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onToolClick: (ToolItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // App Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (selectedTab == NavigationTab.FAVORITES) "Favorite Tools" else "Mega Utility",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (selectedTab == NavigationTab.FAVORITES) "আপনার বুকমার্ক করা প্রয়োজনীয় টুলস" else "৭০টি অফলাইন স্মার্ট টুলস প্যাকেজ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                Text(
                    text = "${tools.size} Tools",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_bar"),
            placeholder = { Text("টুলস খুঁজুন / Search 70 offline tools...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
            )
        )

        Spacer(Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { onCategorySelect(null) },
                    label = { Text("সব (All 70)") },
                    shape = RoundedCornerShape(12.dp)
                )
            }
            items(ToolCategory.values()) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelect(if (selectedCategory == category) null else category) },
                    leadingIcon = {
                        Icon(
                            category.icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = category.accentColor
                        )
                    },
                    label = { Text(category.banglaTitle, fontSize = 12.sp) },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Tool Grid
        if (tools.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "কোনো টুল পাওয়া যায়নি!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "অন্য কি-ওয়ার্ড বা ক্যাটাগরি দিয়ে চেষ্টা করুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tools, key = { it.id }) { tool ->
                    ToolGridCard(
                        tool = tool,
                        isFavorite = favoriteIds.contains(tool.id),
                        onToggleFavorite = { onToggleFavorite(tool.id) },
                        onClick = { onToolClick(tool) }
                    )
                }
            }
        }
    }
}

@Composable
fun ToolGridCard(
    tool: ToolItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(148.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("tool_card_${tool.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Category-tinted Icon Container
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = tool.category.accentColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            tool.icon,
                            contentDescription = tool.name,
                            tint = tool.category.accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Bookmark / Favorite Heart
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFEF4444) else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column {
                Text(
                    text = tool.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = tool.banglaName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Category Mini-Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(tool.category.accentColor)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = tool.category.banglaTitle,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun AboutSettingsScreen(
    totalTools: Int,
    favoriteCount: Int,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onClearData: () -> Unit = {}
) {
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            icon = {
                Icon(
                    Icons.Default.DeleteSweep,
                    contentDescription = "Clear Data",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text("Clear App Data / ডেটা রিসেট")
            },
            text = {
                Text("আপনি কি নিশ্চিত যে অ্যাপের সমস্ত ডেটা ও ক্যাশ মুছে ফেলতে চান? আপনার ফেভারিট/বুকমার্ক তালিকা এবং সংরক্ষিত থিম ডিফল্ট অবস্থায় ফিরে যাবে।")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showClearDialog = false
                        onClearData()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("মুছে ফেলুন (Clear)", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearDialog = false }) {
                    Text("বাতিল (Cancel)")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(androidx.compose.foundation.rememberScrollState())
    ) {
        Text("About & Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("স্মার্ট, আধুনিক, ক্লিন এবং এক ক্লিকে টুলস খোঁজার সেরা অফলাইন অ্যাপ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)

        Spacer(Modifier.height(16.dp))

        // Persistent Theme Switcher Card (DataStore Preferences)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("theme_switcher_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Palette,
                                contentDescription = "Theme",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            "Theme & Display / থিম",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            when (themeMode) {
                                ThemeMode.SYSTEM -> "System Default (সিস্টেম অনুযায়ী)"
                                ThemeMode.LIGHT -> "Light Mode (উজ্জ্বল লাইট মোড)"
                                ThemeMode.DARK -> "Dark Mode (ডার্ক মোড)"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // 3 Options Row: System, Light, Dark
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeOptionButton(
                        modifier = Modifier.weight(1f),
                        title = "System",
                        banglaTitle = "সিস্টেম",
                        icon = Icons.Default.BrightnessAuto,
                        isSelected = themeMode == ThemeMode.SYSTEM,
                        onClick = { onThemeModeChange(ThemeMode.SYSTEM) }
                    )
                    ThemeOptionButton(
                        modifier = Modifier.weight(1f),
                        title = "Light",
                        banglaTitle = "লাইট",
                        icon = Icons.Default.LightMode,
                        isSelected = themeMode == ThemeMode.LIGHT,
                        onClick = { onThemeModeChange(ThemeMode.LIGHT) }
                    )
                    ThemeOptionButton(
                        modifier = Modifier.weight(1f),
                        title = "Dark",
                        banglaTitle = "ডার্ক",
                        icon = Icons.Default.DarkMode,
                        isSelected = themeMode == ThemeMode.DARK,
                        onClick = { onThemeModeChange(ThemeMode.DARK) }
                    )
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    text = when (themeMode) {
                        ThemeMode.SYSTEM -> "অ্যান্ড্রয়েড সিস্টেম সেটিংসের সাথে স্বয়ংক্রিয়ভাবে থিম পরিবর্তিত হবে।"
                        ThemeMode.LIGHT -> "সবসময় পরিষ্কার ও উজ্জ্বল লাইট থিম ইন্টারফেস প্রদর্শিত হবে।"
                        ThemeMode.DARK -> "ব্যাটারি সাশ্রয়ী ডার্ক থিম ইন্টারফেস সবসময় প্রদর্শিত হবে।"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Share Mega Utility Button Card
        val currentContext = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("share_mega_utility_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            "Share Mega Utility / অ্যাপ শেয়ার",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "পরিচিতদের সাথে ৭০টি অফলাইন টুলসের এই অ্যাপটি শেয়ার করুন",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Button(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Mega Utility: ৭০টি সম্পূর্ণ অফলাইন স্মার্ট টুলস - ক্যালকুলেটর, কিউআর কোড স্ক্যানার, জমি পরিমাপ, ক্যাশ কাউন্টার ও আরও অনেক কিছু! ডাউনলোড করুন: https://mega-utility.app"
                            )
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Mega Utility")
                        currentContext.startActivity(shareIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("share_mega_utility_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share Icon",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Share Mega Utility",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Clear App Data / Cache Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("clear_cache_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.DeleteSweep,
                                contentDescription = "Clear Cache",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            "Clear App Data & Cache / ডেটা মুছুন",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "বুকমার্ক, কাস্টম সেটিংস ও সংরক্ষিত ডেটা রিসেট করুন",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { showClearDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("clear_cache_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Clear App Data / Cache",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("App Highlights", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(Modifier.height(8.dp))
                Text("• $totalTools Total Offline Tools Integrated")
                Text("• $favoriteCount Tools Pinned / Bookmarked")
                Text("• 7 Organized Modern Categories")
                Text("• 100% Offline: Zero data tracking, zero ads, zero internet dependency")
                Text("• Material Design 3 Dynamic Layouts")
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Categories Breakdown", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        ToolCategory.values().forEach { cat ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = cat.accentColor.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(cat.icon, contentDescription = null, tint = cat.accentColor, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(cat.title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                            Text(cat.banglaTitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                    Text("${ToolRegistry.allTools.count { it.category == cat }} tools", fontWeight = FontWeight.Bold, color = cat.accentColor)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Version 1.0.0 • Offline Suite", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun ThemeOptionButton(
    modifier: Modifier = Modifier,
    title: String,
    banglaTitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("theme_btn_${title.lowercase()}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
            Text(
                banglaTitle,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
        }
    }
}
