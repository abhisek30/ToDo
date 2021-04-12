package com.example.todo.fragments.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.databinding.FragmentAddBinding

class AddFragment : Fragment() {
    private var _binding:FragmentAddBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout for this fragment with viewBinding
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root

        //set options menu in Add Fragment
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
        inflater.inflate(R.menu.add_fragment_menu,menu)
    }
}