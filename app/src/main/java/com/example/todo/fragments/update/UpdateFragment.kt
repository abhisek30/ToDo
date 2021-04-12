package com.example.todo.fragments.update

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {
    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout with viewBinding
        _binding = FragmentUpdateBinding.inflate(inflater,container,false)
        val view = binding.root

        //set menu for Update Fragment
        setHasOptionsMenu(true)
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
}