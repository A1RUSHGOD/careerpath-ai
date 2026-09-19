package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeeklyTask
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.*

@Composable
fun WeeklyPlanScreen(
    state: CareerPathUiState,
    onToggleTask: (Long, Boolean) -> Unit
) {
    val tasks = state.tasks
    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size
    val progressPct = if (totalCount > 0) (completedCount * 100) / totalCount else 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        text = "Weekly Execution Plan",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "High-impact daily tasks calibrated to ${state.profile.hoursPerDay} hours/day. Track completion every day to build momentum.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }
            }
        }

        // Progress Overview Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftLavender),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Purple.copy(alpha = 0.25f)),
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
                                text = "WEEK 1 EXECUTION SPRINT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Purple,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$completedCount of $totalCount Tasks Done",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }
                        Surface(
                            color = CardWhite,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Purple.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "$progressPct%",
                                color = Purple,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = { (progressPct / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Purple,
                        trackColor = CardWhite
                    )
                }
            }
        }

        // Tasks list
        items(tasks) { task ->
            WeeklyTaskItem(task = task, onToggle = { onToggleTask(task.id, it) })
        }

        item {
            DisclaimerBanner(
                text = "Tasks focus strictly on high-yield interview requirements. Checking off items updates your overall career readiness."
            )
        }
    }
}

@Composable
fun WeeklyTaskItem(task: WeeklyTask, onToggle: (Boolean) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (task.isCompleted) StatusGreen.copy(alpha = 0.3f) else BorderSubtle
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onToggle,
                colors = CheckboxDefaults.colors(
                    checkedColor = StatusGreen,
                    uncheckedColor = TextSecondary,
                    checkmarkColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = SoftBlue,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Day ${task.dayOfWeek}",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        color = SoftOrange,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${task.estimatedMinutes} mins",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusOrange,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = task.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (task.isCompleted) TextSecondary else TextMain,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = task.description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
