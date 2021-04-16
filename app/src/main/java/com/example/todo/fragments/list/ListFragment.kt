package com.example.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.transition.Slide
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.todo.R
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewModel.ToDoViewModel
import com.example.todo.databinding.FragmentListBinding
import com.example.todo.fragments.SharedViewModel
import com.example.todo.fragments.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    //viewBinding variables
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    //Adapter variable
    private val adapter: ListAdapter by lazy { ListAdapter() }

    //viewModel
    private val mToDoViewModel: ToDoViewModel by viewModels()

    //SharedViewModel
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout for this fragment using viewBinding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        //setting up data binding , lifecycle owner to msharedViewModel
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        //setting up recyclerView
        setUpRecyclerView()

        //getting reference of getAllData and observing the live data
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            //checking database whenever we observe changes
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        //Set menu option on ListFragment
        setHasOptionsMenu(true)

        return view
    }

    //function to set up recyclerView
    private fun setUpRecyclerView() {
        //getting reference of recyclerView from Layout with help of binding
        val recyclerView = binding.recylerView
        //pass the adapter(List Adapter) to recyclerView adapter
        recyclerView.adapter = adapter
        //Setting up layout Manager(StaggeredGridLayout Manager)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //recyclerViewAnimator
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        //swipe to delete
        swipeToDelete(recyclerView)
    }

    //function to swipe to delete data
    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleteItem = adapter.datalist[viewHolder.adapterPosition]
                //deleteItem
                mToDoViewModel.deleteItem(deleteItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //restore delete function
                undoDelete(viewHolder.itemView, deleteItem, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    //to restore DeletedItem
    private fun undoDelete(view: View, deleteItem: ToDoData, position: Int) {
        val snackbar = Snackbar.make(
                view, "Delete '${deleteItem.title}'", Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            mToDoViewModel.insertData(deleteItem)
            adapter.notifyItemChanged(position)
        }
        snackbar.show()
    }

    //Overriding for viewBinding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Overriding for setHasOptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    //override to enable setOnclickListener for the items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> deleteAll()
            //observing the changes whenever user clicks the priority sorting
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    //override for searchDatabase (SearchView.OnQueryTextListener) - after enter display result
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    //override for searchDatabase (SearchView.OnQueryTextListener) - after typing something in search box
    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    //function to confirm and delete all items from database
    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                    requireContext(),
                    "Successfully Removed: Everything",
                    Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

    //function to search data within database and display it in the recyclerView
    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.setData(it)
            }
        })
    }
}