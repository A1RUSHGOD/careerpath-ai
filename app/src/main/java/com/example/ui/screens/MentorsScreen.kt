package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HumanMentor
import com.example.ui.CareerPathUiState
import com.example.ui.components.DisclaimerBanner
import com.example.ui.theme.*

@Composable
fun MentorsScreen(
    state: CareerPathUiState
) {
    val mentors = state.verifiedMentors
    var bookedMentorName by remember { mutableStateOf<String?>(null) }

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
                        text = "Verified Industry Mentors",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Connect 1-on-1 with engineers and analysts from top tech companies who navigated tier-2/3 placements.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }
            }
        }

        items(mentors) { mentor ->
            MentorCard(
                mentor = mentor,
                onBook = { bookedMentorName = mentor.name }
            )
        }

        item {
            DisclaimerBanner(
                text = "All mentors are alumni and active practitioners at Indian product and consulting firms."
            )
        }
    }

    if (bookedMentorName != null) {
        AlertDialog(
            onDismissRequest = { bookedMentorName = null },
            title = {
                Text(
                    text = "1:1 Session Requested",
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            },
            text = {
                Text(
                    text = "A consultation request has been sent to $bookedMentorName. You will receive a calendar invite once confirmed.",
                    fontSize = 13.5.sp,
                    color = TextMain
                )
            },
            confirmButton = {
                Button(
                    onClick = { bookedMentorName = null },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun MentorCard(
    mentor: HumanMentor,
    onBook: () -> Unit
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(SoftBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = mentor.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = RoyalBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "${mentor.role} at ${mentor.company}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = RoyalBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "Alumni: ${mentor.collegeAlumni} • ${mentor.yearsExperience} yrs experience",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = BackgroundLight,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Expertise: ${mentor.expertise.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = TextMain,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Availability: ${mentor.availability}",
                        fontSize = 11.5.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = SoftGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = mentor.domain,
                        color = StatusGreen,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Button(
                    onClick = onBook,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("Book 1:1 Session", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
