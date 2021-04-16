package com.example.todo.data.repository

import androidx.lifecycle.LiveData
import com.example.todo.data.ToDoDao
import com.example.todo.data.models.ToDoData

//to get reference of ToDodao , add parameter to it
class ToDoRepository(private val toDoDao: ToDoDao) {

    //using toDodao to get access from getAllData fun from ToDoDao class and hold reference from dao to repository
    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    //getting Data list sorted according to high priority and low priority
    val sortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()

    //to insert data in DB pass ToDoData to this fun and it will pass on to toDoDoa insertData fun with todoData as parameter
    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    //to update data in DB pass ToDoData to this fun and it will pass on to toDoDoa updateData fun with todoData as parameter
    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    //to delete data in DB pass ToDoData to this fun and it will pass on to toDoDoa deleteData fun with todoData as parameter
    suspend fun deleteItem(toDoData: ToDoData) {
        toDoDao.deleteItem(toDoData)
    }

    //to delete all the data in DB call toDoDoa deleteAll fun
    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    //function to search from the database
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> {
        return toDoDao.searchDatabase(searchQuery)
    }
}