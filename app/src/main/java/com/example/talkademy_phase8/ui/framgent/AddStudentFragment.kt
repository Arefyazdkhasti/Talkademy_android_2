package com.example.talkademy_phase8.ui.framgent

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.talkademy_phase8.R
import com.example.talkademy_phase8.data.Student
import com.example.talkademy_phase8.data.local.StudentDao
import com.example.talkademy_phase8.data.local.StudentDataBase
import com.example.talkademy_phase8.databinding.FragmentAddStudentBinding
import com.example.talkademy_phase8.databinding.FragmentMainBinding
import com.example.talkademy_phase8.util.Gender
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddStudentFragment : Fragment() {

    private var _binding: FragmentAddStudentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { AddStudentFragmentArgs.fromBundle(it) }
        val student = safeArgs?.student
        if (student != null) {
            println(student.nationalCode)
            bindStudent(student)
            bindUI(true)
            (activity as? AppCompatActivity)?.supportActionBar?.title = "Update Student"
        }else{
            bindUI(false)
        }
    }

    private fun bindStudent(student: Student) {

        binding.nameEdt.setText(student.name)
        binding.familyEdt.setText(student.family)
        binding.nationalCodeEdt.apply {
            setText(student.nationalCode)
            isEnabled = false
        }
        binding.scoreEdt.setText(student.score)
        if (student.gender == Gender.MALE)
            binding.maleRadioBtn.isChecked = true
        else
            binding.femaleRadioBtn.isChecked = true
    }

    private fun bindUI(isLoaded: Boolean) {
        val studentDatabase = StudentDataBase
        val dao = studentDatabase.getDatabase(requireContext())

        binding.saveBtn.setOnClickListener {

            val name = binding.nameEdt.text.toString()
            val family = binding.familyEdt.text.toString()
            val nationalCode = binding.nationalCodeEdt.text.toString()
            val score = binding.scoreEdt.text.toString()

            val gender: Gender = if (binding.maleRadioBtn.isChecked) Gender.MALE else Gender.FEMALE

            if (name.isNotEmpty() and family.isNotEmpty() and nationalCode.isNotEmpty() and score.isNotEmpty()) {
                val student = Student(name, family, nationalCode, score, gender)

                if (isLoaded){
                    GlobalScope.launch {
                        dao.studentDao().update(student)
                    }
                    showToast("${student.name} ${student.family} updated!")
                }else{
                    GlobalScope.launch {
                        dao.studentDao().insert(student)
                    }
                    showToast("${student.name} ${student.family} added!")
                }

            } else Toast.makeText(requireContext(), "fill all parts!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToast(text:String){
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

}