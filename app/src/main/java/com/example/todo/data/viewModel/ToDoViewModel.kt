package com.example.todo.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.ToDoDatabase
import com.example.todo.data.models.ToDoData
import com.example.todo.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//extend with AndroidViewModel which contains Application context
class ToDoViewModel(application: Application):AndroidViewModel(application){
    //get the toDoDoa instance
    private val toDoDao = ToDoDatabase.getDatabase(application).todoDao()
    //get repository Instance
    private val repository:ToDoRepository
    //list of all toDoData wrapped inside LiveData and later observe this field to get notified whenever a new data is inserted to DB
    val getAllData:LiveData<List<ToDoData>>

    //called when ToDoViewModel is initialized first,initialize repo and all data
    init{
        repository = ToDoRepository(toDoDao)
        getAllData = repository.getAllData
    }

    //use viewModelScope(part of Kotlin Coroutines) to launch coroutine
    //want to run insertData() in background thread so use coroutines inside viewModel
    fun insertData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertData(toDoData)
        }
    }

    //use viewModelScope(part of Kotlin Coroutines) to launch coroutine
    //want to run updateData() in background thread so use coroutines inside viewModel
    fun updateData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateData(toDoData)
        }
    }

    //want to run deleteItem() in background thread so use coroutines inside viewModel
    fun deleteItem(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteItem(toDoData)
        }
    }

    //want to run deleteAll() in background thread so use coroutines inside viewModel
    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}