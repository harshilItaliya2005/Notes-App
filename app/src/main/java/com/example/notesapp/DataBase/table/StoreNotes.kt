package com.example.notesapp.DataBase.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class StoreNotes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "Title") var title: String,
    @ColumnInfo(name = "Notes") var notes: String,
) : Serializable
