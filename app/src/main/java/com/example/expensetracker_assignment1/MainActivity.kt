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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

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
                    //Button for selecting date to display monthly expense
                    Button(onClick = { showMonth_Calendar.value = true}, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                        Text("${expense_year.value}  ${expense_month.value}")
                    }
                    //Calendar visibility decision is here
                    if (showMonth_Calendar.value){
                        monthSelector(
                            expense_year.value,expense_month.value){ a, b, c ->
                            if (a) {
                                expense_year.value = b
                                expense_month.value = c
                                 showMonth_Calendar.value=false

                            }

                        }
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
                //Edit budget button is declared here
                    Button(onClick = {showEdit_Budget.value=true},
                        Modifier
                            .align(End)
                            .padding(end = 20.dp),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.Black,disabledContentColor = Color.Black, containerColor = Color.Green))
                                {
                        Text(text = "Edit Budget")

                }
                //edit budget dialog visibility declared her
                if (showEdit_Budget.value){
                    editBudget(){
                        if (it){
                            showEdit_Budget.value=false
                        }
                    }
                }
                //Add expense button is declared here
                //It will visible only if the month selected is the current month
                if (expense_month.value == LocalDate.now().month.toString() && expense_year.value ==LocalDate.now().year  ){
                    FloatingActionButton(onClick = {  showAdd_list.value = true},
                        Modifier
                            .align(End)
                            .padding(end = 20.dp, top = 10.dp)) {
                        Icon(Icons.Filled.Add,contentDescription = "Add Expense", tint = Color.Red) }
                }
                //composable functions cannot be called from within onClicks from either a button or a modifier.
                //So we use a flag in side the Floating Button click and let the flag to decide about viewing add item dialogue
                if (showAdd_list.value){
                    addListItem (){
                        if (it == false){
                            showAdd_list.value = false}
                    }
                }
                expenseList (current_date) {last_clicked.value = it}
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    var current_date = mutableStateOf(LocalDate.now())
    @RequiresApi(Build.VERSION_CODES.O)
    var expense_year = mutableStateOf( LocalDate.now().year.toInt())
    @RequiresApi(Build.VERSION_CODES.O)
    var expense_month = mutableStateOf(LocalDate.now().month.toString())

    var total_expense = mutableStateOf(0)
    var balance = mutableStateOf(0)
    var last_clicked = mutableStateOf(listItem())
    var showAdd_list = mutableStateOf(false) // boolean variable to display add list dialog
    var showEdit_Budget = mutableStateOf(false) // boolean variable to display edit budget dialog
    var showMonth_Calendar = mutableStateOf(false) //boolean variable to display calendar view
}
