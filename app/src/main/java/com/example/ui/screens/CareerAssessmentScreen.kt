package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CareerRecommendation
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.components.FitScoreBadge
import com.example.ui.theme.*

@Composable
fun CareerAssessmentScreen(
    state: CareerPathUiState,
    onSelectCareer: (String) -> Unit,
    onNavigateToComparison: () -> Unit
) {
    val top3 = state.recommendations.take(3)
    val profile = state.profile

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary Card
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PROFILE ASSESSMENT SUMMARY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue,
                                letterSpacing = 1.sp
                            )
                        )
                        Surface(
                            color = SoftGreen,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Analysis Complete",
                                color = StatusGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Your profile shows strongest leverage in Python (${profile.python}/10) and structured analytical problem-solving, but your SQL (${profile.sql}/10), lack of verified projects (${profile.completedProjects}/3), and industry exposure currently limit entry-level conversion.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 21.sp,
                            color = TextMain
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = SoftBlue,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "College: ${profile.college}",
                                fontSize = 11.sp,
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
                                text = "Bandwidth: ${profile.hoursPerDay} hrs/day",
                                fontSize = 11.sp,
                                color = Purple,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section Title & Compare Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Top 3 Career Matches",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Text(
                        text = "Ranked by transition feasibility and skill overlap",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
                OutlinedButton(
                    onClick = onNavigateToComparison,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RoyalBlue),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.4f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("Compare All 8", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Top 3 Career Recommendation Cards
        itemsIndexed(top3) { index, recommendation ->
            CareerMatchCard(
                rank = index + 1,
                recommendation = recommendation,
                isSelected = recommendation.name == profile.selectedCareer,
                onSelect = { onSelectCareer(recommendation.name) }
            )
        }

        item {
            DisclaimerBanner(
                text = "Fit calculations compare your verified skills, college year, and study hours against real hiring criteria at 50+ Indian tech employers. Choosing a path generates your customized milestone roadmap."
            )
        }
    }
}

@Composable
fun CareerMatchCard(
    rank: Int,
    recommendation: CareerRecommendation,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) RoyalBlue else BorderSubtle
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Rank + Title + Fit Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (rank == 1) RoyalBlue else SoftBlue,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "#$rank",
                            color = if (rank == 1) Color.White else RoyalBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = recommendation.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        )
                        Text(
                            text = "${recommendation.typicalPrepDuration} to readiness",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }

                FitScoreBadge(score = recommendation.fitScore)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metrics Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight, RoundedCornerShape(12.dp))
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Prep Duration", fontSize = 11.sp, color = TextSecondary)
                    Text(recommendation.typicalPrepDuration, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
                Column {
                    Text("Difficulty", fontSize = 11.sp, color = TextSecondary)
                    Text(recommendation.difficultyLevel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
                }
                Column {
                    Text("Priority", fontSize = 11.sp, color = TextSecondary)
                    Text(recommendation.suggestedPriority, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StatusGreen)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Why This Fits
            Text(
                text = "Why This Fits You:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = recommendation.whyItFits,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Major Gaps
            if (recommendation.majorGaps.isNotEmpty()) {
                Surface(
                    color = SoftOrange,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚠️ ", fontSize = 12.sp)
                        Text(
                            text = "Major Gap: ${recommendation.majorGaps.joinToString(", ")}",
                            fontSize = 11.5.sp,
                            color = StatusOrange,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Button
            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) StatusGreen else RoyalBlue
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSelected) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Active Goal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                } else {
                    Text("Set as Active Goal & Generate Roadmap →", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
