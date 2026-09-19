package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.*

@Composable
fun WhatIfSimulatorScreen(
    state: CareerPathUiState,
    onRunSimulation: (hours: Int, months: Int, projects: Int, internship: Boolean) -> Unit
) {
    var hoursPerDay by remember { mutableFloatStateOf(state.profile.hoursPerDay.toFloat()) }
    var durationMonths by remember { mutableFloatStateOf(6f) }
    var projectsCount by remember { mutableFloatStateOf(state.profile.completedProjects.toFloat()) }
    var hasInternship by remember { mutableStateOf(state.profile.internships > 0) }

    LaunchedEffect(hoursPerDay, durationMonths, projectsCount, hasInternship) {
        onRunSimulation(
            hoursPerDay.toInt(),
            durationMonths.toInt(),
            projectsCount.toInt(),
            hasInternship
        )
    }

    val scenario = state.whatIfScenario

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        text = "What-If Career Simulator",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Target: ${state.profile.selectedCareer}",
                        style = MaterialTheme.typography.titleSmall.copy(color = RoyalBlue, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Experiment with your daily bandwidth, timeline, and portfolio inputs to see estimated readiness based on technical milestones.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }
            }
        }

        // Sliders Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "SIMULATION INPUTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Study Hours Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Daily Preparation Hours", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        Text("${hoursPerDay.toInt()} hrs/day", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
                    }
                    Slider(
                        value = hoursPerDay,
                        onValueChange = { hoursPerDay = it },
                        valueRange = 1f..8f,
                        steps = 6,
                        colors = SliderDefaults.colors(thumbColor = RoyalBlue, activeTrackColor = RoyalBlue, inactiveTrackColor = SoftBlue)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Duration Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Target Timeline to Placement", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        Text("${durationMonths.toInt()} Months", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Purple)
                    }
                    Slider(
                        value = durationMonths,
                        onValueChange = { durationMonths = it },
                        valueRange = 2f..12f,
                        steps = 9,
                        colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple, inactiveTrackColor = SoftLavender)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Projects Count Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Completed Portfolio Projects", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        Text("${projectsCount.toInt()} Projects", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StatusGreen)
                    }
                    Slider(
                        value = projectsCount,
                        onValueChange = { projectsCount = it },
                        valueRange = 0f..5f,
                        steps = 4,
                        colors = SliderDefaults.colors(thumbColor = StatusGreen, activeTrackColor = StatusGreen, inactiveTrackColor = SoftGreen)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Internship Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Has Completed Prior Internship?", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DeepNavy)
                            Text("+15% boost to resume screening conversion", fontSize = 11.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = hasInternship,
                            onCheckedChange = { hasInternship = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RoyalBlue)
                        )
                    }
                }
            }
        }

        // Projected Readiness Outcome Card
        if (scenario != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SoftBlue),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.3f)),
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
                                    text = "PROJECTED OUTCOME",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Readiness Probability",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            }
                            Surface(
                                color = CardWhite,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "${scenario.estimatedReadinessScore}%",
                                    color = RoyalBlue,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = CardWhite,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Analysis & Feasibility:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = DeepNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = scenario.analysisNotes,
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            DisclaimerBanner(
                text = "Simulation calculations are statistical estimates and do not guarantee placement outcomes."
            )
        }
    }
}
