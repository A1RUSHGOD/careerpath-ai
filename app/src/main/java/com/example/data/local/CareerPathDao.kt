package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CareerPathDao {

    @Query("SELECT * FROM student_profiles WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<StudentProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: StudentProfileEntity)

    @Query("SELECT * FROM roadmap_tasks ORDER BY monthNumber ASC, weekNumber ASC, id ASC")
    fun getTasks(): Flow<List<RoadmapTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<RoadmapTaskEntity>)

    @Query("UPDATE roadmap_tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Long, isCompleted: Boolean)

    @Query("DELETE FROM roadmap_tasks")
    suspend fun clearTasks()

    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatMessages()

    @Query("SELECT * FROM interview_sessions ORDER BY timestamp DESC")
    fun getInterviewSessions(): Flow<List<InterviewSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterviewSession(session: InterviewSessionEntity)
}
