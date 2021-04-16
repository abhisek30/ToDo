package com.example.todo.data

import androidx.room.TypeConverter
import com.example.todo.data.models.Priority

//to convert priority to string while writing to DB and string to priority while reading from DB
class Converter {
    //to notify ROOM as it use Typeconverter
    //for writing to DB
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name    //will return HIGH,MEDIUM OR LOW as String
    }

    //for reading from DB
    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)   //will return HIGH,MEDIUM OR LOW as Priority
    }
}