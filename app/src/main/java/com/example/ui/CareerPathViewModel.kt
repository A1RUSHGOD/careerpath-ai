package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.ChatMessageEntity
import com.example.data.model.*
import com.example.data.repository.CareerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CareerPathUiState(
    val profile: StudentProfile = StudentProfile(),
    val recommendations: List<CareerRecommendation> = emptyList(),
    val skillGaps: List<SkillGapItem> = emptyList(),
    val tasks: List<WeeklyTask> = emptyList(),
    val chatMessages: List<ChatMessageEntity> = emptyList(),
    val isMentorThinking: Boolean = false,
    val resumeFeedback: ResumeFeedback? = null,
    val isAnalyzingResume: Boolean = false,
    val whatIfScenario: WhatIfScenario? = null,
    val interviewEvaluation: InterviewEvaluation? = null,
    val isEvaluatingInterview: Boolean = false,
    val adaptationMessage: String? = null,
    val currentStreakDays: Int = 5,
    val verifiedMentors: List<HumanMentor> = emptyList(),
    val recommendedProjects: List<RecommendedProject> = emptyList()
)

class CareerPathViewModel(
    private val repository: CareerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CareerPathUiState())
    val uiState: StateFlow<CareerPathUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                verifiedMentors = repository.getVerifiedMentors()
            )
        }

        // Observe profile changes from Room
        viewModelScope.launch {
            repository.studentProfile.collect { profile ->
                val recommendations = repository.assessCareers(profile)
                val gaps = repository.getSkillGapsForCareer(profile, profile.selectedCareer)
                val projects = repository.getRecommendedProjects(profile.selectedCareer)
                val whatIf = repository.simulateScenario(
                    hoursPerDay = profile.hoursPerDay,
                    durationMonths = 6,
                    projectsCount = profile.completedProjects,
                    hasInternship = profile.internships > 0,
                    targetCareer = profile.selectedCareer,
                    baseProfile = profile
                )

                _uiState.update { current ->
                    current.copy(
                        profile = profile,
                        recommendations = recommendations,
                        skillGaps = gaps,
                        recommendedProjects = projects,
                        whatIfScenario = whatIf
                    )
                }
            }
        }

        // Observe tasks
        viewModelScope.launch {
            repository.tasks.collect { taskList ->
                // If tasks are empty, seed initial roadmap for the profile
                if (taskList.isEmpty()) {
                    repository.generateRoadmapForCareer(_uiState.value.profile, _uiState.value.profile.selectedCareer)
                } else {
                    _uiState.update { it.copy(tasks = taskList) }
                }
            }
        }

        // Observe chat messages
        viewModelScope.launch {
            repository.chatMessages.collect { messages ->
                _uiState.update { it.copy(chatMessages = messages) }
            }
        }
    }

    fun loadDemoProfile() {
        viewModelScope.launch {
            repository.loadDemoProfile()
        }
    }

    fun saveProfile(updated: StudentProfile) {
        viewModelScope.launch {
            repository.saveProfile(updated)
            repository.generateRoadmapForCareer(updated, updated.selectedCareer)
        }
    }

    fun selectTargetCareer(careerName: String) {
        viewModelScope.launch {
            repository.selectTargetCareer(careerName, _uiState.value.profile)
        }
    }

    fun toggleTask(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(taskId, isCompleted)
        }
    }

    fun adaptRoadmap(reason: String) {
        viewModelScope.launch {
            val message = repository.adaptRoadmap(_uiState.value.profile, reason)
            _uiState.update { it.copy(adaptationMessage = message) }
        }
    }

    fun clearAdaptationMessage() {
        _uiState.update { it.copy(adaptationMessage = null) }
    }

    fun runWhatIfSimulation(hoursPerDay: Int, durationMonths: Int, projectsCount: Int, hasInternship: Boolean) {
        val scenario = repository.simulateScenario(
            hoursPerDay = hoursPerDay,
            durationMonths = durationMonths,
            projectsCount = projectsCount,
            hasInternship = hasInternship,
            targetCareer = _uiState.value.profile.selectedCareer,
            baseProfile = _uiState.value.profile
        )
        _uiState.update { it.copy(whatIfScenario = scenario) }
    }

    fun askMentor(question: String) {
        if (question.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isMentorThinking = true) }
            repository.askMentor(question, _uiState.value.profile)
            _uiState.update { it.copy(isMentorThinking = false) }
        }
    }

    fun analyzeResume(resumeText: String) {
        if (resumeText.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzingResume = true) }
            val feedback = repository.analyzeResume(resumeText, _uiState.value.profile)
            _uiState.update { it.copy(isAnalyzingResume = false, resumeFeedback = feedback) }
        }
    }

    fun evaluateInterview(career: String, question: String, answer: String) {
        if (answer.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isEvaluatingInterview = true) }
            val evaluation = repository.evaluateInterviewAnswer(career, question, answer)
            _uiState.update { it.copy(isEvaluatingInterview = false, interviewEvaluation = evaluation) }
        }
    }
}

class CareerPathViewModelFactory(
    private val repository: CareerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CareerPathViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CareerPathViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
