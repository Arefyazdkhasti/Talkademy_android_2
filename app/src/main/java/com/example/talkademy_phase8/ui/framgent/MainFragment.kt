package com.example.talkademy_phase8.ui.framgent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.talkademy_phase8.R
import com.example.talkademy_phase8.data.Student
import com.example.talkademy_phase8.databinding.FragmentMainBinding
import com.example.talkademy_phase8.util.Gender

class MainFragment : Fragment() {


    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        binding.addStudentBtn.setOnClickListener {
            val action = MainFragmentDirections.addStudent(null)
            Navigation.findNavController(requireActivity(), R.id.add_student_btn)
                .navigate(action)
        }

        binding.showStudentBtn.setOnClickListener {
            val action = MainFragmentDirections.showStudentList()
            Navigation.findNavController(requireActivity(), R.id.show_student_btn)
                .navigate(action)
        }
    }
}