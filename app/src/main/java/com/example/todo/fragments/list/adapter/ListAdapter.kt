package com.example.todo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.models.ToDoData
import com.example.todo.databinding.RowLayoutBinding

//Extend it with RecyclerView Adapter and specifying a viewHolder and creating the class of viewHolder inside it
class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    //list of toDoData
    var datalist = emptyList<ToDoData>()

    //will extend RecyclerView.ViewHolder ,pass view parameters which represents row layout
    class MyViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        //binding function for toDoData object
        fun bind(toDoData: ToDoData) {
            binding.todoData = toDoData
            binding.executePendingBindings()
        }

        //returning view binding
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    //override 3 methods for ListAdapter(onCreateViewHolder,onBindViewHolder,getItemCount())
    //get reference of row_layout file using viewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //creating currentItem of toDoData and getting dynamically from data list
        val currentItem = datalist[position]
        //using holder passing currentItem(toDoData object) to bind function
        holder.bind(currentItem)
    }

    //return count of datalist (todoData)
    override fun getItemCount(): Int {
        return datalist.size
    }

    //getting parameters of list todoData
    fun setData(toDoData: List<ToDoData>) {
        //diffUtil variables
        val toDoDiffUtil = ToDoDiffUtil(datalist, toDoData)
        val toDoDiffUtilResult = DiffUtil.calculateDiff(toDoDiffUtil)
        //set toDoData list of parameters to toDoData from ListAdapter
        this.datalist = toDoData
        //and notify the changes to diffutil
        toDoDiffUtilResult.dispatchUpdatesTo(this)
    }
}