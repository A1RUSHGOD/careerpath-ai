package com.example

import com.example.data.local.AppDatabase
import com.example.data.model.GapPriority
import com.example.data.model.StudentProfile
import com.example.data.remote.GeminiService
import com.example.data.repository.CareerRepository
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CareerPathLogicTest {

    @Test
    fun testCareerAssessmentAndSkillGaps() {
        val context = RuntimeEnvironment.getApplication()
        val db = AppDatabase.getInstance(context)
        val repo = CareerRepository(db.careerPathDao(), GeminiService())

        val testProfile = StudentProfile(
            name = "Test Student",
            college = "PICT",
            branch = "Computer Engineering",
            currentYear = "3rd Year",
            sql = 4,
            python = 7,
            powerBi = 5,
            completedProjects = 2,
            hoursPerDay = 2,
            selectedCareer = "Data Analyst"
        )

        val recommendations = repo.assessCareers(testProfile)
        assertFalse(recommendations.isEmpty())

        val topRecommendation = recommendations.first()
        assertEquals("Data Analyst", topRecommendation.name)
        assertTrue("Fit score should be substantial", topRecommendation.fitScore >= 60)

        val gaps = repo.getSkillGapsForCareer(testProfile, "Data Analyst")
        val sqlGap = gaps.find { it.skillName.contains("SQL") }
        assertNotNull("SQL gap should exist", sqlGap)
        assertEquals(GapPriority.CRITICAL, sqlGap?.priority)

        val scenario = repo.simulateScenario(
            hoursPerDay = 2,
            durationMonths = 6,
            projectsCount = 2,
            hasInternship = false,
            targetCareer = "Data Analyst",
            baseProfile = testProfile
        )
        assertTrue("Readiness score should be calculated", scenario.estimatedReadinessScore in 50..95)
    }
}
