package com.example.expensetracker_assignment1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { }, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                        Text("${expense_date.value.month}")
                    }
                }
                Row (
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    Arrangement.SpaceEvenly

                ){
                    Column( verticalArrangement = Arrangement.SpaceEvenly) {
                        Text("${total_expense.value}", modifier = Modifier,Color.Red,textAlign = TextAlign.Center)
                        Text("Total Expense",textAlign = TextAlign.Center)
                    }

                    Column (verticalArrangement = Arrangement.SpaceEvenly){
                        Text("${balance.value}", modifier = Modifier, Color.Blue,textAlign = TextAlign.Center)
                        Text("Balance", modifier = Modifier, textAlign = TextAlign.Center)
                    }
                }

                    Button(onClick = {},
                        Modifier
                            .align(End)
                            .padding(end = 20.dp),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.Black,disabledContentColor = Color.Black, containerColor = Color.Green))
                                {
                        Text(text = "Edit Budget")

                }
                if (expense_date.value.month == LocalDate.now().month ){
                    FloatingActionButton(onClick = {  show_Dialog.value = true},
                        Modifier
                            .align(End)
                            .padding(end = 20.dp, top = 10.dp)) {
                        Icon(Icons.Filled.Add,contentDescription = "Add Expense", tint = Color.Red) }
                }
                //composable functions cannot be called from within onClicks from either a button or a modifier.
                //So we use a flag in side the Floating Button click and let the flag to decide about viewing add item dialogue
                if (show_Dialog.value){
                    addListItem {}
                }
                expenseList (expense_date) {last_clicked.value = it}
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    var expense_date = mutableStateOf(LocalDate.now())
    var total_expense = mutableStateOf(0)
    var balance = mutableStateOf(0)
    var last_clicked = mutableStateOf(listItem())
    var show_Dialog = mutableStateOf(false)

}
