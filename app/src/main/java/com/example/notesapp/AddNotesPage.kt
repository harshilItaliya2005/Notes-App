package com.example.notesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.notesapp.DataBase.AppDataBase
import com.example.notesapp.DataBase.table.StoreNotes
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.ui.theme.bgColor
import com.example.notesapp.ui.theme.topBtn
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class AddNotesPage : ComponentActivity() {
    lateinit var db: AppDataBase
    var title = ""
    var note = ""

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        GlobalScope.launch {
            db = Room.databaseBuilder(
                applicationContext, AppDataBase::class.java, "storeNotes"
            ).build()
            setContent {
                NotesAppTheme {

                    val dataNote =
                        intent.getSerializableExtra<StoreNotes>("note", StoreNotes::class.java)
                    Log.d("====", "onCreate: $dataNote")

                    Design(dataNote = dataNote, title = {
                        title = it
                    }, value = {
                        note = it
                    })
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun Design(dataNote: StoreNotes?, title: (String) -> Unit, value: (String) -> Unit) {

        Log.d("------------", "Design: $dataNote")
        val title = remember { mutableStateOf(dataNote?.title ?: "") }
        val note = remember { mutableStateOf(dataNote?.notes ?: "") }
        val expandDialog = remember { mutableStateOf(false) }

        if (expandDialog.value) {
            AlertDialog(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.remove),
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                },
                title = {
                    Text(
                        text = "Delete This Notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.White
                    )
                },
                onDismissRequest = {
                    expandDialog.value = false
                }, confirmButton = {
                    Button(modifier = Modifier.padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = topBtn
                        ),
                        onClick = {
                            expandDialog.value = false
                            GlobalScope.launch {
                                dataNote?.let {
                                    db.notesDao().deleteNotes(it.id)
                                    val remainingNotes = db.notesDao().getAll()
                                    if (remainingNotes.isEmpty()) {
                                        db.notesDao().resetNotesId()
                                    }
                                    val intent =
                                        Intent(applicationContext, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }) {
                        Text(
                            text = "Ok",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }, dismissButton = {
                    Button(modifier = Modifier.padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = topBtn
                        ), onClick = {
                            expandDialog.value = false
                        }) {
                        Text(
                            text = "Cancel",
                            fontSize = 20.sp,
                            color = Color.White
                        )

                    }
                })
        }
        Scaffold {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = bgColor)
                    .padding(it)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    FloatingActionButton(
                        onClick = {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, shape = RoundedCornerShape(10.dp), containerColor = topBtn
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    Row {
                        FloatingActionButton(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {},
                            modifier = Modifier.padding(end = 10.dp),
                            containerColor = topBtn
                        ) {
                            Icon(painter = painterResource(R.drawable.remove),
                                contentDescription = "Remove",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(25.dp)
                                    .clickable {
                                        expandDialog.value = true
                                    })
                        }

                        FloatingActionButton(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {},
                            modifier = Modifier.padding(end = 10.dp),
                            containerColor = topBtn
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.save),
                                contentDescription = "Remove",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        }


                    }
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(25.dp))
                    TextField(
                        value = title.value,
                        onValueChange = {
                            title.value = it
                            title(it)
                        },
                        placeholder = { Text("Title", fontSize = 40.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 40.sp, color = Color.White),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = note.value,
                        onValueChange = {
                            note.value = it
                            value(it)
                        },
                        placeholder = { Text("Type something...", fontSize = 20.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("-------------", "Check: $title $note")
        GlobalScope.launch {
            var status = intent.getStringExtra("status")
            val noteData = intent.getSerializableExtra("note") as? StoreNotes
            Log.d("------------", "onBackPressed: $status")

            if (title.isNotEmpty() && note.isNotEmpty()) {
                val saveNote = StoreNotes(
                    title = title,
                    notes = note
                )
                try {
                    if (status == "add") {
                        db.notesDao().insert(saveNote)
                    } else if (status == "edit") {
                        noteData?.let {
                            db.notesDao().updateNotes(it.id, saveNote.title, saveNote.notes)
                        }

                    }
                } catch (e: Exception) {
                    Log.d("------------", "onBackPressed Error: $e")
                }
            }
        }
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }
}

