package com.example.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AlertDialogLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewModel.ToDoViewModel
import com.example.todo.databinding.FragmentUpdateBinding
import com.example.todo.fragments.SharedViewModel

class UpdateFragment : Fragment() {
    //viewBinding
    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    //safe args variable
    private val args by navArgs<UpdateFragmentArgs>()
    //ToDoview Model
    private val mToDoViewModel :ToDoViewModel by viewModels()
    //Shared viewModel
    private val mSharedViewModel:SharedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout with viewBinding
        _binding = FragmentUpdateBinding.inflate(inflater,container,false)
        val view = binding.root

        //set menu for Update Fragment
        setHasOptionsMenu(true)

        //getting reference from binding and updating using safe args to display in Update Fragment
        binding.currentTitleEditText.setText(args.currentItem.title)
        binding.currentPrioritiesSpinner.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        binding.currentDescriptionEditText.setText(args.currentItem.description)
        binding.currentPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        return view
    }

    //overriding for viewBinding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //overriding for setHasOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
    }

    //overriding for selecting options(save) from action bar OR onClickListener for save option
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    //function to update a data field
    private fun updateItem() {
        //fetching the data from current data list
        val title = binding.currentTitleEditText.text.toString()
        val description = binding.currentDescriptionEditText.text.toString()
        val getPriority = binding.currentPrioritiesSpinner.selectedItem.toString()

        //verifying data from the user
        val validation = mSharedViewModel.verifyDataFromUser(title, description)
        //if validation true, insert else do not insert
        if(validation){
            //updatedItem represents a ToDoData and after that it is passed to viewModel to update changes in DB
            val updatedItem = ToDoData(args.currentItem.id,title,mSharedViewModel.parsePriority(getPriority),description)
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(),"Updated Successfully",Toast.LENGTH_SHORT).show()
            //navigate back to list fragment
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else{
            Toast.makeText(requireContext(),"Fill out All feilds",Toast.LENGTH_SHORT).show()
        }
    }

    //function to confirm and delete
    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Removed: ${args.currentItem.title}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_-> }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure you want to delete '${args.currentItem.title}'?")
        builder.create().show()
    }
}