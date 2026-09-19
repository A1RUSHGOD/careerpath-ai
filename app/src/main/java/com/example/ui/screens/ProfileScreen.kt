package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    state: CareerPathUiState,
    onSaveProfile: (StudentProfile) -> Unit,
    onLoadDemo: () -> Unit,
    onReassess: () -> Unit = {}
) {
    var profile by remember(state.profile) { mutableStateOf(state.profile) }

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
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Student Profile",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Configures your AI assessment, skill gaps, and milestone schedule.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    OutlinedButton(
                        onClick = onLoadDemo,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Purple),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Purple.copy(alpha = 0.4f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Academic Details Section
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
                        text = "1. ACADEMIC DETAILS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = profile.name,
                        onValueChange = { profile = profile.copy(name = it) },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBlue,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = profile.college,
                        onValueChange = { profile = profile.copy(college = it) },
                        label = { Text("College Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBlue,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = profile.branch,
                            onValueChange = { profile = profile.copy(branch = it) },
                            label = { Text("Branch / Major") },
                            modifier = Modifier.weight(1.2f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalBlue,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )
                        OutlinedTextField(
                            value = profile.currentYear,
                            onValueChange = { profile = profile.copy(currentYear = it) },
                            label = { Text("Year") },
                            modifier = Modifier.weight(0.8f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalBlue,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = profile.cgpa.toString(),
                            onValueChange = { profile = profile.copy(cgpa = it.toDoubleOrNull() ?: profile.cgpa) },
                            label = { Text("CGPA (0–10)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalBlue,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )
                        OutlinedTextField(
                            value = profile.hoursPerDay.toString(),
                            onValueChange = { profile = profile.copy(hoursPerDay = it.toIntOrNull() ?: profile.hoursPerDay) },
                            label = { Text("Study Hrs/Day") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalBlue,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )
                    }
                }
            }
        }

        // Skills Self-Assessment Sliders
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
                        text = "2. SKILLS SELF-RATING (0–10)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Purple,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Rate honestly. These levels calibrate your skill gap analysis and remedial milestones.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    SkillSlider(name = "Python (Pandas, Scripting)", value = profile.python) { profile = profile.copy(python = it) }
                    SkillSlider(name = "SQL (Joins, Queries, CTEs)", value = profile.sql) { profile = profile.copy(sql = it) }
                    SkillSlider(name = "Power BI / Tableau (Visualization)", value = profile.powerBi) { profile = profile.copy(powerBi = it) }
                    SkillSlider(name = "Statistics & Probability", value = profile.statistics) { profile = profile.copy(statistics = it) }
                    SkillSlider(name = "Machine Learning (Scikit-Learn)", value = profile.machineLearning) { profile = profile.copy(machineLearning = it) }
                    SkillSlider(name = "Data Structures & Algorithms", value = profile.dsa) { profile = profile.copy(dsa = it) }
                }
            }
        }

        // Career Goal & Urgency
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
                        text = "3. CAREER GOAL & TIMELINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusGreen,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = profile.selectedCareer,
                        onValueChange = { profile = profile.copy(selectedCareer = it) },
                        label = { Text("Target Role") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalBlue,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Urgent Internship Needed?", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DeepNavy)
                            Text("Prioritize fast screening readiness in 2-3 months", fontSize = 11.5.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = profile.urgentInternshipNeeded,
                            onCheckedChange = { profile = profile.copy(urgentInternshipNeeded = it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RoyalBlue)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Placement Season Approaching?", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DeepNavy)
                            Text("Fast-track critical gaps first", fontSize = 11.5.sp, color = TextSecondary)
                        }
                        Switch(
                            checked = profile.placementSeasonApproaching,
                            onCheckedChange = { profile = profile.copy(placementSeasonApproaching = it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RoyalBlue)
                        )
                    }
                }
            }
        }

        // Action Buttons
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { onSaveProfile(profile) },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Profile & Update Career Path", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                OutlinedButton(
                    onClick = onReassess,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RoyalBlue),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalBlue.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reassess Career Fit Matrix", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                }
            }
        }

        item {
            DisclaimerBanner(
                text = "CareerPath AI stores academic and skill records locally on device. Your profile is used exclusively to generate personalized career roadmaps."
            )
        }
    }
}

@Composable
fun SkillSlider(
    name: String,
    value: Int,
    range: IntRange = 0..10,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, fontSize = 12.5.sp, fontWeight = FontWeight.Medium, color = TextMain)
            Text(text = "$value / ${range.last}", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = range.last - range.first - 1,
            colors = SliderDefaults.colors(
                thumbColor = RoyalBlue,
                activeTrackColor = RoyalBlue,
                inactiveTrackColor = SoftBlue
            )
        )
    }
}
