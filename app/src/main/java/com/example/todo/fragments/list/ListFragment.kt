package com.example.todo.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentListBinding

class ListFragment : Fragment() {
    //viewBinding variables
    private var _binding:FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout for this fragment using viewBinding
        _binding = FragmentListBinding.inflate(inflater,container,false)
        val view = binding.root

        //Floating action button to Navigate from list to add fragment
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        //Floating action button to Navigate from list to update fragment
        binding.listLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }

        //Set menu option on ListFragment
        setHasOptionsMenu(true)

        return view
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
}