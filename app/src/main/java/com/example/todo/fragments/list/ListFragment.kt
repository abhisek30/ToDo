package com.example.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewModel.ToDoViewModel
import com.example.todo.databinding.FragmentListBinding
import com.example.todo.fragments.SharedViewModel

class ListFragment : Fragment() {
    //viewBinding variables
    private var _binding:FragmentListBinding? = null
    private val binding get() = _binding!!

    //Adapter variable
    private val adapter : ListAdapter by lazy { ListAdapter() }
    //viewModel
    private val mToDoViewModel : ToDoViewModel by viewModels()
    //SharedViewModel
    private val mSharedViewModel : SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout for this fragment using viewBinding
        _binding = FragmentListBinding.inflate(inflater,container,false)
        val view = binding.root

        //getting reference of recyclerView from Layout with help of binding
        val recyclerView = binding.recylerView
        //pass the adapter(List Adapter) to recyclerView adapter
        recyclerView.adapter = adapter
        //Setting up layout Manager(Linear layout )
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        //getting reference of getAllData and observing the data
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data->
            //checking database whenever we observe changes
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        //check/observe if database is empty or not
        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabase(it)
        })

        //Floating action button to Navigate from list to add fragment
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }


        //Set menu option on ListFragment
        setHasOptionsMenu(true)

        return view
    }

    //To set visibility if the database is empty or not empty
    private fun showEmptyDatabase(emptyDatabase:Boolean) {
        if(emptyDatabase){
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        }else{
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }

    //Overriding for viewBinding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Overriding for setHasOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)
    }

    //override to enable setOnclickListener for the items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    //function to confirm and delete all items from database
    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _,_ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed: Everything",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No"){_,_-> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }
}