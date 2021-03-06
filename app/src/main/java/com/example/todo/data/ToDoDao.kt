package com.example.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo.data.models.ToDoData

//Represent the interface with @Dao to get notified by ROOM
@Dao
interface ToDoDao {
    //to receive all data from table
    //Custom query to display all data from todo_table with id as ascending order
    //this query will return type of list(TodoData) wrapped inside LiveData
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoData>>

    //predefined Insert Query to insert data
    //if new data is same as the data we already have in db then ignore the conflict by passing arguments.
    //Add suspend keyword to tell compiler , function will be running inside coroutines i.e want to run function in background thread in viewModel.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    //predefined Update Query to Update data
    @Update
    suspend fun updateData(toDoData: ToDoData)

    //predefined Delete Query to Update data
    @Delete
    suspend fun deleteItem(toDoData: ToDoData)

    //custom query to delete all data from table
    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    //custom query to search data from database
    @Query("SELECT * FROM TODO_TABLE WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>

    //custom query to sort the data according to high priority in database
    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<ToDoData>>

    //custom query to sort the data according to low priority in database
    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<ToDoData>>
}