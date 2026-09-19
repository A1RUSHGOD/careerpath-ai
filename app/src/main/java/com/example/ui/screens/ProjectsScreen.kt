package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RecommendedProject
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.*

@Composable
fun ProjectsScreen(
    state: CareerPathUiState
) {
    val projects = state.recommendedProjects
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedProjectForDialog by remember { mutableStateOf<RecommendedProject?>(null) }

    val filteredProjects = when (selectedFilter) {
        "Intermediate" -> projects.filter { it.estimatedWeeks <= 3 }
        "Advanced" -> projects.filter { it.estimatedWeeks > 3 }
        else -> projects
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Recommended Projects",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Real-world problems solving actual business scenarios. No generic todo lists, calculators, or Iris/Titanic datasets.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Filters
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All", "Intermediate", "Advanced").forEach { filter ->
                            val isSelected = selectedFilter == filter
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedFilter = filter },
                                label = {
                                    Text(
                                        text = filter,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RoyalBlue,
                                    selectedLabelColor = Color.White,
                                    containerColor = SoftBlue,
                                    labelColor = DeepNavy
                                ),
                                border = null,
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Project Cards
        items(filteredProjects) { project ->
            ProjectCardItem(
                project = project,
                onViewDetails = { selectedProjectForDialog = project }
            )
        }

        item {
            DisclaimerBanner(
                text = "Projects should be deployed with public GitHub repositories, interactive dashboards/demos, and an executive summary deck to ensure maximum recruiter conversion."
            )
        }
    }

    // Project Details Dialog
    if (selectedProjectForDialog != null) {
        val proj = selectedProjectForDialog!!
        AlertDialog(
            onDismissRequest = { selectedProjectForDialog = null },
            title = {
                Text(
                    text = proj.title,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = SoftBlue,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Industry Problem", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = RoyalBlue)
                            Text(proj.problemStatement, fontSize = 12.sp, color = TextMain)
                        }
                    }

                    Surface(
                        color = SoftLavender,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Dataset & APIs", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Purple)
                            Text(proj.datasetApiRequirements, fontSize = 12.sp, color = TextMain)
                        }
                    }

                    Surface(
                        color = SoftGreen,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Deliverables", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = StatusGreen)
                            Text(proj.expectedDeliverables.joinToString("\n• ", prefix = "• "), fontSize = 11.5.sp, color = TextMain)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedProjectForDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Got It")
                }
            }
        )
    }
}

@Composable
fun ProjectCardItem(
    project: RecommendedProject,
    onViewDetails: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        shape = RoundedCornerShape(20.dp),
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
                Surface(
                    color = SoftBlue,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = project.domain.uppercase(),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    color = SoftGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${project.estimatedWeeks} Weeks • ${project.difficulty}",
                        color = StatusGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy,
                    fontSize = 16.5.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = project.problemStatement,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    lineHeight = 19.sp,
                    fontSize = 13.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Skills tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (skill in project.skillsDeveloped.take(4)) {
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

            Spacer(modifier = Modifier.height(14.dp))

            // Interview talking points
            val talkingPoint = project.interviewTalkingPoints.firstOrNull()
            if (talkingPoint != null) {
                Surface(
                    color = BackgroundLight,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💼 Interview Talking Point:",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = talkingPoint,
                            fontSize = 11.5.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            Button(
                onClick = onViewDetails,
                colors = ButtonDefaults.buttonColors(containerColor = SoftBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text(
                    text = "View Project Details →",
                    color = RoyalBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
