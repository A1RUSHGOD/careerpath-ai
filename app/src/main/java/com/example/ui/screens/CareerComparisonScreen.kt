package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun CareerComparisonScreen(
    state: CareerPathUiState,
    onSelectCareer: (String) -> Unit
) {
    val careers = state.recommendations

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        text = "Career Paths Comparison Matrix",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Compare trade-offs, preparation timelines, and primary bottlenecks across 8 major Indian tech pathways.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }
            }
        }

        items(careers) { career ->
            val isCurrent = career.name == state.profile.selectedCareer

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectCareer(career.name) },
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 2.dp else 1.dp),
                border = androidx.compose.foundation.BorderStroke(
                    if (isCurrent) 2.dp else 1.dp,
                    if (isCurrent) RoyalBlue else BorderSubtle
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = career.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )
                            if (isCurrent) {
                                Surface(
                                    color = SoftGreen,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE GOAL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StatusGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        FitScoreBadge(score = career.fitScore)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundLight, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Prep Duration", fontSize = 11.sp, color = TextSecondary)
                            Text(career.typicalPrepDuration, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        }
                        Column {
                            Text("Difficulty", fontSize = 11.sp, color = TextSecondary)
                            Text(career.difficultyLevel, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
                        }
                        Column {
                            Text("Priority", fontSize = 11.sp, color = TextSecondary)
                            Text(career.suggestedPriority, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = StatusGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (career.majorGaps.isNotEmpty()) {
                        Text(
                            text = "Major Gaps: ${career.majorGaps.joinToString(", ")}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Button(
                        onClick = { onSelectCareer(career.name) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCurrent) SoftGreen else SoftBlue
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isCurrent) "Currently Selected" else "Switch Active Goal →",
                            color = if (isCurrent) StatusGreen else RoyalBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            DisclaimerBanner(
                text = "Switching career goal instantly updates your active roadmap, prioritized skill gap indicators, and recommended projects."
            )
        }
    }
}
