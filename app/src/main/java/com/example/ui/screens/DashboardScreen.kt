package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.CareerPathUiState
import com.example.ui.components.CircularSkillCard
import com.example.ui.components.DisclaimerBanner
import com.example.ui.components.SummaryStatCard
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    state: CareerPathUiState,
    onNavigateTo: (String) -> Unit,
    onLoadDemo: () -> Unit,
    onCompleteTask: (Long, Boolean) -> Unit
) {
    val totalTasks = state.tasks.size
    val completedTasks = state.tasks.count { it.isCompleted }
    val week1Tasks = state.tasks.filter { it.weekNumber == 1 }
    val week1Completed = week1Tasks.count { it.isCompleted }
    val week1Total = if (week1Tasks.isNotEmpty()) week1Tasks.size else 6
    val weeklyPercentage = if (week1Tasks.isNotEmpty()) (week1Completed * 100) / week1Tasks.size else 0

    val topRecommendation = state.recommendations.find { it.name == state.profile.selectedCareer }
        ?: state.recommendations.firstOrNull()
    val fitScore = topRecommendation?.fitScore ?: 52

    val nextTask = state.tasks.firstOrNull { !it.isCompleted }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Large Hero Card with subtle blue/lavender gradient & career journey illustration
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFFDCE6FB),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFEDF4FF),
                                    Color(0xFFF4EFFF),
                                    Color(0xFFF9F6FF)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp)
                        ) {
                            Text(
                                text = "Good Morning,",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${state.profile.name.ifBlank { "Demo Student" }} 👋",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 25.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your journey to a better career starts with the right guidance.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondary,
                                    lineHeight = 20.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Career Goal Badge with Change Goal
                            Surface(
                                color = CardWhite,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F5)),
                                shadowElevation = 1.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clickable { onNavigateTo("career_assessment") }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Career Goal",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        )
                                        Text(
                                            text = "🎯 ${state.profile.selectedCareer}",
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                color = DeepNavy,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Surface(
                                        color = SoftBlue,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "Change",
                                                color = RoyalBlue,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                                contentDescription = null,
                                                tint = RoyalBlue,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Subtle Career Journey Illustration
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(CardWhite)
                                .border(1.dp, Color(0xFFE2E8F5), RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_career_journey),
                                contentDescription = "Career Journey Illustration",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        // 2. Four Summary Cards (Fit, Readiness, Streak, Weekly Plan)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryStatCard(
                        title = "AI Estimated Fit",
                        value = "$fitScore/100",
                        supportingText = "Based on your profile",
                        statusText = "Good potential",
                        icon = Icons.Default.AutoAwesome,
                        containerColor = SoftGreen,
                        accentColor = StatusGreen,
                        onClick = { onNavigateTo("career_assessment") },
                        modifier = Modifier.weight(1f)
                    )

                    SummaryStatCard(
                        title = "Career Readiness",
                        value = "61%",
                        supportingText = "Milestone score",
                        statusText = "On track",
                        icon = Icons.Default.TrendingUp,
                        containerColor = SoftBlue,
                        accentColor = RoyalBlue,
                        onClick = { onNavigateTo("roadmap") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryStatCard(
                        title = "Current Streak",
                        value = "5 Days 🔥",
                        supportingText = "Consistency reward",
                        statusText = "Keep it up!",
                        icon = Icons.Default.Whatshot,
                        containerColor = SoftOrange,
                        accentColor = StatusOrange,
                        onClick = { onNavigateTo("weekly_plan") },
                        modifier = Modifier.weight(1f)
                    )

                    SummaryStatCard(
                        title = "Weekly Plan",
                        value = "$weeklyPercentage%",
                        supportingText = "$week1Completed of $week1Total tasks done",
                        statusText = "Start today →",
                        icon = Icons.Default.Checklist,
                        containerColor = SoftLavender,
                        accentColor = Purple,
                        onClick = { onNavigateTo("weekly_plan") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Wide Blue/Purple Career Goal Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    DeepNavy,
                                    Color(0xFF1E3A8A),
                                    Purple
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = Color.White.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "YOUR CAREER GOAL",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp,
                                            fontSize = 10.5.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = state.profile.selectedCareer,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "\"Turning data into insights. That's the goal.\"",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Destination Targets (Target Role, Target Salary, Target Timeline)
                        Surface(
                            color = Color.White.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text(
                                        text = "Target Role",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                    Text(
                                        text = state.profile.selectedCareer,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Target Salary",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                    Text(
                                        text = "₹6–8 LPA",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Target Timeline",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                    Text(
                                        text = "5–7 Months",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onNavigateTo("career_assessment") },
                            colors = ButtonDefaults.buttonColors(containerColor = CardWhite),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "View Details →",
                                color = DeepNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // 4. Highly Visible Section: 💡 Next Action
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalBlue.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "💡",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Next Action",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )
                        }

                        Surface(
                            color = Color(0xFFFFECEB),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "HIGH PRIORITY",
                                color = StatusRed,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = nextTask?.title ?: "Complete SQL JOIN practice",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "This will help you improve your SQL skills (high priority). Covers INNER, LEFT, and self-joins on realistic orders/customers schemas.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = SoftBlue,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "⏱️ ${nextTask?.estimatedMinutes ?: 45} mins",
                                    fontSize = 12.sp,
                                    color = RoyalBlue,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                color = SoftLavender,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "📊 SQL Practice",
                                    fontSize = 12.sp,
                                    color = Purple,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Button(
                            onClick = { onNavigateTo("weekly_plan") },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Start Now →",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }
            }
        }

        // 5. Skill Progress Card with Circular Progress Indicators
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Your Skill Progress",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )
                            Text(
                                text = "Target: Data Analyst requirements",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        TextButton(onClick = { onNavigateTo("skill_gap") }) {
                            Text(
                                text = "View All →",
                                color = RoyalBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Circular Progress Indicators in a clean horizontal scroll row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularSkillCard(
                            skillName = "Python",
                            percentage = (state.profile.python * 10).coerceIn(10, 100),
                            statusBadge = "Strong",
                            accentColor = RoyalBlue,
                            modifier = Modifier.width(115.dp)
                        )

                        CircularSkillCard(
                            skillName = "SQL",
                            percentage = (state.profile.sql * 10).coerceIn(10, 100),
                            statusBadge = "Needs Work",
                            accentColor = StatusRed,
                            modifier = Modifier.width(115.dp)
                        )

                        CircularSkillCard(
                            skillName = "Power BI",
                            percentage = (state.profile.powerBi * 10).coerceIn(10, 100),
                            statusBadge = "Improving",
                            accentColor = StatusOrange,
                            modifier = Modifier.width(115.dp)
                        )

                        CircularSkillCard(
                            skillName = "Statistics",
                            percentage = (state.profile.statistics * 10).coerceIn(10, 100),
                            statusBadge = "Needs Work",
                            accentColor = Purple,
                            modifier = Modifier.width(115.dp)
                        )

                        CircularSkillCard(
                            skillName = "Machine Learning",
                            percentage = (state.profile.machineLearning * 10).coerceIn(10, 100),
                            statusBadge = "Beginner",
                            accentColor = TextSecondary,
                            modifier = Modifier.width(115.dp)
                        )
                    }
                }
            }
        }

        // Demo Profile Re-loader button
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SoftLavender.copy(alpha = 0.7f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Purple.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "⚡", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Sample Student Profile",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DeepNavy
                            )
                            Text(
                                text = "Loaded: 3rd Year Comp Engg student (PICT Pune)",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onLoadDemo,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Purple),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Purple),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Reset Demo", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Disclaimer Banner
        item {
            DisclaimerBanner(
                text = "CareerPath AI calculates fit scores and recommendations as planning benchmarks. Progress depends on consistent daily execution."
            )
        }
    }
}
