package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
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
fun ResumeScreen(
    state: CareerPathUiState,
    onAnalyzeResume: (String) -> Unit
) {
    val sampleResume = """
        Rahul Sharma
        B.Tech Computer Engineering, 2025 | CGPA: 7.8
        Pune Institute of Computer Technology
        
        TECHNICAL SKILLS:
        Languages: Python, C++, SQL (Basics), HTML/CSS
        Tools: Power BI, Excel, Git, VS Code
        Coursework: Data Structures, Database Systems, Computer Networks
        
        PROJECTS:
        1. E-Commerce Order Analysis
        - Analyzed customer purchase transactions using Python and Pandas.
        - Made charts using Matplotlib and Seaborn.
        - Responsible for writing SQL queries to extract data.
        
        2. Student Attendance Management System
        - Built a web application using HTML, CSS, and JavaScript.
        - Stored student records in a MySQL database.
        
        EXPERIENCE:
        College Technical Club Member - Organized coding workshop for 120 juniors.
    """.trimIndent()

    var resumeText by remember { mutableStateOf(sampleResume) }
    val feedback = state.resumeFeedback

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
                        text = "ATS Resume Analyzer",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Target Role: ${state.profile.selectedCareer}",
                        style = MaterialTheme.typography.titleSmall.copy(color = RoyalBlue, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Scan your resume against automated ATS filters used by Indian recruiters. Detect missing keywords, weak bullets, and get action-driven rewrites.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }
            }
        }

        // Resume Input Field
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
                        text = "Paste Resume Content / Project Section",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = resumeText,
                        onValueChange = { resumeText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBlue,
                            unfocusedBorderColor = BorderSubtle,
                            focusedContainerColor = BackgroundLight,
                            unfocusedContainerColor = BackgroundLight
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (resumeText.isNotBlank()) {
                                onAnalyzeResume(resumeText)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        enabled = !state.isAnalyzingResume && resumeText.isNotBlank()
                    ) {
                        if (state.isAnalyzingResume) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing ATS Compliance...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyze Resume with AI", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Analysis Results Card
        if (feedback != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalBlue.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ATS Screening Score",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )

                            Surface(
                                color = if (feedback.readinessScore >= 70) SoftGreen else SoftOrange,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "${feedback.readinessScore}/100",
                                    color = if (feedback.readinessScore >= 70) StatusGreen else StatusOrange,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Missing Keywords
                        Surface(
                            color = SoftOrange,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "⚠️ Critical Missing Keywords",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = StatusOrange
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    feedback.missingKeywords.take(4).forEach { kw ->
                                        Surface(
                                            color = CardWhite,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "+ $kw",
                                                color = StatusOrange,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Suggested Fixes
                        if (feedback.suggestedFixes.isNotEmpty()) {
                            Surface(
                                color = SoftLavender,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "🎯 AI Bullet Rewrite (Google XYZ Formula)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Purple
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    feedback.suggestedFixes.take(2).forEach { fix ->
                                        Text(
                                            text = "• $fix",
                                            fontSize = 12.sp,
                                            color = TextMain,
                                            lineHeight = 17.sp,
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Summary
                        Surface(
                            color = SoftBlue,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "📋 ATS Summary",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = RoyalBlue
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = feedback.summary,
                                    fontSize = 12.sp,
                                    color = TextMain,
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
                text = "Resume scoring evaluates structural metrics, keyword density, and quantifiable impact formulas."
            )
        }
    }
}
