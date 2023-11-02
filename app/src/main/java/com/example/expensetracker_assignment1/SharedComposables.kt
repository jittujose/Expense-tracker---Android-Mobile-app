@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expensetracker_assignment1

import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import java.util.Calendar

//This function is used to display the vertical scrollable list in the main interface.
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun verticalList(items: List<ListItems>,month: String,year: Int, onItemClick: (ListItems) -> Unit,totalandbudjet:(Float,Float,Boolean)->Unit) {
    LazyColumn {
        if (items.isNotEmpty()){
            var totalAmount =0f
            var budgetAmount = 0f
            var isempty = true
            for (i in items) {
                if (i.month == month && i.year==year){
                    totalAmount += (i.amount ?: 0f).toFloat()
                    budgetAmount = (i.budget ?: 0f).toFloat()
                    isempty = false
                    item {
                        ListItem(
                            modifier = Modifier.clickable {
                                // Call the onItemClick callback when the item is clicked
                                onItemClick(i)
                            },
                            headlineText = {
                                Text(text = i.itemName ?: "No item name")
                            },
                            supportingText = {
                                Row {
                                    Text(text = "Day: ")
                                    Text(text = i.day?.toString() ?: "No day")
                                }
                                
                            },
                            trailingContent = {
                                Row {
                                    Text(text = "\u20AC")
                                    Text(text = i.amount?.toString() ?: "No Amount",
                                        fontSize = 20.sp)
                                }

                            }



                        )
                    }

            }

            }

            totalandbudjet(totalAmount,budgetAmount,isempty)
        }
    }
}

//Function to add new expense is declared here
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addListItem(expense_year:Int,expense_month:String,expense_day:Int,budget:Float,
    onDismissRequest: (Boolean) -> Unit,
                ){
    var _itemName by remember { mutableStateOf("") }
    var _itemAmount by remember { mutableStateOf("") }
   Dialog(onDismissRequest = { onDismissRequest(false) }) {
       Column {
               TextField(value = _itemName, onValueChange ={ _itemName = it }, label = { Text(text = "Enter Item")})
               TextField(value = _itemAmount,
                   onValueChange = {_itemAmount = it },
                   keyboardOptions = KeyboardOptions(keyboardType=KeyboardType.Number),
                   label = { Text(text = "Enter Amount")}
               )

           Row {
               Button(onClick = { onDismissRequest(false)

               }) {
                   Text(text = "Cancel")
               }
               Button(onClick = {
                   if (_itemName.isNotEmpty() && _itemAmount.isNotEmpty()){
                   addData(ITEM_NAME = _itemName,expense_year.toString(),expense_month,expense_day.toString(),_itemAmount.toString(),budget.toString())
                   onDismissRequest(false)}
               }) {
                   Text(text = "Add")
               }
           }



       }
   }

}




//Function for edit Budjet
@Composable
fun editBudget(month: String,year: String,currentBudget:Float,dismiss: (Boolean,String) ->Unit){
    var _budget by remember { mutableStateOf("${currentBudget}") }

    Dialog(onDismissRequest = { dismiss(true,_budget) }) {
        Column {
            Row {
                TextField(value = _budget, onValueChange = { _budget = it },
                    keyboardOptions = KeyboardOptions(keyboardType=KeyboardType.Number),
                    label = { Text(text = "Enter Budget")})
            }
            Row {
                Button(onClick = { dismiss(true,_budget) }) {
                    Text(text = "Cancel")
                }
                Button(onClick = {
                    updateBudget(_budget.toFloat(),currentBudget,month,year)
                    dismiss(true,_budget)
                }) {
                    Text(text = "Ok")
                }
            }
        }
    }

}

//Function for month selector
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun monthSelector(_year: Int, _month:String,dismiss: (bool:Boolean,year:Int,month:String) -> Unit){

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

//function to edit list item
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editListItem(itemID:Int,currentItemName:String,currentItemAmount: Float,
                onDismissRequest: (Boolean) -> Unit,
){
    var _itemName by remember { mutableStateOf("$currentItemName") }
    var _itemAmount by remember { mutableStateOf("$currentItemAmount") }
    Dialog(onDismissRequest = { onDismissRequest(false) }) {
        Column {
            TextField(value = _itemName, onValueChange ={ _itemName = it }, label = { Text(text = "Enter Item")})
            TextField(value = _itemAmount,
                onValueChange = {_itemAmount = it },
                keyboardOptions = KeyboardOptions(keyboardType=KeyboardType.Number),
                label = { Text(text = "Enter Amount")}
            )

            Row {
                Button(onClick = {
                    deleteItemByID(itemID)
                    onDismissRequest(false)

                }) {
                    Text(text = "Delete")
                }
                Button(onClick = { onDismissRequest(false)

                }) {
                    Text(text = "Cancel")
                }
                Button(onClick = {
                    if(_itemName.isNotEmpty() && _itemAmount.isNotEmpty()){
                    updateItemData(itemID,_itemName,currentItemName,_itemAmount.toFloat(),currentItemAmount)
                    onDismissRequest(false)}
                }) {
                    Text(text = "Ok")
                }
            }
        }
    }

}

