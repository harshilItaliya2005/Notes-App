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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

@Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
class MainActivity : ComponentActivity() {
    private lateinit var db: AppDataBase

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                Design()
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun Design() {
        var result by remember { mutableStateOf<List<StoreNotes>>(emptyList()) }

        GlobalScope.launch {
            db = Room.databaseBuilder(
                applicationContext,
                AppDataBase::class.java,
                "storeNotes"
            ).build()

            result = db.notesDao().getAll()
        }
        Scaffold(
            floatingActionButton = { FloatingButtonSection() }
        ) {
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
                    Text(
                        text = "Notes",
                        color = Color.White,
                        fontSize = 55.sp,
                        fontWeight = FontWeight.Light,
                    )
                    Row {
                        FloatingActionButton(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {},
                            modifier = Modifier.padding(end = 10.dp),
                            containerColor = topBtn
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.search),
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        FloatingActionButton(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {},
                            containerColor = topBtn
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.info),
                                contentDescription = "Info",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(result.size) { index ->
                        NotesCard(
                            notesList = result[index]
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun FloatingButtonSection() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(applicationContext, AddNotesPage::class.java)
                    intent.putExtra("notes", StoreNotes::class.java)
                    startActivity(intent)
                },
                modifier = Modifier.size(70.dp),
                shape = CircleShape,
                containerColor = Color.Black
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "Add Note",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }


    @Composable
    fun NotesCard(notesList: StoreNotes) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clickable{
                    val intent = Intent(applicationContext, AddNotesPage::class.java)
                    intent.putExtra("note", notesList)
                    startActivity(intent)
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            border = BorderStroke(3.dp, color = Color.Black),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = notesList.title,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = notesList.notes,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}