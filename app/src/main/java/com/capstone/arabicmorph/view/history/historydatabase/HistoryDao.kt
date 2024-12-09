package com.capstone.arabicmorph.view.history.historydatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM predict_history  ORDER BY id DESC")
    fun getPredictHistory() : LiveData<List<HistoryEntity>>

    @Delete
    suspend fun deletePredictHistory(history: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPredictHistory(history : List<HistoryEntity>)

    @Query("DELETE FROM predict_history WHERE id = :id")
    suspend fun deletePredictHistoryById(id: Int)
}