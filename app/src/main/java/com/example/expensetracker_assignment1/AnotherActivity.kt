package com.example.expensetracker_assignment1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.time.LocalDate

class month_year{
    var year: Int? = null
    var month: String? = null
}
@RequiresApi(Build.VERSION_CODES.O)
class ThirdActivity :ComponentActivity(){
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var arraylist: MutableList<ListItems> = ArrayList()
        var monthlist: MutableSet<month_year> = HashSet()
        val months = listOf<String>("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER")
        arraylist = retrieveData()

        for (i in 2020 until 2050) {
            for (j in months) {
                var found = false
                for (k in arraylist) {
                    if (k.year == i && k.month == j) {
                        val monthyearobj = month_year().apply {
                            year = i
                            month = j
                        }
                        monthlist.add(monthyearobj)
                        found = true
                        break
                    }
                }

            }
        }
      var sortedmonthlist = monthlist.sortedWith(compareBy({ it.year }, { it.month }))
        setContent {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Aligns items vertically to the center
                horizontalAlignment = Alignment.CenterHorizontally) {



                Text(text = "Selected expense sheet ")
                Text(text = "Year: ${_year.value}")
                Text(text = "Month: ${_month.value}")
                Row {
                Button(onClick = { showMonth_Calendar.value = true }) {
                    Text(text = "Select month and year")
                }
                //Calendar visibility decision is here
                if (showMonth_Calendar.value){
                    monthSelector(
                        _year.value,_month.value){ a, b, c ->
                        if (a) {
                            _year.value = b
                            _month.value = c
                            showMonth_Calendar.value=false

                        }

                    }
                }
                Button(onClick = { finishActivity() },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.Black,disabledContentColor = Color.Black, containerColor = Color.Green)) {
                    Text(text = "Create")
                }}
                if (sortedmonthlist.isNotEmpty()) {
                    LazyColumn {
                        for (i in sortedmonthlist) {
                            item {
                                ListItem(
                                    headlineText = {
                                        Text(text = "${i.month}")
                                    },
                                    leadingContent = { Text(text = "${i.year}") },
                                    trailingContent = {
                                        IconButton(onClick = {
                                            deleteItemByMonthYear(
                                                i.month.toString(),
                                                i.year!!.toInt()
                                            )
                                            finishActivity()
                                        })
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = Color.Red
                                            )
                                        }
                                    },
                                    modifier = Modifier.clickable(
                                        onClick = {
                                            _year.value = i.year!!
                                            _month.value = i.month!!
                                            finishActivity()
                                        }
                                    )
                                )
                            }
                        }
                    }
                }else{
                    Text(text = "No expense sheets")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finishActivity(){
        var return_intent = Intent(Intent.ACTION_VIEW)
        return_intent.putExtra("year",_year.value.toString())
        return_intent.putExtra("month",_month.value)
        setResult(RESULT_OK,return_intent)
        finish()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    var _year = mutableStateOf(LocalDate.now().year.toInt())
    @RequiresApi(Build.VERSION_CODES.O)
    var _month = mutableStateOf(LocalDate.now().month.toString())
    var showMonth_Calendar = mutableStateOf(false) //boolean variable to display dialog to select year and month
    var refresh = mutableStateOf(false)
}
