package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.local.AppDatabase
import com.example.data.remote.GeminiService
import com.example.data.repository.CareerRepository
import com.example.ui.CareerPathViewModel
import com.example.ui.CareerPathViewModelFactory
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CareerPathApp()
            }
        }
    }
}

data class NavItem(
    val route: String,
    val label: String,
    val outlineIcon: ImageVector,
    val filledIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerPathApp() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val geminiService = remember { GeminiService() }
    val repository = remember { CareerRepository(database.careerPathDao(), geminiService) }
    val viewModel: CareerPathViewModel = viewModel(factory = CareerPathViewModelFactory(repository))

    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

    var showNotificationsDialog by remember { mutableStateOf(false) }

    val navItems = listOf(
        NavItem("dashboard", "Home", Icons.Outlined.Home, Icons.Filled.Home),
        NavItem("roadmap", "Roadmap", Icons.Outlined.Timeline, Icons.Filled.Timeline),
        NavItem("skill_gap", "Skills", Icons.Outlined.BarChart, Icons.Filled.BarChart),
        NavItem("projects", "Projects", Icons.Outlined.Folder, Icons.Filled.Folder),
        NavItem("ai_mentor", "Mentor", Icons.Outlined.AutoAwesome, Icons.Filled.AutoAwesome),
        NavItem("more_tools", "More", Icons.Outlined.Widgets, Icons.Filled.Widgets)
    )

    val isTopLevelRoute = currentRoute in navItems.map { it.route }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        val isDesktop = maxWidth >= 720.dp

        Row(modifier = Modifier.fillMaxSize()) {
            // Desktop Left Sidebar Navigation
            if (isDesktop) {
                Surface(
                    color = CardWhite,
                    modifier = Modifier
                        .width(240.dp)
                        .fillMaxHeight(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            // Logo & Branding
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.linearGradient(listOf(RoyalBlue, Purple))
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Explore,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "CareerPath AI",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy
                                        )
                                    )
                                    Text(
                                        text = "Plan • Learn • Build • Grow",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            // Sidebar Navigation Items
                            navItems.forEach { item ->
                                val selected = currentRoute == item.route
                                Surface(
                                    color = if (selected) SoftBlue else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (selected) item.filledIcon else item.outlineIcon,
                                            contentDescription = item.label,
                                            tint = if (selected) RoyalBlue else TextSecondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = item.label,
                                            color = if (selected) RoyalBlue else TextMain,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Bottom Profile Card on Desktop
                        Surface(
                            color = SoftLavender,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("profile") }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(RoyalBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = uiState.profile.name.take(1).ifBlank { "S" },
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = uiState.profile.name.ifBlank { "Demo Student" },
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy
                                        )
                                    )
                                    Text(
                                        text = uiState.profile.selectedCareer,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Purple,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Main Content Area
            Scaffold(
                modifier = Modifier.weight(1f),
                containerColor = BackgroundLight,
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            Brush.linearGradient(listOf(RoyalBlue, Purple))
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Explore,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "CareerPath AI",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy,
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = "Plan • Learn • Build • Grow",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextSecondary,
                                            fontSize = 10.5.sp
                                        )
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            if (!isTopLevelRoute) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = DeepNavy
                                    )
                                }
                            }
                        },
                        actions = {
                            // Notifications Icon
                            IconButton(onClick = { showNotificationsDialog = true }) {
                                Box {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = "Notifications",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(StatusOrange)
                                            .align(Alignment.TopEnd)
                                    )
                                }
                            }

                            // Profile / Avatar Icon
                            IconButton(onClick = { navController.navigate("profile") }) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SoftLavender)
                                        .border(1.5.dp, Purple.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile",
                                        tint = Purple,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = CardWhite,
                            titleContentColor = DeepNavy
                        )
                    )
                },
                bottomBar = {
                    // Fixed Clean Bottom Navigation on Mobile
                    if (!isDesktop) {
                        Surface(
                            color = CardWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            shadowElevation = 8.dp
                        ) {
                            NavigationBar(
                                containerColor = CardWhite,
                                tonalElevation = 0.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .height(64.dp)
                            ) {
                                navItems.forEach { item ->
                                    val isSelected = currentRoute == item.route
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) item.filledIcon else item.outlineIcon,
                                                contentDescription = item.label,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = item.label,
                                                fontSize = 10.5.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = RoyalBlue,
                                            selectedTextColor = RoyalBlue,
                                            unselectedIconColor = TextSecondary,
                                            unselectedTextColor = TextSecondary,
                                            indicatorColor = SoftBlue
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "dashboard",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    composable("dashboard") {
                        DashboardScreen(
                            state = uiState,
                            onNavigateTo = { route -> navController.navigate(route) },
                            onLoadDemo = { viewModel.loadDemoProfile() },
                            onCompleteTask = { id, comp -> viewModel.toggleTask(id, comp) }
                        )
                    }

                    composable("roadmap") {
                        RoadmapScreen(
                            state = uiState,
                            onAdaptRoadmap = { reason -> viewModel.adaptRoadmap(reason) },
                            onDismissMessage = { viewModel.clearAdaptationMessage() }
                        )
                    }

                    composable("skill_gap") {
                        SkillGapScreen(
                            state = uiState,
                            onNavigateToRoadmap = { navController.navigate("roadmap") }
                        )
                    }

                    composable("projects") {
                        ProjectsScreen(state = uiState)
                    }

                    composable("ai_mentor") {
                        AiMentorScreen(
                            state = uiState,
                            onSendMessage = { query -> viewModel.askMentor(query) }
                        )
                    }

                    composable("more_tools") {
                        MoreToolsScreen(
                            state = uiState,
                            onNavigateTo = { route -> navController.navigate(route) }
                        )
                    }

                    composable("weekly_plan") {
                        WeeklyPlanScreen(
                            state = uiState,
                            onToggleTask = { id, comp -> viewModel.toggleTask(id, comp) }
                        )
                    }

                    composable("career_assessment") {
                        CareerAssessmentScreen(
                            state = uiState,
                            onSelectCareer = { career ->
                                viewModel.selectTargetCareer(career)
                                navController.navigate("roadmap")
                            },
                            onNavigateToComparison = { navController.navigate("career_comparison") }
                        )
                    }

                    composable("career_comparison") {
                        CareerComparisonScreen(
                            state = uiState,
                            onSelectCareer = { career ->
                                viewModel.selectTargetCareer(career)
                                navController.navigate("roadmap")
                            }
                        )
                    }

                    composable("what_if") {
                        WhatIfSimulatorScreen(
                            state = uiState,
                            onRunSimulation = { h, m, p, i -> viewModel.runWhatIfSimulation(h, m, p, i) }
                        )
                    }

                    composable("resume") {
                        ResumeScreen(
                            state = uiState,
                            onAnalyzeResume = { text -> viewModel.analyzeResume(text) }
                        )
                    }

                    composable("interview") {
                        InterviewScreen(
                            state = uiState,
                            onEvaluateAnswer = { c, q, a -> viewModel.evaluateInterview(c, q, a) }
                        )
                    }

                    composable("mentors") {
                        MentorsScreen(state = uiState)
                    }

                    composable("profile") {
                        ProfileScreen(
                            state = uiState,
                            onSaveProfile = { prof ->
                                viewModel.saveProfile(prof)
                                navController.popBackStack()
                            },
                            onLoadDemo = { viewModel.loadDemoProfile() },
                            onReassess = { navController.navigate("career_assessment") }
                        )
                    }
                }
            }
        }
    }

    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔔 Notifications", fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = SoftBlue,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Daily Goal Reminder",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DeepNavy
                            )
                            Text(
                                text = "Complete SQL JOIN practice to keep your 5-day streak active!",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Surface(
                        color = SoftGreen,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Roadmap Milestone",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DeepNavy
                            )
                            Text(
                                text = "Month 1 SQL Foundations is 40% complete. Keep it up!",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showNotificationsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Close")
                }
            }
        )
    }
}
