package com.example.data.model

data class StudentProfile(
    val name: String = "Demo Student",
    val college: String = "Pune Institute of Computer Technology",
    val degree: String = "B.Tech / B.E.",
    val branch: String = "Computer Engineering",
    val currentYear: String = "3rd Year",
    val currentSemester: Int = 5,
    val cgpa: Double = 7.8,
    val backlogs: Int = 0,

    // Career preferences
    val preferredDomain: String = "Data Analyst",
    val codingInterest: Int = 7,
    val mathStatsInterest: Int = 6,
    val businessInterest: Int = 7,
    val preferredWorkType: String = "Hybrid",
    val preferredLocation: String = "Bangalore / Pune",
    val targetSalaryRange: String = "7-12 LPA",

    // Current skills (0-10)
    val python: Int = 7,
    val java: Int = 4,
    val cpp: Int = 5,
    val sql: Int = 4,
    val excel: Int = 6,
    val powerBi: Int = 5,
    val statistics: Int = 5,
    val machineLearning: Int = 4,
    val deepLearning: Int = 2,
    val dsa: Int = 5,
    val webDev: Int = 4,
    val cloudComputing: Int = 3,
    val cybersecurity: Int = 2,
    val communication: Int = 7,
    val problemSolving: Int = 6,
    val gitGithub: Int = 5,

    // Extra profile data
    val completedProjects: Int = 2,
    val internships: Int = 0,
    val certifications: Int = 1,
    val leetcodeProblems: Int = 85,
    val hasResume: Boolean = true,
    val hasGithub: Boolean = true,

    // Constraints
    val hoursPerDay: Int = 2,
    val daysPerWeek: Int = 6,
    val monthlyBudget: Int = 1000,
    val urgentInternshipNeeded: Boolean = true,
    val placementSeasonApproaching: Boolean = true,

    // Current chosen target career
    val selectedCareer: String = "Data Analyst"
)

data class CareerRecommendation(
    val id: String,
    val name: String,
    val fitScore: Int, // 0-100 AI Estimated Fit Score
    val whyItFits: String,
    val requiredSkills: List<String>,
    val currentStrengths: List<String>,
    val majorGaps: List<String>,
    val typicalPrepDuration: String,
    val difficultyLevel: String, // "Moderate", "Challenging", "Demanding"
    val suggestedNextStep: String,
    val suggestedPriority: String // "HIGH", "MEDIUM", "LOW"
)

data class SkillGapItem(
    val skillName: String,
    val currentLevel: Int, // 0-10
    val targetLevel: Int,  // 0-10
    val gap: Int,          // target - current
    val priority: GapPriority, // CRITICAL, IMPORTANT, LOW
    val category: String,  // Technical, Tool, Core
    val impactReason: String
)

enum class GapPriority {
    CRITICAL,  // 🔴
    IMPORTANT, // 🟠
    LOW        // 🟢
}

data class RoadmapMilestone(
    val monthNumber: Int,
    val monthTitle: String,
    val objective: String,
    val skills: List<String>,
    val tasks: List<String>,
    val estimatedHours: Int,
    val deliverable: String,
    val completionCriteria: String,
    val isCompleted: Boolean = false
)

data class WeeklyTask(
    val id: Long,
    val weekNumber: Int,
    val dayOfWeek: String,
    val title: String,
    val description: String,
    val estimatedMinutes: Int,
    val difficulty: String,
    val skill: String,
    val isCompleted: Boolean = false
)

data class WhatIfScenario(
    val title: String,
    val hoursPerDay: Int,
    val durationMonths: Int,
    val projectsCount: Int,
    val hasInternship: Boolean,
    val targetCareer: String,
    val estimatedReadinessScore: Int,
    val analysisNotes: String
)

data class RecommendedProject(
    val id: String,
    val title: String,
    val domain: String,
    val difficulty: String,
    val estimatedWeeks: Int,
    val problemStatement: String,
    val skillsDeveloped: List<String>,
    val datasetApiRequirements: String,
    val expectedDeliverables: List<String>,
    val interviewTalkingPoints: List<String>
)

data class HumanMentor(
    val id: String,
    val name: String,
    val role: String,
    val company: String,
    val domain: String,
    val yearsExperience: Int,
    val collegeAlumni: String,
    val expertise: List<String>,
    val availability: String,
    val isVerified: Boolean = true
)

data class ResumeFeedback(
    val readinessScore: Int,
    val summary: String,
    val matchingKeywords: List<String>,
    val missingKeywords: List<String>,
    val criticalGaps: List<String>,
    val weakBulletPoints: List<String>,
    val suggestedFixes: List<String>
)

data class InterviewQuestion(
    val id: String,
    val career: String,
    val questionType: String, // Technical, HR, Behavioral, Project-based
    val question: String,
    val contextHint: String
)

data class InterviewEvaluation(
    val score: Int, // /100
    val whatWasGood: String,
    val whatWasMissing: String,
    val betterAnswerStructure: String,
    val followUpQuestion: String
)
