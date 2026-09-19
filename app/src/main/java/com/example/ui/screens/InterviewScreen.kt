package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
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
fun InterviewScreen(
    state: CareerPathUiState,
    onEvaluateAnswer: (career: String, question: String, answer: String) -> Unit
) {
    var selectedCareer by remember { mutableStateOf(state.profile.selectedCareer) }
    var selectedCategory by remember { mutableStateOf("Technical") }
    var selectedDifficulty by remember { mutableStateOf("Intermediate") }

    val technicalQuestions = listOf(
        "Explain the difference between WHERE and HAVING in SQL with a realistic query example.",
        "How do you handle missing values in a dataset? When do you impute vs drop rows or columns?",
        "What are window functions in SQL, and when would you use ROW_NUMBER() vs RANK()?"
    )

    val hrQuestions = listOf(
        "Tell me about yourself and why you want to pursue a career in $selectedCareer.",
        "Where do you see yourself in 3 years after starting as an entry-level analyst?"
    )

    val behavioralQuestions = listOf(
        "Tell me about a time you worked on a technical project with a tight deadline and how you prioritized.",
        "Describe a situation where you had a disagreement with a project teammate and how you resolved it."
    )

    val currentQuestions = when (selectedCategory) {
        "HR" -> hrQuestions
        "Behavioral" -> behavioralQuestions
        else -> technicalQuestions
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val activeQuestion = currentQuestions.getOrElse(currentQuestionIndex) { currentQuestions.first() }

    var studentAnswer by remember {
        mutableStateOf("To handle missing values, we first check if the missing data is MCAR or MAR. For numerical columns with few outliers, mean or median imputation works, but if missingness is over 50% we evaluate dropping the feature. We must always check if the missingness itself conveys a signal.")
    }

    val eval = state.interviewEvaluation

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
                        text = "AI Interview Practice",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Practice responding to questions and receive instant AI grading, critique, and structural coaching.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // Selection Controls Card (Category, Difficulty)
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
                        text = "Category",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Technical", "HR", "Behavioral").forEach { cat ->
                            val isSelected = selectedCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedCategory = cat
                                    currentQuestionIndex = 0
                                },
                                label = { Text(cat, fontSize = 12.sp) },
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

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Difficulty",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Beginner", "Intermediate", "Advanced").forEach { diff ->
                            val isSelected = selectedDifficulty == diff
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedDifficulty = diff },
                                label = { Text(diff, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Purple,
                                    selectedLabelColor = Color.White,
                                    containerColor = SoftLavender,
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

        // Question Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftLavender.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Purple.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = CardWhite,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Question ${currentQuestionIndex + 1} of ${currentQuestions.size}",
                                color = Purple,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        TextButton(
                            onClick = {
                                currentQuestionIndex = (currentQuestionIndex + 1) % currentQuestions.size
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = Purple)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Next Question", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = activeQuestion,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
        }

        // Student Answer Field
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
                        text = "Your Response",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = studentAnswer,
                        onValueChange = { studentAnswer = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text("Structure your response using STAR or step-by-step logic...") },
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
                            if (studentAnswer.isNotBlank()) {
                                onEvaluateAnswer(selectedCareer, activeQuestion, studentAnswer)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        enabled = !state.isEvaluatingInterview && studentAnswer.isNotBlank()
                    ) {
                        if (state.isEvaluatingInterview) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI is analyzing response...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Evaluate Answer with AI", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Evaluation Results (if evaluated)
        if (eval != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, StatusGreen.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AI Interview Evaluation",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )

                            Surface(
                                color = SoftGreen,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "${eval.score}/100",
                                    color = StatusGreen,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // What you did well
                        Surface(
                            color = SoftGreen,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "✅ What You Did Well",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = StatusGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = eval.whatWasGood,
                                    fontSize = 12.5.sp,
                                    color = TextMain,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // What was missing
                        Surface(
                            color = SoftOrange,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "⚠️ What Was Missing",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = StatusOrange
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = eval.whatWasMissing,
                                    fontSize = 12.5.sp,
                                    color = TextMain,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Better answer structure
                        Surface(
                            color = SoftLavender,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "🎯 Recommended Better Answer Structure",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Purple
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = eval.betterAnswerStructure,
                                    fontSize = 12.5.sp,
                                    color = TextMain,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        if (eval.followUpQuestion.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                color = SoftBlue,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "❓ Follow-Up Question",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = RoyalBlue
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = eval.followUpQuestion,
                                        fontSize = 12.5.sp,
                                        color = TextMain,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            DisclaimerBanner(
                text = "Interview feedback is evaluated using AI assessment against realistic Indian hiring benchmarks."
            )
        }
    }
}
