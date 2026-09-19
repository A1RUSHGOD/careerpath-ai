package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GapPriority
import com.example.data.model.SkillGapItem
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.*

@Composable
fun SkillGapScreen(
    state: CareerPathUiState,
    onNavigateToRoadmap: () -> Unit
) {
    val gaps = state.skillGaps
    val criticalCount = gaps.count { it.priority == GapPriority.CRITICAL }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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
                        text = "Skill Gap Analysis",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Focus on the skills that will have the biggest impact on your target career.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = SoftBlue,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎯", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Target: ${state.profile.selectedCareer} • Screening Benchmark",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue
                                )
                            )
                        }
                    }
                }
            }
        }

        // Highlight Highest-Impact Priority Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8EE)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StatusOrange.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚡", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "High-Impact Focus Directive",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusOrange
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Do NOT learn everything at once. Focus 100% on SQL & Power BI first. That bridges 80% of actual hiring barriers.",
                            fontSize = 12.sp,
                            color = TextMain,
                            lineHeight = 17.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onNavigateToRoadmap,
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("Roadmap", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Quick Table Overview Card
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
                        text = "Summary Table",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SoftBlue.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Skill", fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = DeepNavy, modifier = Modifier.weight(1.3f))
                        Text("Current", fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = DeepNavy, modifier = Modifier.weight(1f))
                        Text("Target", fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = DeepNavy, modifier = Modifier.weight(1f))
                        Text("Gap", fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = DeepNavy, modifier = Modifier.weight(0.9f))
                        Text("Priority", fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = DeepNavy, modifier = Modifier.weight(1.2f))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    gaps.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.skillName, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = TextMain, modifier = Modifier.weight(1.3f))
                            Text("${item.currentLevel * 10}%", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.weight(1f))
                            Text("${item.targetLevel * 10}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RoyalBlue, modifier = Modifier.weight(1f))
                            Text("${item.gap * 10}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (item.gap > 0) StatusRed else StatusGreen, modifier = Modifier.weight(0.9f))
                            Box(modifier = Modifier.weight(1.2f)) {
                                PriorityBadge(priority = item.priority)
                            }
                        }
                        HorizontalDivider(color = BorderSubtle, thickness = 0.8.dp)
                    }
                }
            }
        }

        // Detailed Skill Cards with visual dual progress bars
        items(gaps) { gapItem ->
            DetailedSkillGapCard(item = gapItem)
        }

        item {
            DisclaimerBanner(
                text = "Target skill benchmarks are derived from analysis of job descriptions across Indian tech companies (MNCs, Unicorns, and high-growth startups) for entry-level hiring."
            )
        }
    }
}

@Composable
fun DetailedSkillGapCard(item: SkillGapItem) {
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
                Column {
                    Text(
                        text = item.skillName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Text(
                        text = "Category: ${item.category}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
                PriorityBadge(priority = item.priority)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Current vs Target Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current: ${item.currentLevel * 10}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain
                )
                Text(
                    text = "Required Target: ${item.targetLevel * 10}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Multi-segment Comparison Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFEEF2F6))
            ) {
                // Target Bar (Ghost)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (item.targetLevel / 10f).coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(RoyalBlue.copy(alpha = 0.25f))
                )

                // Current Filled Bar
                val currentBarColor = when (item.priority) {
                    GapPriority.CRITICAL -> StatusRed
                    GapPriority.IMPORTANT -> StatusOrange
                    GapPriority.LOW -> StatusGreen
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (item.currentLevel / 10f).coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(currentBarColor)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (item.gap > 0) "Gap: ${item.gap * 10}% deficit" else "Requirement Met ✓",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.gap > 0) StatusRed else StatusGreen
                )
                Text(
                    text = "Target Level: ${item.targetLevel}/10",
                    fontSize = 11.5.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = SoftBlue.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💡", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.impactReason,
                        fontSize = 11.5.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
