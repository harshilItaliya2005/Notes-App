package com.example.notesapp.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notesapp.DataBase.table.StoreNotes

@Database(entities = [StoreNotes::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun notesDao() : NotesDao


}
