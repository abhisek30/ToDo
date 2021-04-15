package com.example.todo.data

import android.content.Context
import androidx.room.*
import com.example.todo.data.models.ToDoData

//Add annotation of database to get notified by ROOM
//specify entities parameter ,version,exportSchema (for Version History of database)
//Make class as abstract and extend from RoomDatabase
@Database(entities = [ToDoData::class],version = 1,exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase :RoomDatabase(){

    abstract fun todoDao():ToDoDao

    //to directly get reference of function in this to other classes use companion object
    companion object{
        //adding volatile to writes to this fields are immediately made visible to other threads.
        @Volatile
        private var INSTANCE : ToDoDatabase? = null

        //to have only one instance of database class
        fun getDatabase(context:Context):ToDoDatabase{
            val tempInstance = INSTANCE
            //check weather instance is already present or not null, if not null return same instance
            if(tempInstance != null){
                return  tempInstance
            }

            //don't want multiple thread create multiple instance so used synchronized() and only one thread is access to code inside it
            //instance is null or no instance , create instance of database class and return the instance
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoDatabase::class.java,
                        "todo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}