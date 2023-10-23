package com.example.expensetracker_assignment1

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
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
    var amount: Int? =null
    var budget: Int?=null

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
                    addListItem (expense_year.value,expense_month.value, expense_day.value,budget.value){
                        if (it == false){
                            showAdd_list.value = false}
                    }
                }
                arraylist = retrieveData()
                if(arraylist.isNotEmpty()){
                verticalList(items = arraylist, onItemClick = {it}) }
            }


        }
    }

    //override onResume function which we will use to retrieve data and update the displayed string
    override fun onResume() {
        super.onResume()

//        var arraylist: MutableList<ListItems> = ArrayList()
//        arraylist = retrieveData()
        // current_data.value = retrieveData()
    }

    //overridden onStop function which we will use to add and update data in the database
    override fun onStop() {
        super.onStop()

//        addData("Ufdgd","2022","JHF","22","25","300")
//        addData("UHfbd","2022","JHF","22","25","300")
//        addData("llkl","2022","JHF","22","25","300")
//        addData("UHfbd","2022","JHF","22","25","300")
        //updateData(15,20)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    var current_date = mutableStateOf(LocalDate.now())
    @RequiresApi(Build.VERSION_CODES.O)
    var expense_year = mutableStateOf( LocalDate.now().year.toInt())
    @RequiresApi(Build.VERSION_CODES.O)
    var expense_month = mutableStateOf(LocalDate.now().month.toString())
    @RequiresApi(Build.VERSION_CODES.O)
    var expense_day = mutableStateOf(LocalDate.now().dayOfMonth.toInt())
    var budget = mutableStateOf(0)

    var total_expense = mutableStateOf(0)
    var balance = mutableStateOf(0)
    //var last_clicked = mutableStateOf(listItem())
    var showAdd_list = mutableStateOf(false) // boolean variable to display add list dialog
    var showEdit_Budget = mutableStateOf(false) // boolean variable to display edit budget dialog
    var showMonth_Calendar = mutableStateOf(false) //boolean variable to display calendar view



}

//      database operations
// function that will add some data to our database
 fun addData(ITEM_NAME: String,YEAR:String,MONTH:String,DAY:String,AMOUNT:String,BUDGET:String) {
    //we will add in three rows of data to the database using the content values objects
    val row1: ContentValues = ContentValues().apply {
        put("ITEM_NAME", ITEM_NAME)
        put("YEAR", YEAR)
        put("MONTH", MONTH)
        put("DAY", DAY)
        put("AMOUNT", AMOUNT)
        put("BUDGET",BUDGET)

    }

    //add all three rows to the database
    sdb.insert("test", null, row1)

}

// function that will update some data in our database
 fun updateData(newitemAmount:Int,olditemAmount:Int) {
    val row: ContentValues = ContentValues().apply {
        put("ITEM_AMOUNT", newitemAmount)
    }
    //generate a query to pick out the rows with jim as the first name so we can change it to jacob
    var table: String = "test"
    var where: String = "ITEM_AMOUNT = ?"
    var where_args: Array<String> = arrayOf(olditemAmount.toString())
    sdb.update(table, row, where, where_args)
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
        list.amount=c.getString(5).toInt()
        list.budget=c.getString(6).toInt()
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
