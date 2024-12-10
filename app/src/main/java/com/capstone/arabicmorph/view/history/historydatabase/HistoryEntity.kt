package com.capstone.arabicmorph.view.history.historydatabase


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Entity(tableName = "predict_history")
@Parcelize
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo("id")
    val id : Int = 0,

    @field:ColumnInfo("input_text")
    val inputText: String,

    @field:ColumnInfo("prediction")
    val prediction: String,

    @field:ColumnInfo(name = "isSuccess")
    var isSuccess : Boolean
) : Parcelable
