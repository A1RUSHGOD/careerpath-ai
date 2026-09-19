package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.*

data class RoadmapMilestone(
    val month: Int,
    val title: String,
    val status: MilestoneStatus,
    val completionPct: Int,
    val skills: List<String>,
    val tasks: List<String>,
    val estimatedHours: Int,
    val deliverable: String
)

enum class MilestoneStatus {
    COMPLETED, CURRENT, UPCOMING
}

@Composable
fun RoadmapScreen(
    state: CareerPathUiState,
    onAdaptRoadmap: (String) -> Unit,
    onDismissMessage: () -> Unit
) {
    var showAdaptDialog by remember { mutableStateOf(false) }

    val milestones = listOf(
        RoadmapMilestone(
            month = 1,
            title = "SQL Foundations & Query Mastery",
            status = MilestoneStatus.CURRENT,
            completionPct = 40,
            skills = listOf("SQL Joins", "Aggregations", "CTEs", "PostgreSQL", "Window Functions"),
            tasks = listOf("Complete 40+ SQL practice challenges", "Master 3-table joins with nested aggregation", "Build relational e-commerce query suite"),
            estimatedHours = 52,
            deliverable = "Completed query script + 40 LeetCode/HackerRank SQL solutions"
        ),
        RoadmapMilestone(
            month = 2,
            title = "Power BI & Business Metric Dashboards",
            status = MilestoneStatus.UPCOMING,
            completionPct = 0,
            skills = listOf("Power BI", "DAX Formulas", "Data Modeling", "Excel Power Query"),
            tasks = listOf("Create dynamic DAX calculation measures", "Build drill-through sales & customer retention reports", "Design executive dashboard layout"),
            estimatedHours = 48,
            deliverable = "Published interactive sales & marketing performance dashboard"
        ),
        RoadmapMilestone(
            month = 3,
            title = "Real-world Indian Industry Capstone Project",
            status = MilestoneStatus.UPCOMING,
            completionPct = 0,
            skills = listOf("Python (Pandas, Seaborn)", "SQL Extraction", "Business Analysis"),
            tasks = listOf("Scrape/import real quick-commerce logistics data", "Identify customer churn and unit economics bottlenecks", "Build automated summary dashboard"),
            estimatedHours = 56,
            deliverable = "GitHub repo + clean Jupyter pipeline + 8-slide executive PDF deck"
        ),
        RoadmapMilestone(
            month = 4,
            title = "Portfolio & GitHub Presence",
            status = MilestoneStatus.UPCOMING,
            completionPct = 0,
            skills = listOf("Git/GitHub", "Streamlit/Voila", "Technical Writing", "Personal Branding"),
            tasks = listOf("Document architecture and business ROI in README", "Deploy Streamlit interactive exploration demo", "Publish 2 technical breakdown posts on LinkedIn"),
            estimatedHours = 40,
            deliverable = "Live portfolio site with clickable project walkthroughs"
        ),
        RoadmapMilestone(
            month = 5,
            title = "ATS Resume Calibration + Targeted Applications",
            status = MilestoneStatus.UPCOMING,
            completionPct = 0,
            skills = listOf("Resume Optimization", "Cold Outreach", "Networking", "LinkedIn InMail"),
            tasks = listOf("Format single-column quantified bullet points", "Track 30 tailored job applications with employee referrals", "Optimize ATS keyword match score > 80%"),
            estimatedHours = 38,
            deliverable = "Calibrated resume + 30 tracked company referral applications"
        ),
        RoadmapMilestone(
            month = 6,
            title = "Technical Screens & Interview Preparation",
            status = MilestoneStatus.UPCOMING,
            completionPct = 0,
            skills = listOf("Live SQL Coding", "Market Sizing Guesstimates", "Behavioral STAR", "Negotiation"),
            tasks = listOf("Conduct 5 timed mock technical whiteboards", "Master product intuition & behavioral STAR frameworks", "Prepare offer evaluation & salary negotiation"),
            estimatedHours = 45,
            deliverable = "5 completed full-length mock interview recordings with feedback"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Your Career Roadmap",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = state.profile.selectedCareer,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = RoyalBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = " • 5–7 Month Journey",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = { showAdaptDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Adapt Path", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "A living, adaptive milestone curriculum built around your academic bandwidth. Milestones automatically adjust when your pace or circumstances change.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Adaptation Notification Banner
        if (state.adaptationMessage != null) {
            item {
                Surface(
                    color = SoftBlue,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "✨ Dynamic Adaptation Active",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = state.adaptationMessage,
                                fontSize = 12.sp,
                                color = TextMain
                            )
                        }
                        TextButton(onClick = onDismissMessage) {
                            Text("Dismiss", color = RoyalBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Timeline Milestones
        items(milestones.size) { index ->
            val milestone = milestones[index]
            val isLast = index == milestones.size - 1

            TimelineMilestoneRow(
                milestone = milestone,
                isLast = isLast
            )
        }

        item {
            DisclaimerBanner(
                text = "Milestones represent recommended pedagogical sequences. Estimated preparation duration will automatically adapt as you log task completions or modify study hours."
            )
        }
    }

    // Adaptive Roadmap Dialog
    if (showAdaptDialog) {
        AlertDialog(
            onDismissRequest = { showAdaptDialog = false },
            title = {
                Text(
                    text = "Adapt Roadmap Curriculum",
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Select an event to recalibrate your timeline and priorities:",
                        fontSize = 12.5.sp,
                        color = TextSecondary
                    )

                    Button(
                        onClick = {
                            onAdaptRoadmap("Struggling with SQL queries - need remedial practice")
                            showAdaptDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SoftBlue),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Struggling with SQL (Add Remedial Drills)", color = RoyalBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            onAdaptRoadmap("Fast tracking - cleared SQL basics ahead of time")
                            showAdaptDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SoftGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Fast-Track (Advance to Capstone Early)", color = StatusGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            onAdaptRoadmap("College semester exams approaching - reducing hours")
                            showAdaptDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SoftOrange),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Exam Season (Scale to 1 hr/day)", color = StatusOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            onAdaptRoadmap("Secured an internship - pivot to workplace execution")
                            showAdaptDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SoftLavender),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Got an Internship! (Pivot to Work Skills)", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAdaptDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun TimelineMilestoneRow(
    milestone: RoadmapMilestone,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Timeline Indicator Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            // Circle Node
            val (nodeColor, iconTint, isCurrent) = when (milestone.status) {
                MilestoneStatus.COMPLETED -> Triple(StatusGreen, Color.White, false)
                MilestoneStatus.CURRENT -> Triple(RoyalBlue, Color.White, true)
                MilestoneStatus.UPCOMING -> Triple(Color(0xFFE2E8F0), TextSecondary, false)
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(nodeColor)
                    .border(
                        width = if (isCurrent) 3.dp else 0.dp,
                        color = if (isCurrent) SoftBlue else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                when (milestone.status) {
                    MilestoneStatus.COMPLETED -> Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                    MilestoneStatus.CURRENT -> Text(
                        text = "${milestone.month}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    MilestoneStatus.UPCOMING -> Text(
                        text = "${milestone.month}",
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Connecting line
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.5.dp)
                        .height(230.dp)
                        .background(if (milestone.status == MilestoneStatus.COMPLETED) StatusGreen else Color(0xFFE2E8F0))
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Milestone Content Card
        Card(
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = if (milestone.status == MilestoneStatus.CURRENT) 2.dp else 1.dp),
            border = androidx.compose.foundation.BorderStroke(
                width = if (milestone.status == MilestoneStatus.CURRENT) 1.5.dp else 1.dp,
                color = if (milestone.status == MilestoneStatus.CURRENT) RoyalBlue else BorderSubtle
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (isLast) 0.dp else 16.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MONTH ${milestone.month}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (milestone.status == MilestoneStatus.CURRENT) RoyalBlue else TextSecondary,
                            letterSpacing = 1.sp
                        )
                    )

                    // Status Badge
                    when (milestone.status) {
                        MilestoneStatus.COMPLETED -> {
                            Surface(color = SoftGreen, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "Completed",
                                    color = StatusGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        MilestoneStatus.CURRENT -> {
                            Surface(color = SoftBlue, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "In Progress • ${milestone.completionPct}%",
                                    color = RoyalBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        MilestoneStatus.UPCOMING -> {
                            Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "Upcoming",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = milestone.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Skills tags
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    milestone.skills.take(3).forEach { skill ->
                        Surface(
                            color = SoftLavender,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = skill,
                                color = Purple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tasks breakdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    milestone.tasks.forEach { task ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text("• ", color = RoyalBlue, fontWeight = FontWeight.Bold)
                            Text(
                                text = task,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMain,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Deliverable & Hours footer
                Surface(
                    color = SoftBlue.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📦 ${milestone.deliverable.take(38)}...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        )
                        Text(
                            text = "⏱️ ~${milestone.estimatedHours}h",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue
                        )
                    }
                }
            }
        }
    }
}
