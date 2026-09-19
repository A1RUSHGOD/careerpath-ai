package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profiles")
data class StudentProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val college: String,
    val degree: String,
    val branch: String,
    val currentYear: String,
    val currentSemester: Int,
    val cgpa: Double,
    val backlogs: Int,
    val preferredDomain: String,
    val codingInterest: Int,
    val mathStatsInterest: Int,
    val businessInterest: Int,
    val preferredWorkType: String,
    val preferredLocation: String,
    val targetSalaryRange: String,
    val python: Int,
    val java: Int,
    val cpp: Int,
    val sql: Int,
    val excel: Int,
    val powerBi: Int,
    val statistics: Int,
    val machineLearning: Int,
    val deepLearning: Int,
    val dsa: Int,
    val webDev: Int,
    val cloudComputing: Int,
    val cybersecurity: Int,
    val communication: Int,
    val problemSolving: Int,
    val gitGithub: Int,
    val completedProjects: Int,
    val internships: Int,
    val certifications: Int,
    val leetcodeProblems: Int,
    val hasResume: Boolean,
    val hasGithub: Boolean,
    val hoursPerDay: Int,
    val daysPerWeek: Int,
    val monthlyBudget: Int,
    val urgentInternshipNeeded: Boolean,
    val placementSeasonApproaching: Boolean,
    val selectedCareer: String
)

@Entity(tableName = "roadmap_tasks")
data class RoadmapTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthNumber: Int,
    val weekNumber: Int,
    val dayOfWeek: String,
    val title: String,
    val description: String,
    val estimatedMinutes: Int,
    val difficulty: String,
    val skill: String,
    val isCompleted: Boolean = false,
    val deliverable: String = ""
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "user" or "mentor"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "interview_sessions")
data class InterviewSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val career: String,
    val questionType: String,
    val question: String,
    val userAnswer: String,
    val score: Int,
    val whatWasGood: String,
    val whatWasMissing: String,
    val betterAnswerStructure: String,
    val followUpQuestion: String,
    val timestamp: Long = System.currentTimeMillis()
)
