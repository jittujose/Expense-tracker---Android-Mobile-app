package com.example.expensetracker_assignment1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//class that will be responsible for maintaining our applications SQLite database
class ExptrackerDBOpenHelper(context: Context,name:String, factory: SQLiteDatabase.CursorFactory?,version: Int)
    :SQLiteOpenHelper(context,name,factory,version)
{
    //function that is called to create the database for the application if it has not been created before
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }
    //function that will be called if the database needs to be upgraded or adapted to anew schema
    //here, we drop the tables and recreate them
    override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //db?.execSQL(DROP_TABLE)
        //db?.execSQL(CREATE_TABLE)
    }

    //private constant string for creating a single table in our database
    private val CREATE_TABLE: String = "create table test("+
            "ID integer primary key autoincrement,"+
            "ITEM_NAME string,"+
            "YEAR string,"+
            "MONTH string,"+
            "DAY string,"+
            "AMOUNT string,"+
            "BUDGET string"+
            ")"

    //private constant string for dropping our single table. we will need this when upgrading tables
    private val DROP_TABLE: String = "drop table test"
}