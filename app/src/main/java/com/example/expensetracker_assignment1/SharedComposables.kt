@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expensetracker_assignment1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog


@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun verticalList(items: List<ListItems>, onItemClick: (ListItems) -> Unit) {
    LazyColumn {
        if (items.isNotEmpty()){
            for (i in items) {
                item {
                    ListItem(
                        headlineText = {
                            Text(text = i.itemName ?: "No item name")
                        },
                        supportingText = {
                            Text(text = i.day?.toString() ?: "No day")
                        },
                        trailingContent = {
                            Text(text = i.amount?.toString() ?: "No Amount")
                        }
//                modifier = Modifier.clickable {
//                    onItemClick(i)
//                }
                    )
                }
            }
        }
    }
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun expenseList(expense_date: MutableState<LocalDate>, onStateChanged:(listItem)->Unit){
    if (arrayList.isEmpty()){
        Text(text = "You don't have any Expense in this month")
    }else {
        LazyColumn {
            for (i in arrayList) {
                if (expense_date.value.month == i.itemdate?.month) {
                    item {
                        ListItem(headlineText = { Text(text = "${i.itemName}") },
                            leadingContent = { Text(text = "${i.itemdate}") },
                            trailingContent = { Text(text = "${i.itemAmount}") },
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    onStateChanged(i)
                                }
                            ))
                    }
                }
            }
        }
    }
}

 */
//Function to add new expense is declared here
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addListItem(expense_year:Int,expense_month:String,expense_day:Int,budget:Int,
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
                   addData(ITEM_NAME = _itemName,expense_year.toString(),expense_month,expense_day.toString(),_itemAmount.toString(),budget.toString())
                   onDismissRequest(false)
               }) {
                   Text(text = "Add")
               }
           }
       }
   }

}
//Function for edit Budjet
@Composable
fun editBudget(dismiss: (Boolean) ->Unit){
    var _budget by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { dismiss(true) }) {
        Column {
            Row {
                TextField(value = _budget, onValueChange = { _budget = it },
                    keyboardOptions = KeyboardOptions(keyboardType=KeyboardType.Number),
                    label = { Text(text = "Enter Budget")})
            }
            Row {
                Button(onClick = { dismiss(true) }) {
                    Text(text = "Cancel")
                }
                Button(onClick = { /*TODO*/ }) {
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



