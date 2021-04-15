package com.example.todo.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewModel.ToDoViewModel
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.fragments.SharedViewModel

class AddFragment : Fragment() {
    //binding variables
    private var _binding:FragmentAddBinding? = null
    private val binding get() = _binding!!
    //viewModel
    private val mToDoViewModel : ToDoViewModel by viewModels()
    //SharedViewModel
    private val mSharedViewModel : SharedViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout for this fragment with viewBinding
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root

        //set options menu in Add Fragment
        setHasOptionsMenu(true)

        //to display colored spinner
        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        return view
    }

    //overriding for viewBinding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //overriding for setHasOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu,menu)
    }

    //To validate the checkMark override onOptionsItemSelected OR will handel onClickListener for menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //if item selected is equals to tick mark insert data , so call insertDataToDb()
        if(item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    //function to insertData to Db
    private fun insertDataToDb() {
        //getting values from layout or edittext and spinner
        val mTitle = binding.titleEditText.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEditText.text.toString()
        //to verify input fields call verifyDataFromUser fun
        val validation = mSharedViewModel.verifyDataFromUser(mTitle,mDescription)
        //if validation true, insert else do not insert
        if(validation){
            //ROOM will automatically generate id so just pass 0, parse the string priority to get back priority object
            val newData = ToDoData(0,mTitle,mSharedViewModel.parsePriority(mPriority),mDescription)
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(),"Successfully Added",Toast.LENGTH_LONG).show()
            //navigate user back from add to list fragment
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else{
            Toast.makeText(requireContext(),"Please Fill out all Fields",Toast.LENGTH_LONG).show()
        }
    }
}