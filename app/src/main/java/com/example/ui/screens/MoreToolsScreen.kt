package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.*

@Composable
fun MoreToolsScreen(
    state: CareerPathUiState,
    onNavigateTo: (String) -> Unit
) {
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
                        text = "More Tools & Services",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Specialized AI and career planning modules designed for Indian engineering students.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }
            }
        }

        item {
            ToolNavigationCard(
                title = "AI Career Assessment",
                subtitle = "Evaluate profile fit across Data, SDE, AI, Cyber, and Cloud",
                icon = Icons.Default.AutoAwesome,
                iconTint = RoyalBlue,
                iconBg = SoftBlue,
                route = "career_assessment",
                onNavigate = onNavigateTo
            )
        }

        item {
            ToolNavigationCard(
                title = "Compare All 8 Career Paths",
                subtitle = "Side-by-side benchmark across salaries, timelines, difficulty & Indian market",
                icon = Icons.Default.CompareArrows,
                iconTint = Purple,
                iconBg = SoftLavender,
                route = "career_comparison",
                onNavigate = onNavigateTo
            )
        }

        item {
            ToolNavigationCard(
                title = "ATS Resume Analyzer",
                subtitle = "Scan resume against ATS algorithms, detect keyword gaps & rewrite bullets",
                icon = Icons.Default.Description,
                iconTint = StatusGreen,
                iconBg = SoftGreen,
                route = "resume",
                onNavigate = onNavigateTo
            )
        }

        item {
            ToolNavigationCard(
                title = "AI Interview Practice",
                subtitle = "Timed technical, HR, and behavioral questions with instant AI scoring",
                icon = Icons.Default.RecordVoiceOver,
                iconTint = StatusOrange,
                iconBg = SoftOrange,
                route = "interview",
                onNavigate = onNavigateTo
            )
        }

        item {
            ToolNavigationCard(
                title = "What-If Career Simulator",
                subtitle = "Simulate how changing daily study hours and internships impacts readiness",
                icon = Icons.Default.Tune,
                iconTint = RoyalBlue,
                iconBg = SoftBlue,
                route = "what_if",
                onNavigate = onNavigateTo
            )
        }

        item {
            ToolNavigationCard(
                title = "Verified Industry Mentors",
                subtitle = "Connect with engineers and alumni working at top Indian tech firms",
                icon = Icons.Default.People,
                iconTint = Purple,
                iconBg = SoftLavender,
                route = "mentors",
                onNavigate = onNavigateTo
            )
        }

        item {
            ToolNavigationCard(
                title = "Student Profile & Target Settings",
                subtitle = "Update branch, CGPA, target salary, and current skill levels",
                icon = Icons.Default.Person,
                iconTint = DeepNavy,
                iconBg = Color(0xFFECEEF5),
                route = "profile",
                onNavigate = onNavigateTo
            )
        }

        item {
            DisclaimerBanner(
                text = "All modules operate on your verified profile and sync with your current career goal."
            )
        }
    }
}

@Composable
fun ToolNavigationCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    route: String,
    onNavigate: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(route) },
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
