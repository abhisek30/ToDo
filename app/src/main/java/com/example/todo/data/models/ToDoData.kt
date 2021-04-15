package com.example.todo.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo.data.models.Priority
import kotlinx.android.parcel.Parcelize

//Declare @Entity() as specify table name to get notified by ROOM library as an Table/Entity
//Making toDoData parcelable so that to use safeargs between fragments
@Entity(tableName = "todo_table")
@kotlinx.parcelize.Parcelize
data class ToDoData (
        //to generate unique id for var id
        @PrimaryKey(autoGenerate = true)
        var id:Int,
        var title:String,
        var priority: Priority,
        var description: String
        ):Parcelable