package com.example.todo.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.databinding.RowLayoutBinding

//Extend it with RecyclerView Adapter and specifying a viewHolder and creating the class of viewHolder inside it
class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    //list of toDoData
    var datalist = emptyList<ToDoData>()

    //will extend RecyclerView.ViewHolder ,pass view parameters which represents row layout
    class MyViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    //override 3 methods for ListAdapter(onCreateViewHolder,onBindViewHolder,getItemCount())
    //get reference of row_layout file using viewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    //with holder(MyViewHolder) and binding getting the reference of the row_layout
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //change the titleText(And all other views) of row_layout by using toDoData list and position dynamically
        with(holder) {
            binding.titleText.text = datalist[position].title
            binding.descriptionTextView.text = datalist[position].description

            when (datalist[position].priority) {
                Priority.HIGH -> binding.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.red))

                Priority.MEDIUM -> binding.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.yellow))

                Priority.LOW -> binding.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green))
            }

            //navigate from listFragment to UpdateFragment using safe args
            binding.rowBackground.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(datalist[position])
                binding.rowBackground.findNavController().navigate(action)
            }
        }
    }

    //return count of datalist (todoData)
    override fun getItemCount(): Int {
        return datalist.size
    }

    //getting parameters of list todoData
    fun setData(toDoData:List<ToDoData>){
        //set toDoData list of parameters to toDoData from ListAdapter
        this.datalist = toDoData
        //and notify the changes
        notifyDataSetChanged()
    }
}