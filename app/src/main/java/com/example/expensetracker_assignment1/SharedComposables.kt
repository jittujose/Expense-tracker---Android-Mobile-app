@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expensetracker_assignment1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import java.time.LocalDate

class listItem{
    @RequiresApi(Build.VERSION_CODES.O)
    var itemdate: LocalDate? = null
    var itemName: String? = null
    var itemAmount: Int? = null

}
var arrayList= arrayOf<listItem>()
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
//Add new expense
@Composable
fun addListItem(
    onDismissRequest: () -> Unit,
                ){
    var _itemName by remember { mutableStateOf("") }
    var _itemAmount by remember { mutableStateOf("") }
   Dialog(onDismissRequest = { onDismissRequest() }) {
       Column {
           Row {
               Text(text = "Enter Item")
               TextField(value = _itemName, onValueChange ={ _itemName = it })
           }
           Row {
               Text(text = "Enter Amount")
               TextField(value = _itemAmount,
                   onValueChange = {_itemAmount = it },
                   keyboardOptions = KeyboardOptions(keyboardType=KeyboardType.Number)
               )
           }
           Row {
               Button(onClick = { /*TODO*/ }) {
                   Text(text = "Cancel")
               }
               Button(onClick = {

               }) {
                   Text(text = "Add")
               }
           }
       }
   }

}
//itemdate: LocalDate, itemName: String, itemAmount: Int