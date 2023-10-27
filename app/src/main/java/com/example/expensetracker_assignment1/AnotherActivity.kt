package com.example.expensetracker_assignment1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class ThirdActivity :ComponentActivity(){
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Aligns items vertically to the center
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Selected year and month")
                Text(text = "Year: ${_year.value}")
                Text(text = "Month: ${_month.value}")

                Button(onClick = { finishActivity() }) {
                    Text(text = "Apply")
                }
                Text(text = "Click here to select expense sheet Year and Month. Then press apply ")
                Button(onClick = { showMonth_Calendar.value = true }) {
                    Text(text = "Click")
                }
                Text(text = "If sheet is not created previously, it will automatically create an empty sheet for selected month and year", color = Color.LightGray)
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

}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun monthYearSelector(_year: Int, _month:String,dismiss: (bool:Boolean,year:Int,month:String) -> Unit){

    var funyear by remember { mutableStateOf(_year) }
    var funmonth by remember { mutableStateOf(_month) }
    var months = listOf<String>("JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER")

    var yearexpanded by remember { mutableStateOf(false) }
    var monthexpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { dismiss(true,_year,_month)}) {
        Column {
            Row {
                Text(text = "Select Year")
                ExposedDropdownMenuBox(expanded = yearexpanded, onExpandedChange ={yearexpanded = it} ) {
                    TextField(value = funyear.toString(), onValueChange = {}, readOnly = true, modifier = Modifier.menuAnchor())
                    ExposedDropdownMenu(expanded= yearexpanded, onDismissRequest={yearexpanded = false}){
                        for (i in 2020 until 2050 )
                            DropdownMenuItem(text={ Text(i.toString())},onClick={funyear=i; yearexpanded = false})
                    }
                }
            }
            Row {
                Text(text = "Select Month")
                ExposedDropdownMenuBox(expanded = monthexpanded, onExpandedChange ={monthexpanded = it} ) {
                    TextField(value = funmonth.toString(), onValueChange = {}, readOnly = true, modifier = Modifier.menuAnchor())
                    ExposedDropdownMenu(expanded= monthexpanded, onDismissRequest={monthexpanded = false}){
                        for (i in months.indices)
                            DropdownMenuItem(text={ Text(months[i])},onClick={funmonth=months[i]; monthexpanded = false})
                    }
                }
            }
            Row {
                Button(onClick = { dismiss(true,funyear,funmonth) }) {
                    Text(text = "Cancel")
                }
                Button(onClick = { dismiss(true,funyear,funmonth) }) {
                    Text("Ok")
                }
            }
        }
    }
}