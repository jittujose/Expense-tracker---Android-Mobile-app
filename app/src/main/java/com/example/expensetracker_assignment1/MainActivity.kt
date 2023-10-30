package com.example.expensetracker_assignment1

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate


class ListItems{
    var itemID: Int? =null
    var itemName: String? = null
    var year: Int? = null
    var month: String? = null
    var day: Int? = null
    var amount: Float? =null
    var budget: Float?=null

}
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var arraylist: MutableList<ListItems> = ArrayList()

        //get a writable connection to the database
        tdb = ExptrackerDBOpenHelper(this, "test.db", null, 1)
        sdb = tdb.writableDatabase
        setContent {
            var launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){
                //Getting data from AnotherActivity.
                third_year.value=it.data?.getStringExtra("year").toString()
                third_month.value=it.data?.getStringExtra("month").toString()
                expense_year.value=third_year.value.toInt()
                expense_month.value=third_month.value
            }
            Column {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    horizontalArrangement = Arrangement.Center
                ) {
                    //Button for selecting date to display monthly expense
                    Button(onClick = {
                        launcher.launch(createIntentThirdActivity())

                                     }, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                        Text("${expense_year.value}  ${expense_month.value}")
                    }
                }
                Row (
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray),
                    Arrangement.SpaceEvenly

                ){//Column to display total expense of selected month
                    Column( verticalArrangement = Arrangement.SpaceEvenly) {
                        Text("\u20AC ${String.format("%.2f",total_expense.value)}", modifier = Modifier,Color.Red,textAlign = TextAlign.Center)
                        Text("Total Expense",textAlign = TextAlign.Center)
                    }
                            //Column to display balance amount for the selected month.
                    Column (verticalArrangement = Arrangement.SpaceEvenly){
                        Text("\u20AC ${String.format("%.2f",balance.value)}", modifier = Modifier, Color.Blue,textAlign = TextAlign.Center)
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
                    editBudget(expense_month.value,expense_year.value.toString(),budget.value){a,b->
                        if (a){
                            showEdit_Budget.value=false
                        }
                        secondary_budget.value=b.toFloat()
                    }
                }
                //Add expense button is declared here
                

                    FloatingActionButton(onClick = {  showAdd_list.value = true},
                        Modifier
                            .align(CenterHorizontally)
                            ) {
                        Icon(Icons.Filled.Add,contentDescription = "Add Expense", tint = Color.Red) }

                //composable functions cannot be called from within onClicks from either a button or a modifier.
                //So we use a flag in side the Floating Button click and let the flag to decide about viewing add item dialogue
                if (showAdd_list.value){
                    addListItem (expense_year.value,expense_month.value, expense_day.value,budget.value){
                        if (it == false){
                            showAdd_list.value = false}
                    }
                }
                arraylist = retrieveData() //retrieving data from database
                //Displaying vertical list of expenses of selected month
                if(arraylist.isNotEmpty()){
                verticalList(items = arraylist,expense_month.value,expense_year.value,onItemClick = {item ->
                    selectedListItem.value=item
                    showEditListItem.value=true

                }){
                    a,b,c->
                    total_expense.value = a
                    budget.value= b
                    if(c){
                        budget.value = secondary_budget.value
                    }
                    balance.value=budget.value - total_expense.value
                    listempty.value=c
                } }
                if (showEditListItem.value){
                    editListItem(
                        itemID = selectedListItem.value.itemID!!.toInt(),
                        currentItemName = selectedListItem.value.itemName!!.toString(),
                        currentItemAmount =selectedListItem.value.amount!!.toFloat() ,

                        ){showEditListItem.value=it}}
                //Checking list is empty or not.
                if (listempty.value){
                    Text(text = "No expense in this month. Press + button to add new expense")

                }
            }


        }


    }


@RequiresApi(Build.VERSION_CODES.O)
fun createIntentThirdActivity():Intent {
    var intent:Intent = Intent(this,ThirdActivity::class.java)
    return intent
}


    @RequiresApi(Build.VERSION_CODES.O)
    var third_year = mutableStateOf(LocalDate.now().year.toString())//Used for returned year from AnotherActivity
    @RequiresApi(Build.VERSION_CODES.O)
    var third_month = mutableStateOf(LocalDate.now().month.toString())//Used for returned month from AnotherActivity


    @RequiresApi(Build.VERSION_CODES.O)
    var expense_year = mutableStateOf( LocalDate.now().year.toInt())
    //Integer variable used to store selected year for the list and initialised to current year
    @RequiresApi(Build.VERSION_CODES.O)
    var expense_month = mutableStateOf(LocalDate.now().month.toString())
    //String variable used to store selected month for the list and initialised to current month
    @RequiresApi(Build.VERSION_CODES.O)
    var expense_day = mutableStateOf(LocalDate.now().dayOfMonth.toInt())
    //Integer variable used to store current day for the list
    var budget = mutableStateOf(0f)
    //Floating point variable used to store budget of the selected month
    var total_expense = mutableStateOf(0f)
    //floating point variable used to store total expense of selected month
    var balance = mutableStateOf(0f)
    //floating point variable used to store balance amount of selected month
    var secondary_budget = mutableStateOf(0f)// used to set budget if the list is empty. Value accepts from function editBudget().

    // Initialize a mutable state variable with a default instance of ListItems
    var initialListItems = ListItems()

    // Create a mutable state with the initial value
    var selectedListItem: MutableState<ListItems> = mutableStateOf(initialListItems)



    var showAdd_list = mutableStateOf(false) // boolean variable to display add list dialog
    var showEdit_Budget = mutableStateOf(false) // boolean variable to display edit budget dialog
    var showMonth_Calendar = mutableStateOf(false) //boolean variable to display dialog to select year and month
    var showEditListItem = mutableStateOf(false) //boolean variable used to display dialog to edit List Item
    var listempty = mutableStateOf(false)// boolean for checking list is empty or not


}

//                     database operations

// function that will add new item data to our database
 fun addData(ITEM_NAME: String,YEAR:String,MONTH:String,DAY:String,AMOUNT:String,BUDGET:String) {
    //we will add in one row of data to the database using the content values objects
    val row1: ContentValues = ContentValues().apply {
        put("ITEM_NAME", ITEM_NAME)
        put("YEAR", YEAR)
        put("MONTH", MONTH)
        put("DAY", DAY)
        put("AMOUNT", AMOUNT)
        put("BUDGET",BUDGET)

    }

    //add our row to the database
    sdb.insert("test", null, row1)

}

// function that will update list item amount and item name in our database
 fun updateItemData(itemID:Int,newItemName:String,oldItemName:String,newitemAmount:Float,olditemAmount:Float) {
    val row: ContentValues = ContentValues().apply {
        put("ITEM_NAME", newItemName)
        put("AMOUNT", newitemAmount.toString())
    }
    //generate a query to pick out the rows with jim as the first name so we can change it to jacob
    var table: String = "test"
    var where: String = "ID = ?"
    var where_args: Array<String> = arrayOf(itemID.toString())
    sdb.update(table, row, where, where_args)
}
//function that will update budget in database for selected month
fun updateBudget(newBudget:Float,oldBudget:Float,month:String,year: String) {
    val row: ContentValues = ContentValues().apply {
        put("BUDGET", newBudget.toString())
    }
    //generate a query to pick out the rows with month
    var table: String = "test"
    var where: String = "MONTH = ? AND YEAR =?"
    var where_args:Array<String> = arrayOf(month,year)
    sdb.update(table, row, where, where_args)
}

//function to delete row from database table test
fun deleteItemByID(itemId: Int) {


    // Define the table and the condition for deletion
    val table = "test"
    val whereClause = "ID = ?"
    val whereArgs = arrayOf(itemId.toString())

    // Execute the DELETE statement
    sdb.delete(table, whereClause, whereArgs)


}
//private function that will retrieve the full data from our database and return it as a string
fun retrieveData(): MutableList<ListItems> {
    // the name of the table we are going to query
    val table_name: String = "test"
    //columns of the table we are going to retrieve
    val columns: Array<String> = arrayOf("ID", "ITEM_NAME", "YEAR","MONTH","DAY", "AMOUNT","BUDGET")
    //where clause of query
    val where: String? = null
    //arguments to provide to the where clause
    val where_args: Array<String>? = null
    //group by clause of query
    val group_by: String? = null
    //having clause of the query
    val having: String? = null
    //order by clause of the query
    val order_by: String? = null



    // Create a list to store the retrieved data
    val arraylist: MutableList<ListItems> = ArrayList()

    //run the query which will return a cursor object that we can use to iterate through the
    //returned rows from the query
    var c: Cursor =
        sdb.query(table_name, columns, where, where_args, group_by, having, order_by)


    // Iterate through the cursor's results
    while (c.moveToNext()) {
        // Create a new ListItems instance for each row
        val list = ListItems()

        list.itemID =c.getInt(0).toInt()
        list.itemName=c.getString(1).toString()

        list.year=c.getString(2).toInt()

        list.month=c.getString(3).toString()

        list.day=c.getString(4).toInt()
        list.amount=c.getString(5).toFloat()
        list.budget=c.getString(6).toFloat()
        arraylist.add(list)

    }
    // Close the cursor to release resources
    c.close()
    //return the constructed arraylist
    return arraylist
}



//the test db open helper we have defined that will be used to open the database
//we will initialise this in the onCreate method hence lateinit
private lateinit var tdb: ExptrackerDBOpenHelper

//the database itself that we will get after opening it . this is what we will use to store
//our SQL based data. also lateinit as we will initialise this in onCreate
private lateinit var sdb: SQLiteDatabase
