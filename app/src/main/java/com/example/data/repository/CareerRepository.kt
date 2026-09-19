package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import com.example.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class CareerRepository(
    private val dao: CareerPathDao,
    private val geminiService: GeminiService
) {

    val studentProfile: Flow<StudentProfile> = dao.getProfile().map { entity ->
        entity?.toDomain() ?: StudentProfile()
    }

    val tasks: Flow<List<WeeklyTask>> = dao.getTasks().map { list ->
        list.map { it.toDomain() }
    }

    val chatMessages: Flow<List<ChatMessageEntity>> = dao.getChatMessages()

    suspend fun saveProfile(profile: StudentProfile) {
        dao.insertOrUpdateProfile(profile.toEntity())
    }

    suspend fun loadDemoProfile() {
        val demo = StudentProfile(
            name = "Demo Student",
            college = "Pune Institute of Computer Technology",
            degree = "B.Tech / B.E.",
            branch = "Computer Engineering",
            currentYear = "3rd Year",
            currentSemester = 5,
            cgpa = 7.8,
            backlogs = 0,
            preferredDomain = "Data Analyst",
            codingInterest = 7,
            mathStatsInterest = 6,
            businessInterest = 7,
            preferredWorkType = "Hybrid",
            preferredLocation = "Bangalore / Pune",
            targetSalaryRange = "7-12 LPA",
            python = 7,
            java = 4,
            cpp = 5,
            sql = 4,
            excel = 6,
            powerBi = 5,
            statistics = 5,
            machineLearning = 4,
            deepLearning = 2,
            dsa = 5,
            webDev = 4,
            cloudComputing = 3,
            cybersecurity = 2,
            communication = 7,
            problemSolving = 6,
            gitGithub = 5,
            completedProjects = 2,
            internships = 0,
            certifications = 1,
            leetcodeProblems = 85,
            hasResume = true,
            hasGithub = true,
            hoursPerDay = 2,
            daysPerWeek = 6,
            monthlyBudget = 1000,
            urgentInternshipNeeded = true,
            placementSeasonApproaching = true,
            selectedCareer = "Data Analyst"
        )
        dao.insertOrUpdateProfile(demo.toEntity())
        generateRoadmapForCareer(demo, "Data Analyst")
    }

    suspend fun toggleTaskCompletion(taskId: Long, isCompleted: Boolean) {
        dao.updateTaskCompletion(taskId, isCompleted)
    }

    suspend fun selectTargetCareer(careerName: String, currentProfile: StudentProfile) {
        val updated = currentProfile.copy(selectedCareer = careerName)
        saveProfile(updated)
        generateRoadmapForCareer(updated, careerName)
    }

    // --- Deterministic Assessment Engine ---
    fun assessCareers(profile: StudentProfile): List<CareerRecommendation> {
        val careers = listOf(
            evaluateDataAnalyst(profile),
            evaluateDataScientist(profile),
            evaluateSoftwareDeveloper(profile),
            evaluateMlEngineer(profile),
            evaluateCybersecurity(profile),
            evaluateCloudEngineer(profile),
            evaluateProductAnalyst(profile),
            evaluateManagementMba(profile)
        )
        return careers.sortedByDescending { it.fitScore }
    }

    private fun evaluateDataAnalyst(p: StudentProfile): CareerRecommendation {
        // Core: SQL(8), Excel(7), PowerBI(7), Python(6), Stats(6), Projects(6)
        val score = (
            p.sql * 2.5 +
            p.excel * 1.5 +
            p.powerBi * 1.8 +
            p.python * 1.5 +
            p.statistics * 1.2 +
            min(p.completedProjects * 2.0, 10.0) +
            p.problemSolving * 0.5
        ).roundToInt().coerceIn(20, 95)

        val gaps = mutableListOf<String>()
        if (p.sql < 8) gaps.add("SQL querying & window functions (${p.sql}/10 vs 8/10)")
        if (p.powerBi < 7) gaps.add("Power BI / Tableau dashboarding (${p.powerBi}/10 vs 7/10)")
        if (p.completedProjects < 3) gaps.add("Industry data analysis projects (${p.completedProjects} vs 3 required)")

        val strengths = mutableListOf<String>()
        if (p.python >= 6) strengths.add("Solid Python programming baseline")
        if (p.excel >= 6) strengths.add("Proficient in spreadsheet modeling")
        if (p.communication >= 6) strengths.add("Strong stakeholder communication")

        val duration = when {
            p.hoursPerDay >= 3 -> "3–4 months"
            p.hoursPerDay == 2 -> "5–6 months"
            else -> "7–9 months"
        }

        return CareerRecommendation(
            id = "da",
            name = "Data Analyst",
            fitScore = score,
            whyItFits = "Your analytical interest and Python foundation give you high leverage. Bridging SQL and dashboarding will make you interview-ready fastest.",
            requiredSkills = listOf("SQL", "Power BI / Tableau", "Excel", "Python (Pandas)", "Statistics", "Portfolio"),
            currentStrengths = strengths.ifEmpty { listOf("Quick learner", "Engineering aptitude") },
            majorGaps = gaps.ifEmpty { listOf("Advanced business storytelling") },
            typicalPrepDuration = duration,
            difficultyLevel = "Moderate",
            suggestedNextStep = "Master SQL JOINs, GROUP BY, and window functions within 30 days.",
            suggestedPriority = "HIGH"
        )
    }

    private fun evaluateDataScientist(p: StudentProfile): CareerRecommendation {
        val score = (
            p.python * 2.0 +
            p.statistics * 2.2 +
            p.machineLearning * 2.0 +
            p.sql * 1.5 +
            p.mathStatsInterest * 1.3 +
            p.deepLearning * 1.0
        ).roundToInt().coerceIn(15, 95)

        val gaps = mutableListOf<String>()
        if (p.statistics < 8) gaps.add("Inferential statistics & hypothesis testing (${p.statistics}/10)")
        if (p.machineLearning < 7) gaps.add("Scikit-learn model tuning & validation (${p.machineLearning}/10)")
        if (p.internships < 1) gaps.add("Practical ML production experience")

        val strengths = mutableListOf<String>()
        if (p.python >= 7) strengths.add("Strong Python fluency")
        if (p.mathStatsInterest >= 7) strengths.add("High quantitative appetite")

        return CareerRecommendation(
            id = "ds",
            name = "Data Scientist",
            fitScore = score,
            whyItFits = "Requires deep mathematical rigor and applied ML. Strong long-term trajectory if you commit to statistics and feature engineering.",
            requiredSkills = listOf("Python", "Applied Statistics", "Machine Learning", "SQL", "Feature Engineering", "Data Modeling"),
            currentStrengths = strengths.ifEmpty { listOf("Python interest") },
            majorGaps = gaps.ifEmpty { listOf("Production ML experience") },
            typicalPrepDuration = "8–12 months",
            difficultyLevel = "Challenging",
            suggestedNextStep = "Deepen probability distributions and complete an end-to-end regression/classification pipeline.",
            suggestedPriority = "MEDIUM"
        )
    }

    private fun evaluateSoftwareDeveloper(p: StudentProfile): CareerRecommendation {
        val codingMax = max(p.java, max(p.cpp, p.python))
        val score = (
            p.dsa * 3.0 +
            codingMax * 2.5 +
            p.webDev * 1.5 +
            p.gitGithub * 1.0 +
            min(p.leetcodeProblems / 15.0, 10.0) * 1.0 +
            p.problemSolving * 1.0
        ).roundToInt().coerceIn(20, 95)

        val gaps = mutableListOf<String>()
        if (p.dsa < 7) gaps.add("DSA (Trees, Graphs, DP) (${p.dsa}/10)")
        if (p.leetcodeProblems < 150) gaps.add("Competitive coding practice (${p.leetcodeProblems}/150+ problems)")
        if (p.webDev < 6) gaps.add("Full-stack web framework & APIs")

        val strengths = mutableListOf<String>()
        if (p.gitGithub >= 5) strengths.add("Version control familiarity")
        if (p.problemSolving >= 6) strengths.add("Structured analytical thinking")

        return CareerRecommendation(
            id = "sde",
            name = "Software Developer",
            fitScore = score,
            whyItFits = "High demand across Indian campus placements. Requires disciplined LeetCode DSA practice and modern backend/web development.",
            requiredSkills = listOf("Data Structures & Algorithms", "Java/C++/Python", "System Design Basics", "Git/GitHub", "REST APIs"),
            currentStrengths = strengths.ifEmpty { listOf("Engineering core") },
            majorGaps = gaps.ifEmpty { listOf("Large scale architecture") },
            typicalPrepDuration = "6–10 months",
            difficultyLevel = "Challenging",
            suggestedNextStep = "Solve 2 DSA questions daily focusing on arrays, strings, and hash maps.",
            suggestedPriority = "MEDIUM"
        )
    }

    private fun evaluateMlEngineer(p: StudentProfile): CareerRecommendation {
        val score = (
            p.python * 2.2 +
            p.machineLearning * 2.5 +
            p.deepLearning * 2.0 +
            p.cloudComputing * 1.3 +
            p.gitGithub * 1.0 +
            p.dsa * 1.0
        ).roundToInt().coerceIn(15, 92)

        return CareerRecommendation(
            id = "mle",
            name = "ML Engineer",
            fitScore = score,
            whyItFits = "Combines software engineering with machine learning deployment (MLOps). Ideal if you enjoy model serving and cloud pipelines.",
            requiredSkills = listOf("Python", "PyTorch/TensorFlow", "Docker", "MLflow/FastAPI", "Cloud Deployment", "DSA"),
            currentStrengths = listOf("Python baseline", "Engineering foundation"),
            majorGaps = listOf("Model deployment & Docker", "Deep learning architectures", "MLOps pipelines"),
            typicalPrepDuration = "10–14 months",
            difficultyLevel = "Demanding",
            suggestedNextStep = "Deploy an ML model as a Dockerized FastAPI container on cloud.",
            suggestedPriority = "LOW"
        )
    }

    private fun evaluateCybersecurity(p: StudentProfile): CareerRecommendation {
        val score = (
            p.cybersecurity * 3.5 +
            p.cloudComputing * 1.5 +
            p.problemSolving * 2.0 +
            p.python * 1.5 +
            p.communication * 1.5
        ).roundToInt().coerceIn(15, 90)

        return CareerRecommendation(
            id = "cyber",
            name = "Cybersecurity Analyst",
            fitScore = score,
            whyItFits = "Crucial industry sector with growing SOC and penetration testing roles in Indian MNCs.",
            requiredSkills = listOf("Networking fundamentals", "Linux/Bash", "SIEM Tools", "Vulnerability Assessment", "CompTIA Security+"),
            currentStrengths = listOf("Problem solving logic"),
            majorGaps = listOf("Network packet inspection (Wireshark)", "Linux security administration", "OWASP Top 10"),
            typicalPrepDuration = "6–8 months",
            difficultyLevel = "Moderate",
            suggestedNextStep = "Set up a Kali Linux lab and complete TryHackMe pre-security pathway.",
            suggestedPriority = "LOW"
        )
    }

    private fun evaluateCloudEngineer(p: StudentProfile): CareerRecommendation {
        val score = (
            p.cloudComputing * 3.5 +
            p.gitGithub * 2.0 +
            p.python * 1.5 +
            p.problemSolving * 1.5 +
            p.webDev * 1.5
        ).roundToInt().coerceIn(15, 92)

        return CareerRecommendation(
            id = "cloud",
            name = "Cloud Engineer",
            fitScore = score,
            whyItFits = "High cloud adoption across India. Excellent infrastructure and DevOps career opportunities.",
            requiredSkills = listOf("AWS/GCP/Azure", "Docker & Kubernetes", "Linux", "Terraform", "CI/CD Pipelines"),
            currentStrengths = listOf("Basic cloud awareness", "Git knowledge"),
            majorGaps = listOf("Hands-on AWS/GCP certification", "Docker & container orchestration", "Linux shell scripting"),
            typicalPrepDuration = "6–9 months",
            difficultyLevel = "Moderate",
            suggestedNextStep = "Study for AWS Certified Cloud Practitioner or Solutions Architect Associate.",
            suggestedPriority = "LOW"
        )
    }

    private fun evaluateProductAnalyst(p: StudentProfile): CareerRecommendation {
        val score = (
            p.sql * 2.5 +
            p.excel * 2.0 +
            p.communication * 2.5 +
            p.businessInterest * 2.0 +
            p.problemSolving * 1.0
        ).roundToInt().coerceIn(20, 94)

        return CareerRecommendation(
            id = "pa",
            name = "Product / Business Analyst",
            fitScore = score,
            whyItFits = "Bridges technology and business strategy. Focuses on metrics, user behavior, and data-driven decisions.",
            requiredSkills = listOf("SQL", "Product Metrics (CAC, LTV, Retention)", "Excel/Sheets", "A/B Testing", "Wireframing", "Storytelling"),
            currentStrengths = listOf("High communication skills", "Business curiosity", "Analytical mindset"),
            majorGaps = listOf("A/B testing methodology", "Cohort & funnel analysis", "Product metric frameworks"),
            typicalPrepDuration = "4–6 months",
            difficultyLevel = "Moderate",
            suggestedNextStep = "Learn cohort analysis in SQL and complete a product teardown of Swiggy or Zomato.",
            suggestedPriority = "MEDIUM"
        )
    }

    private fun evaluateManagementMba(p: StudentProfile): CareerRecommendation {
        val score = (
            p.communication * 3.0 +
            p.businessInterest * 3.0 +
            p.problemSolving * 2.0 +
            (p.cgpa * 10) * 0.2
        ).roundToInt().coerceIn(20, 90)

        return CareerRecommendation(
            id = "mba",
            name = "Tech Management / MBA",
            fitScore = score,
            whyItFits = "Suited for students targeting CAT/GMAT or corporate management associate programs.",
            requiredSkills = listOf("Quantitative Aptitude", "Verbal Reasoning", "Case Studies", "Financial Literacy", "Leadership"),
            currentStrengths = listOf("Strong verbal communication", "Good academic standing"),
            majorGaps = listOf("Structured case interview solving", "Aptitude speed testing"),
            typicalPrepDuration = "9–12 months",
            difficultyLevel = "Demanding",
            suggestedNextStep = "Take a diagnostic CAT/GMAT sectional test to baseline quantitative speed.",
            suggestedPriority = "LOW"
        )
    }

    // --- Skill Gap Analysis ---
    fun getSkillGapsForCareer(profile: StudentProfile, career: String): List<SkillGapItem> {
        val gaps = mutableListOf<SkillGapItem>()
        when (career) {
            "Data Analyst" -> {
                gaps.add(SkillGapItem("SQL Queries & Joins", profile.sql, 8, max(0, 8 - profile.sql), GapPriority.CRITICAL, "Technical", "Gating requirement for 90%+ data analyst interview screens"))
                gaps.add(SkillGapItem("Power BI / Dashboarding", profile.powerBi, 7, max(0, 7 - profile.powerBi), GapPriority.CRITICAL, "Tool", "Required for business presentations and client reporting"))
                gaps.add(SkillGapItem("Real-World Projects", profile.completedProjects, 3, max(0, 3 - profile.completedProjects), GapPriority.CRITICAL, "Portfolio", "Resumes with generic projects get filtered out by Indian recruiters"))
                gaps.add(SkillGapItem("Advanced Excel Modeling", profile.excel, 7, max(0, 7 - profile.excel), GapPriority.IMPORTANT, "Tool", "Essential for rapid ad-hoc calculations and financial sanity checks"))
                gaps.add(SkillGapItem("Python (Pandas, Numpy)", profile.python, 6, max(0, 6 - profile.python), GapPriority.LOW, "Technical", "Needed for automated data extraction and cleaning workflows"))
                gaps.add(SkillGapItem("Business Communication", profile.communication, 8, max(0, 8 - profile.communication), GapPriority.IMPORTANT, "Core", "Translating analytical findings into actionable business ROI"))
            }
            "Data Scientist" -> {
                gaps.add(SkillGapItem("Applied Statistics", profile.statistics, 8, max(0, 8 - profile.statistics), GapPriority.CRITICAL, "Technical", "Hypothesis testing and A/B test validation"))
                gaps.add(SkillGapItem("Machine Learning Models", profile.machineLearning, 8, max(0, 8 - profile.machineLearning), GapPriority.CRITICAL, "Technical", "Supervised/unsupervised algorithms and hyperparameter tuning"))
                gaps.add(SkillGapItem("SQL & Data Extraction", profile.sql, 7, max(0, 7 - profile.sql), GapPriority.IMPORTANT, "Technical", "Extracting training sets from large data warehouses"))
                gaps.add(SkillGapItem("Python for ML", profile.python, 8, max(0, 8 - profile.python), GapPriority.IMPORTANT, "Technical", "Writing clean scikit-learn and pandas pipelines"))
                gaps.add(SkillGapItem("End-to-End ML Projects", profile.completedProjects, 4, max(0, 4 - profile.completedProjects), GapPriority.CRITICAL, "Portfolio", "Demonstrating business impact and deployment"))
            }
            "Software Developer" -> {
                gaps.add(SkillGapItem("Data Structures & Algorithms", profile.dsa, 8, max(0, 8 - profile.dsa), GapPriority.CRITICAL, "Technical", "Standard hurdle for technical screening rounds"))
                gaps.add(SkillGapItem("Core Coding (Java/C++/Python)", max(profile.java, profile.cpp), 8, max(0, 8 - max(profile.java, profile.cpp)), GapPriority.CRITICAL, "Technical", "Clean OOP code, exception handling, and edge cases"))
                gaps.add(SkillGapItem("Web Frameworks / APIs", profile.webDev, 7, max(0, 7 - profile.webDev), GapPriority.IMPORTANT, "Technical", "Building RESTful backend services or modern web apps"))
                gaps.add(SkillGapItem("System Design Fundamentals", 3, 6, 3, GapPriority.IMPORTANT, "Core", "Scalability, caching, database indexing, load balancers"))
                gaps.add(SkillGapItem("Git / Collaboration", profile.gitGithub, 7, max(0, 7 - profile.gitGithub), GapPriority.LOW, "Tool", "Branching, pull requests, and CI/CD basics"))
            }
            else -> {
                gaps.add(SkillGapItem("Core Domain Skills", 4, 8, 4, GapPriority.CRITICAL, "Technical", "Essential foundation for this domain"))
                gaps.add(SkillGapItem("Portfolio & Proof of Work", profile.completedProjects, 3, max(0, 3 - profile.completedProjects), GapPriority.CRITICAL, "Portfolio", "Verified projects to showcase in interviews"))
                gaps.add(SkillGapItem("Problem Solving", profile.problemSolving, 8, max(0, 8 - profile.problemSolving), GapPriority.IMPORTANT, "Core", "Live problem structuring and technical communication"))
            }
        }
        return gaps.sortedBy { it.priority.ordinal }
    }

    // --- Roadmap Generation ---
    suspend fun generateRoadmapForCareer(profile: StudentProfile, career: String) {
        dao.clearTasks()
        val generatedTasks = mutableListOf<RoadmapTaskEntity>()

        val hours = max(1, profile.hoursPerDay)
        val dailyMinutes = hours * 60

        when (career) {
            "Data Analyst" -> {
                // Month 1: SQL Mastery
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Monday", title = "SQL SELECT, WHERE & Filtering", description = "Setup PostgreSQL/MySQL, practice basic filtering and logical operators", estimatedMinutes = dailyMinutes, difficulty = "Easy", skill = "SQL", deliverable = "15 practice problems on HackerRank"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Tuesday", title = "GROUP BY, HAVING & Aggregations", description = "COUNT, SUM, AVG, MIN, MAX with grouped filtering", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "SQL", deliverable = "10 aggregation queries"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Wednesday", title = "INNER & LEFT JOINs Deep-Dive", description = "Multi-table relational joins with schema diagrams", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "SQL", deliverable = "E-commerce schema join exercises"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Thursday", title = "Self JOINs & Complex Conditions", description = "Hierarchical data and employee-manager queries", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "SQL", deliverable = "5 self-join solutions"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Friday", title = "Subqueries & Common Table Expressions (CTEs)", description = "Writing readable modular queries using WITH clause", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "SQL", deliverable = "Refactor 3 nested subqueries into CTEs"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Saturday", title = "Week 1 Milestone: Timed SQL Drill", description = "Complete 1-hour timed mock assessment on StrataScratch / LeetCode", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "SQL", deliverable = "8/10 passing score"))

                // Month 2: Power BI & Business Dashboards
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 2, weekNumber = 5, dayOfWeek = "Monday", title = "Power Query & Data Cleaning", description = "Import raw Indian retail CSVs, handle missing values and types", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "Power BI", deliverable = "Cleaned dimensional model"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 2, weekNumber = 5, dayOfWeek = "Wednesday", title = "DAX Measures (CALCULATE, RELATED, YTD)", description = "Write critical KPI measures for revenue growth and profit margin", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "Power BI", deliverable = "6 custom DAX formulas"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 2, weekNumber = 5, dayOfWeek = "Saturday", title = "Executive Sales Dashboard", description = "Build multi-page interactive dashboard with drill-downs", estimatedMinutes = dailyMinutes + 30, difficulty = "Medium", skill = "Power BI", deliverable = "Published PDF + GitHub README"))

                // Month 3: Real World Indian Industry Project
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 3, weekNumber = 9, dayOfWeek = "Monday", title = "Swiggy / Zomato Order Analytics Project", description = "Define business questions: delivery bottlenecks and city revenue", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "Python & SQL", deliverable = "Project proposal document"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 3, weekNumber = 9, dayOfWeek = "Thursday", title = "Exploratory Data Analysis in Pandas", description = "Univariate & bivariate distributions, outlier detection", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "Python", deliverable = "Jupyter Notebook with charts"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 3, weekNumber = 9, dayOfWeek = "Saturday", title = "Actionable Insights & PPT Deck", description = "Draft 5 business recommendations for restaurant partners", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "Business Communication", deliverable = "8-slide presentation deck"))

                // Month 4: Advanced Portfolio & GitHub Presence
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 4, weekNumber = 13, dayOfWeek = "Tuesday", title = "GitHub Repository Polish", description = "Structure clean README, architecture diagram, methodology, and screenshots", estimatedMinutes = dailyMinutes, difficulty = "Easy", skill = "Git/GitHub", deliverable = "Live GitHub repository link"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 4, weekNumber = 13, dayOfWeek = "Friday", title = "LinkedIn Project Breakdown Post", description = "Write concise post explaining project insights and tagging relevant mentors", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "Communication", deliverable = "Published post on LinkedIn"))

                // Month 5: Resume & Targeted Applications
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 5, weekNumber = 17, dayOfWeek = "Monday", title = "ATS Resume Quantifiable Bullet Points", description = "Format projects using: Accomplished [X] measured by [Y] by doing [Z]", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "Career Prep", deliverable = "1-page single-column PDF resume"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 5, weekNumber = 17, dayOfWeek = "Thursday", title = "Identify 25 High-Growth Indian Startups", description = "Shortlist companies in Bangalore/Pune/Gurgaon hiring entry-level analysts", estimatedMinutes = dailyMinutes, difficulty = "Easy", skill = "Job Search", deliverable = "Target application tracker spreadsheet"))

                // Month 6: Mock Interviews & Placements
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 6, weekNumber = 21, dayOfWeek = "Wednesday", title = "SQL Live Coding Mock Interview", description = "Practice explaining query thought process aloud during joins and grouping", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "Interview Skills", deliverable = "Recorded 45-min mock session"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 6, weekNumber = 21, dayOfWeek = "Saturday", title = "Behavioral & Guesstimate Prep", description = "Prepare market sizing guesstimates and STAR format project stories", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "HR & Case Prep", deliverable = "Cheat sheet with 4 STAR stories"))
            }
            "Software Developer" -> {
                // SDE Track: DSA + Web backend
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Monday", title = "Time & Space Complexity + Two Pointers", description = "Master Big-O notation, solve Two Sum II and Container With Most Water", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "DSA", deliverable = "5 Two-pointer problems on LeetCode"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Wednesday", title = "Sliding Window & Hash Maps", description = "Longest Substring Without Repeating Characters, Group Anagrams", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "DSA", deliverable = "5 Sliding window problems"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Saturday", title = "Linked Lists & Fast/Slow Pointers", description = "Reverse Linked List, Cycle Detection, Merge Two Sorted Lists", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = "DSA", deliverable = "5 Linked list implementations"))

                generatedTasks.add(RoadmapTaskEntity(monthNumber = 2, weekNumber = 5, dayOfWeek = "Tuesday", title = "Binary Search & Tree Traversals", description = "In-order, Pre-order, Level-order, Lowest Common Ancestor", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "DSA", deliverable = "8 Tree questions"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 3, weekNumber = 9, dayOfWeek = "Wednesday", title = "Production REST API Project", description = "Build scalable Spring Boot / Node / FastAPI backend with JWT Auth", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "Web Dev", deliverable = "Deployed API on Render / Railway"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 4, weekNumber = 13, dayOfWeek = "Friday", title = "Database Indexing & Caching with Redis", description = "Optimize slow query response times and implement caching layer", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "System Design", deliverable = "Benchmark report showing 70% latency drop"))
            }
            else -> {
                // General engineering track
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Monday", title = "Core Domain Fundamentals", description = "Essential syntax, tooling, and environment setup", estimatedMinutes = dailyMinutes, difficulty = "Easy", skill = career, deliverable = "Completed setup exercises"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Wednesday", title = "Applied Practical Drill", description = "Solve foundational problem sets", estimatedMinutes = dailyMinutes, difficulty = "Medium", skill = career, deliverable = "Documented code repository"))
                generatedTasks.add(RoadmapTaskEntity(monthNumber = 2, weekNumber = 5, dayOfWeek = "Saturday", title = "Milestone Portfolio Project", description = "Build end-to-end working system with documentation", estimatedMinutes = dailyMinutes, difficulty = "Hard", skill = "Projects", deliverable = "Live demo link"))
            }
        }

        dao.insertTasks(generatedTasks)
    }

    // --- Adaptive Roadmap Engine ---
    suspend fun adaptRoadmap(profile: StudentProfile, reason: String): String {
        when (reason) {
            "NEED_MORE_SQL" -> {
                val newTasks = listOf(
                    RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Thursday", title = "Remedial SQL Window Functions Drill", description = "Deep-dive ROW_NUMBER(), RANK(), DENSE_RANK() with lead/lag practice", estimatedMinutes = 90, difficulty = "Hard", skill = "SQL", deliverable = "Solve 8 window function problems"),
                    RoadmapTaskEntity(monthNumber = 1, weekNumber = 2, dayOfWeek = "Sunday", title = "SQL Complex Subqueries Diagnostics", description = "Correlated subqueries vs joins performance comparison", estimatedMinutes = 75, difficulty = "Medium", skill = "SQL", deliverable = "Clean query benchmarks")
                )
                dao.insertTasks(newTasks)
                return "Roadmap adapted: Added 2 targeted SQL remedial drills to reinforce your weakest query types."
            }
            "FAST_TRACK" -> {
                val advanceTask = RoadmapTaskEntity(
                    monthNumber = 2,
                    weekNumber = 4,
                    dayOfWeek = "Friday",
                    title = "Fast-Track: Production ETL Pipeline",
                    description = "Automate data ingestion with Python, PostgreSQL, and cron scheduling",
                    estimatedMinutes = 120,
                    difficulty = "Hard",
                    skill = "Python & SQL",
                    deliverable = "Automated scraper + SQL loader script"
                )
                dao.insertTasks(listOf(advanceTask))
                return "Roadmap adapted: Advanced milestone unlocked early! You've demonstrated rapid mastery."
            }
            "REDUCED_HOURS" -> {
                val updated = profile.copy(hoursPerDay = 1)
                saveProfile(updated)
                generateRoadmapForCareer(updated, profile.selectedCareer)
                return "Roadmap adapted: Re-calibrated weekly study blocks to 1 hour/day. Adjusted milestone deadlines across 8 months to prevent burnout."
            }
            "INTERNSHIP_OBTAINED" -> {
                val updated = profile.copy(internships = profile.internships + 1, urgentInternshipNeeded = false)
                saveProfile(updated)
                val workplaceTasks = listOf(
                    RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Tuesday", title = "Corporate Codebase Navigation", description = "Reading enterprise repositories, code reviews, and Git hygiene", estimatedMinutes = 60, difficulty = "Medium", skill = "Workplace Skills", deliverable = "Documented project architecture"),
                    RoadmapTaskEntity(monthNumber = 1, weekNumber = 1, dayOfWeek = "Thursday", title = "Agile & Jira Ticket Workflow", description = "Sizing user stories, standup updates, and sprint delivery", estimatedMinutes = 60, difficulty = "Easy", skill = "Workplace Skills", deliverable = "First sprint tasks scoped")
                )
                dao.insertTasks(workplaceTasks)
                return "Congratulations! Roadmap adapted: Replaced generic job-search tasks with high-performance on-the-job mastery modules."
            }
            else -> {
                return "Roadmap re-balanced based on your current progress metrics."
            }
        }
    }

    // --- What-If Career Simulator ---
    fun simulateScenario(
        hoursPerDay: Int,
        durationMonths: Int,
        projectsCount: Int,
        hasInternship: Boolean,
        targetCareer: String,
        baseProfile: StudentProfile
    ): WhatIfScenario {
        var score = 45

        // Impact of daily hours
        score += when {
            hoursPerDay >= 4 -> 25
            hoursPerDay == 3 -> 20
            hoursPerDay == 2 -> 14
            hoursPerDay == 1 -> 8
            else -> 4
        }

        // Impact of duration
        score += when {
            durationMonths >= 6 -> 18
            durationMonths >= 4 -> 12
            durationMonths >= 2 -> 6
            else -> 2
        }

        // Impact of projects
        score += min(projectsCount * 4, 16)

        // Impact of internship
        if (hasInternship) {
            score += 15
        }

        val readiness = min(96, score)

        val notes = when {
            hoursPerDay <= 1 && durationMonths <= 3 ->
                "High risk: 1 hour/day for <3 months does not allow sufficient repetition for competitive technical screening rounds."
            hasInternship && projectsCount >= 2 ->
                "Strong candidate profile: Prior internship combined with verified projects significantly lifts shortlisting rates across Indian recruiters."
            hoursPerDay >= 3 && durationMonths >= 5 ->
                "Optimal pace: Consistent 3 hours/day over 5+ months enables completing both core skills and end-to-end portfolio deliverables."
            else ->
                "Viable trajectory: Meets baseline screening benchmarks if priority skill gaps are addressed first."
        }

        return WhatIfScenario(
            title = "$hoursPerDay hrs/day × $durationMonths months ($projectsCount projects)",
            hoursPerDay = hoursPerDay,
            durationMonths = durationMonths,
            projectsCount = projectsCount,
            hasInternship = hasInternship,
            targetCareer = targetCareer,
            estimatedReadinessScore = readiness,
            analysisNotes = notes
        )
    }

    // --- AI Career Mentor Chat ---
    suspend fun askMentor(question: String, profile: StudentProfile): String {
        dao.insertChatMessage(ChatMessageEntity(sender = "user", message = question))

        val prompt = """
            You are CareerPath AI's Lead Career Mentor for Indian engineering students (2nd to 4th year, often from tier-2/tier-3 colleges).
            STUDENT PROFILE:
            Name: ${profile.name}
            College: ${profile.college}
            Branch: ${profile.branch}, Year: ${profile.currentYear}, CGPA: ${profile.cgpa}
            Target Career: ${profile.selectedCareer}
            Current Skills: Python ${profile.python}/10, SQL ${profile.sql}/10, Power BI ${profile.powerBi}/10, Excel ${profile.excel}/10, DSA ${profile.dsa}/10
            Completed Projects: ${profile.completedProjects}, Internships: ${profile.internships}
            Available Time: ${profile.hoursPerDay} hrs/day, Urgent Internship: ${profile.urgentInternshipNeeded}
            
            STUDENT QUESTION: "$question"
            
            GUIDELINES:
            1. DO NOT give generic motivational advice.
            2. Challenge unrealistic goals directly.
            3. Address their specific target career (${profile.selectedCareer}) and exact skill numbers.
            4. Recommend the highest-impact next action.
            5. Keep response clear, direct, and under 150 words.
        """.trimIndent()

        val aiResponse = geminiService.generateContent(prompt)
        val answer = aiResponse?.trim() ?: generateFallbackMentorAnswer(question, profile)

        dao.insertChatMessage(ChatMessageEntity(sender = "mentor", message = answer))
        return answer
    }

    private fun generateFallbackMentorAnswer(question: String, profile: StudentProfile): String {
        val q = question.lowercase()
        return when {
            q.contains("deep learning") || q.contains("neural") -> {
                "Based on your target of becoming a ${profile.selectedCareer}, Deep Learning is NOT your priority right now. Your SQL (${profile.sql}/10) and portfolio project gaps are much larger blockers. Spend the next 4 weeks mastering relational joins, window functions, and building 1 high-impact project before touching advanced neural networks."
            }
            q.contains("dsa") || q.contains("leetcode") -> {
                if (profile.selectedCareer == "Data Analyst") {
                    "For a Data Analyst role in India, heavy LeetCode Medium/Hard DSA is rarely asked. Don't burn your ${profile.hoursPerDay} hours/day on dynamic programming. Focus on SQL query speed, pandas data wrangling, and metric interpretation instead."
                } else {
                    "For ${profile.selectedCareer}, consistent DSA is non-negotiable. Aim for 2 solved questions daily on Two Pointers, Sliding Window, and Trees rather than cramming 15 on weekends."
                }
            }
            q.contains("tier 3") || q.contains("tier-3") || q.contains("off campus") || q.contains("referral") -> {
                "Coming from a tier-2/3 college means cold job applications on LinkedIn will have a sub-5% response rate. You need 'Proof of Work': deploy projects on GitHub with interactive demos, post clean analysis writeups on LinkedIn, and ask senior alumni for warm referrals citing specific repos."
            }
            q.contains("internship") || q.contains("urgent") -> {
                "With placement season approaching, do not start 5 different certifications. Build ONE high-quality project relevant to Indian consumer companies (e.g., analyzing Swiggy/Zepto/Blinkit transaction patterns), format your resume into single-column ATS style, and reach out directly to team leads."
            }
            else -> {
                "Looking at your profile for ${profile.selectedCareer}, your biggest bottleneck is currently SQL (${profile.sql}/10) and lack of industry-grade project deliverables (${profile.completedProjects} completed). Dedicate your ${profile.hoursPerDay} hours/day strictly to execution rather than tutorial-hopping."
            }
        }
    }

    // --- Resume Analyzer ---
    suspend fun analyzeResume(resumeText: String, profile: StudentProfile): ResumeFeedback {
        val prompt = """
            Analyze this Indian engineering student's resume for a target role of ${profile.selectedCareer}.
            
            RESUME CONTENT:
            $resumeText
            
            Provide a strict, practical review in JSON format:
            {
              "readinessScore": 72,
              "summary": "Brief 2-sentence summary of strengths and core issues",
              "matchingKeywords": ["Python", "SQL", "Pandas"],
              "missingKeywords": ["Window Functions", "DAX", "A/B Testing", "CI/CD"],
              "criticalGaps": ["Bullet points lack measurable metrics", "No deployed links"],
              "weakBulletPoints": ["Worked on data analysis project", "Created website using HTML/CSS"],
              "suggestedFixes": ["Rewrite with 'Accomplished X measured by Y using Z'", "Add live GitHub/portfolio links"]
            }
        """.trimIndent()

        val response = geminiService.generateContent(prompt)
        if (response != null) {
            try {
                val clean = response.substringAfter("{").substringBeforeLast("}")
                val json = JSONObject("{$clean}")
                return ResumeFeedback(
                    readinessScore = json.optInt("readinessScore", 68),
                    summary = json.optString("summary", "Resume has foundational technical skills but lacks measurable impact metrics."),
                    matchingKeywords = json.optJSONArray("matchingKeywords")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: listOf("Python", "SQL"),
                    missingKeywords = json.optJSONArray("missingKeywords")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: listOf("Window Functions", "DAX", "Business Metrics"),
                    criticalGaps = json.optJSONArray("criticalGaps")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: listOf("Generic project descriptions", "Missing quantifiable business outcomes"),
                    weakBulletPoints = json.optJSONArray("weakBulletPoints")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: listOf("Responsible for querying database", "Created frontend using React"),
                    suggestedFixes = json.optJSONArray("suggestedFixes")?.let { arr -> List(arr.length()) { arr.getString(it) } } ?: listOf("Use XYZ formula: Accomplished [X] measured by [Y] doing [Z]", "Include live GitHub and demo links")
                )
            } catch (_: Exception) {}
        }

        // Heuristic fallback analyzer
        val lower = resumeText.lowercase()
        var score = 55
        val matched = mutableListOf<String>()
        val missing = mutableListOf<String>()
        val gaps = mutableListOf<String>()

        if (lower.contains("python")) { matched.add("Python"); score += 8 } else missing.add("Python")
        if (lower.contains("sql")) { matched.add("SQL"); score += 10 } else missing.add("SQL")
        if (lower.contains("github") || lower.contains("git")) { matched.add("Git/GitHub"); score += 6 } else gaps.add("Missing GitHub repository links")
        if (lower.contains("%") || lower.contains("improved") || lower.contains("reduced")) { score += 12 } else gaps.add("Bullets lack quantifiable business metrics (e.g. 'reduced latency by 35%')")
        if (!lower.contains("education") || !lower.contains("cgpa")) gaps.add("Academic section needs clear degree, branch, and CGPA")

        return ResumeFeedback(
            readinessScore = min(85, score),
            summary = "Your resume includes relevant programming technologies, but recruiter impact is limited by passive verbs and missing metric quantification.",
            matchingKeywords = matched.ifEmpty { listOf("Engineering coursework") },
            missingKeywords = missing.ifEmpty { listOf("Production Deployment", "DAX / BI", "A/B Testing") },
            criticalGaps = gaps.ifEmpty { listOf("No live demo URLs") },
            weakBulletPoints = listOf(
                "Helped team with SQL queries and data updates",
                "Worked on college machine learning project"
            ),
            suggestedFixes = listOf(
                "Change passive bullet points to active impact: 'Engineered 12 complex SQL queries with CTEs, reducing query runtime by 40%'.",
                "Provide clickable GitHub and live hosted links for every project.",
                "Ensure single-column, cleanly parsed layout without fancy graphics or tables."
            )
        )
    }

    // --- Interview Practice Simulator ---
    suspend fun evaluateInterviewAnswer(
        career: String,
        question: String,
        answer: String
    ): InterviewEvaluation {
        val prompt = """
            Evaluate this interview answer for a candidate targeting: $career.
            
            QUESTION: "$question"
            CANDIDATE ANSWER: "$answer"
            
            Provide strict evaluation in JSON:
            {
              "score": 75,
              "whatWasGood": "Clear explanation of core concept",
              "whatWasMissing": "Did not mention trade-offs or edge cases",
              "betterAnswerStructure": "1. Define concept. 2. Compare alternatives. 3. Cite practical scenario.",
              "followUpQuestion": "How would this behave under a 10x traffic spike?"
            }
        """.trimIndent()

        val ai = geminiService.generateContent(prompt)
        if (ai != null) {
            try {
                val clean = ai.substringAfter("{").substringBeforeLast("}")
                val json = JSONObject("{$clean}")
                return InterviewEvaluation(
                    score = json.optInt("score", 70),
                    whatWasGood = json.optString("whatWasGood", "Good initial conceptual definition"),
                    whatWasMissing = json.optString("whatWasMissing", "Lacked practical edge case handling"),
                    betterAnswerStructure = json.optString("betterAnswerStructure", "Structure into: Definition -> Use Case -> Performance considerations"),
                    followUpQuestion = json.optString("followUpQuestion", "Can you walk through how you would index this query?")
                )
            } catch (_: Exception) {}
        }

        // Heuristic fallback evaluation
        val wordCount = answer.trim().split("\\s+".toRegex()).size
        val score = when {
            wordCount > 60 -> 82
            wordCount > 30 -> 72
            wordCount > 10 -> 58
            else -> 40
        }

        return InterviewEvaluation(
            score = score,
            whatWasGood = if (wordCount > 30) "You communicated the primary mechanism clearly and addressed the main premise." else "Direct to the point.",
            whatWasMissing = if (wordCount < 40) "Answer is too brief for an interview. Technical interviewers look for real-world context, trade-offs, and failure handling." else "Could explicitly mention performance cost and indexing implications.",
            betterAnswerStructure = "1. State direct answer concisely (30s) -> 2. Explain technical mechanism (60s) -> 3. Mention real-world production example or gotcha.",
            followUpQuestion = "In what scenario would this approach cause performance degradation, and how would you optimize it?"
        )
    }

    // --- Recommended Projects Directory ---
    fun getRecommendedProjects(career: String): List<RecommendedProject> {
        return when (career) {
            "Data Analyst" -> listOf(
                RecommendedProject(
                    id = "da_1",
                    title = "Quick Commerce Delivery & Fleet Optimization",
                    domain = "Logistics / E-commerce (Zepto/Blinkit Style)",
                    difficulty = "Intermediate",
                    estimatedWeeks = 3,
                    problemStatement = "Analyze 150,000+ simulated order records to identify hyperlocal delivery delays, peak hours, and rider allocation inefficiencies across 4 metropolitan zones.",
                    skillsDeveloped = listOf("Advanced SQL (CTEs, Window Functions)", "Power BI Interactive Maps", "Data Cleaning", "Geospatial Metrics"),
                    datasetApiRequirements = "Open delivery logistics datasets or Kaggle Indian e-commerce dispatch logs",
                    expectedDeliverables = listOf(
                        "SQL schema with optimized index definitions",
                        "Power BI executive dashboard with delivery time heatmaps",
                        "GitHub repository with architecture documentation and PDF insights deck"
                    ),
                    interviewTalkingPoints = listOf(
                        "How you calculated 90th percentile delivery latencies across dark-store hubs",
                        "Which dark stores had the highest cancellation rate and root cause analysis"
                    )
                ),
                RecommendedProject(
                    id = "da_2",
                    title = "SaaS Customer Churn & Cohort Retention Engine",
                    domain = "B2B SaaS / Product Analytics",
                    difficulty = "Intermediate",
                    estimatedWeeks = 2,
                    problemStatement = "Perform cohort analysis on monthly recurring revenue (MRR) to pinpoint at what tenure churn spikes and determine feature adoption correlation.",
                    skillsDeveloped = listOf("SQL Cohort Queries", "Python Pandas", "Customer Lifetime Value (LTV)", "Retention Curves"),
                    datasetApiRequirements = "Subscription event log CSVs (User ID, event timestamp, subscription tier)",
                    expectedDeliverables = listOf(
                        "SQL monthly cohort retention matrix",
                        "Seaborn heatmap visualizations of user churn",
                        "4 concrete product intervention recommendations"
                    ),
                    interviewTalkingPoints = listOf(
                        "Explaining the difference between gross churn and net revenue churn to interviewers",
                        "How you handled missing subscription downgrade events"
                    )
                ),
                RecommendedProject(
                    id = "da_3",
                    title = "UPI Payment Failure & Gateway Latency Analyzer",
                    domain = "Fintech / Payments",
                    difficulty = "Advanced",
                    estimatedWeeks = 3,
                    problemStatement = "Model UPI transaction settlement success rates across top issuing banks in India to isolate timeout failure patterns and bank downtime anomalies.",
                    skillsDeveloped = listOf("Time Series Analysis", "SQL Window Functions", "Tableau / Power BI", "Anomaly Detection"),
                    datasetApiRequirements = "NPCI public monthly data + simulated transaction logs",
                    expectedDeliverables = listOf(
                        "Real-time latency monitoring dashboard simulation",
                        "Failure classification taxonomy (Technical decline vs Business decline)"
                    ),
                    interviewTalkingPoints = listOf(
                        "Handling asynchronous webhook timeout timestamps",
                        "How to present technical payment failures to non-technical business partners"
                    )
                )
            )
            "Software Developer" -> listOf(
                RecommendedProject(
                    id = "sde_1",
                    title = "High-Throughput URL Shortener with Distributed Caching",
                    domain = "Distributed Systems / Backend",
                    difficulty = "Intermediate",
                    estimatedWeeks = 3,
                    problemStatement = "Build a scalable link shortener handling 10,000 requests/minute using Base62 encoding, Redis caching, and PostgreSQL persistence with connection pooling.",
                    skillsDeveloped = listOf("Java/Spring Boot or Node.js/Go", "Redis Cache-Aside Pattern", "Docker", "Database Indexing"),
                    datasetApiRequirements = "Synthetic load generation using Apache JMeter or k6",
                    expectedDeliverables = listOf(
                        "Fully dockerized microservice with docker-compose",
                        "Latency benchmarks comparing raw DB lookups vs Redis cache hit rates",
                        "Swagger / OpenAPI documentation"
                    ),
                    interviewTalkingPoints = listOf(
                        "How you handled cache stampede and Redis TTL expiration",
                        "Collision avoidance in Base62 generation"
                    )
                ),
                RecommendedProject(
                    id = "sde_2",
                    title = "Collaborative Real-time Task Board (WebSockets)",
                    domain = "Full Stack Web Engineering",
                    difficulty = "Intermediate",
                    estimatedWeeks = 2,
                    problemStatement = "Develop a real-time Trello clone supporting live board synchronization across multiple browser tabs using WebSockets and optimistic UI updates.",
                    skillsDeveloped = listOf("WebSockets", "React / Vue", "Node / Go Backend", "State Management"),
                    datasetApiRequirements = "Custom user and card schemas",
                    expectedDeliverables = listOf(
                        "Deployed web application on Vercel/Render",
                        "Clean unit tests for conflict resolution and reconnect state"
                    ),
                    interviewTalkingPoints = listOf(
                        "Managing disconnected clients and reconciling out-of-order WebSocket messages"
                    )
                )
            )
            else -> listOf(
                RecommendedProject(
                    id = "gen_1",
                    title = "End-to-End Prediction Service with REST API",
                    domain = "Applied Engineering",
                    difficulty = "Intermediate",
                    estimatedWeeks = 3,
                    problemStatement = "Build, evaluate, and package a production-grade problem solver deployed as a RESTful web service with automated tests.",
                    skillsDeveloped = listOf("Python/Java", "REST APIs", "Docker", "Unit Testing"),
                    datasetApiRequirements = "Real-world domain datasets",
                    expectedDeliverables = listOf(
                        "Working Docker container",
                        "Comprehensive README with performance benchmarks"
                    ),
                    interviewTalkingPoints = listOf("Designing for maintainability and graceful error degradation")
                )
            )
        }
    }

    // --- Human Mentors Directory ---
    fun getVerifiedMentors(): List<HumanMentor> {
        return listOf(
            HumanMentor(
                id = "m1",
                name = "Aarav Sharma",
                role = "Senior Data Analyst",
                company = "Swiggy",
                domain = "Data Analytics & Product Growth",
                yearsExperience = 6,
                collegeAlumni = "Tier-2 College Alumni (NIT Surat)",
                expertise = listOf("SQL Optimization", "Power BI / Tableau", "Off-Campus Placement Strategy", "Guesstimates"),
                availability = "Weekends (2 slots remaining)",
                isVerified = true
            ),
            HumanMentor(
                id = "m2",
                name = "Priya Nair",
                role = "Software Development Engineer II",
                company = "Microsoft India",
                domain = "Software Engineering & Backend",
                yearsExperience = 5,
                collegeAlumni = "Tier-3 College to Product-Based MNC",
                expertise = listOf("DSA in Java", "System Design Basics", "Resume Review", "Mock Interviews"),
                availability = "Saturday Evenings",
                isVerified = true
            ),
            HumanMentor(
                id = "m3",
                name = "Rohan Kulkarni",
                role = "Machine Learning Engineer",
                company = "Amazon AWS",
                domain = "ML & Cloud Infrastructure",
                yearsExperience = 7,
                collegeAlumni = "COEP Pune",
                expertise = listOf("MLOps", "Docker/AWS", "Python Production Code", "Graduate Studies / MS"),
                availability = "Sunday Mornings",
                isVerified = true
            ),
            HumanMentor(
                id = "m4",
                name = "Sneha Verma",
                role = "Product Operations & Business Analyst",
                company = "Flipkart",
                domain = "Business Analytics & Operations",
                yearsExperience = 4,
                collegeAlumni = "VIT Vellore",
                expertise = listOf("E-commerce Analytics", "Excel Financial Modeling", "Stakeholder Presentation", "Tier-2/3 Transition"),
                availability = "Weekday Evenings",
                isVerified = true
            )
        )
    }

    // --- Mapper Extensions ---
    private fun StudentProfileEntity.toDomain(): StudentProfile {
        return StudentProfile(
            name = name,
            college = college,
            degree = degree,
            branch = branch,
            currentYear = currentYear,
            currentSemester = currentSemester,
            cgpa = cgpa,
            backlogs = backlogs,
            preferredDomain = preferredDomain,
            codingInterest = codingInterest,
            mathStatsInterest = mathStatsInterest,
            businessInterest = businessInterest,
            preferredWorkType = preferredWorkType,
            preferredLocation = preferredLocation,
            targetSalaryRange = targetSalaryRange,
            python = python,
            java = java,
            cpp = cpp,
            sql = sql,
            excel = excel,
            powerBi = powerBi,
            statistics = statistics,
            machineLearning = machineLearning,
            deepLearning = deepLearning,
            dsa = dsa,
            webDev = webDev,
            cloudComputing = cloudComputing,
            cybersecurity = cybersecurity,
            communication = communication,
            problemSolving = problemSolving,
            gitGithub = gitGithub,
            completedProjects = completedProjects,
            internships = internships,
            certifications = certifications,
            leetcodeProblems = leetcodeProblems,
            hasResume = hasResume,
            hasGithub = hasGithub,
            hoursPerDay = hoursPerDay,
            daysPerWeek = daysPerWeek,
            monthlyBudget = monthlyBudget,
            urgentInternshipNeeded = urgentInternshipNeeded,
            placementSeasonApproaching = placementSeasonApproaching,
            selectedCareer = selectedCareer
        )
    }

    private fun StudentProfile.toEntity(): StudentProfileEntity {
        return StudentProfileEntity(
            id = 1,
            name = name,
            college = college,
            degree = degree,
            branch = branch,
            currentYear = currentYear,
            currentSemester = currentSemester,
            cgpa = cgpa,
            backlogs = backlogs,
            preferredDomain = preferredDomain,
            codingInterest = codingInterest,
            mathStatsInterest = mathStatsInterest,
            businessInterest = businessInterest,
            preferredWorkType = preferredWorkType,
            preferredLocation = preferredLocation,
            targetSalaryRange = targetSalaryRange,
            python = python,
            java = java,
            cpp = cpp,
            sql = sql,
            excel = excel,
            powerBi = powerBi,
            statistics = statistics,
            machineLearning = machineLearning,
            deepLearning = deepLearning,
            dsa = dsa,
            webDev = webDev,
            cloudComputing = cloudComputing,
            cybersecurity = cybersecurity,
            communication = communication,
            problemSolving = problemSolving,
            gitGithub = gitGithub,
            completedProjects = completedProjects,
            internships = internships,
            certifications = certifications,
            leetcodeProblems = leetcodeProblems,
            hasResume = hasResume,
            hasGithub = hasGithub,
            hoursPerDay = hoursPerDay,
            daysPerWeek = daysPerWeek,
            monthlyBudget = monthlyBudget,
            urgentInternshipNeeded = urgentInternshipNeeded,
            placementSeasonApproaching = placementSeasonApproaching,
            selectedCareer = selectedCareer
        )
    }

    private fun RoadmapTaskEntity.toDomain(): WeeklyTask {
        return WeeklyTask(
            id = id,
            weekNumber = weekNumber,
            dayOfWeek = dayOfWeek,
            title = title,
            description = description,
            estimatedMinutes = estimatedMinutes,
            difficulty = difficulty,
            skill = skill,
            isCompleted = isCompleted
        )
    }
}
