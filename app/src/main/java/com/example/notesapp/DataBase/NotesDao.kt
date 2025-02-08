package com.example.notesapp.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notesapp.DataBase.table.StoreNotes

@Dao
interface NotesDao {

    @Insert
    fun insert(storeNotes: StoreNotes)

    @Query("SELECT * FROM notes")
    fun getAll(): List<StoreNotes>

    @Query("UPDATE notes SET notes = :notes, title = :title WHERE id = :id")
    fun updateNotes(id: Int, title: String, notes: String)



    @Query("DELETE FROM notes WHERE id = :id")
    fun deleteNotes(id: Int)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'notes'")
    fun resetNotesId()
}