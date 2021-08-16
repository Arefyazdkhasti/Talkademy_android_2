package com.example.talkademy_phase8.ui.framgent

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.talkademy_phase8.data.entity.Student
import com.example.talkademy_phase8.data.local.SQLite.DataBaseOpenHelper
import com.example.talkademy_phase8.data.local.room.StudentDataBase
import com.example.talkademy_phase8.databinding.FragmentAddStudentBinding
import com.example.talkademy_phase8.util.DataBase
import com.example.talkademy_phase8.util.Gender
import com.example.talkademy_phase8.util.UiUtil.Companion.showToast
import com.example.talkademy_phase8.util.UiUtil.Companion.getPreferredDataBase
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

        val preferredDB = getPreferredDataBase(activity as AppCompatActivity)

        val safeArgs = arguments?.let { AddStudentFragmentArgs.fromBundle(it) }
        val student = safeArgs?.student
        if (student != null) {
            println(student.nationalCode)
            bindStudent(student)
            bindUI(true, preferredDB)
            (activity as? AppCompatActivity)?.supportActionBar?.title = "Update Student"
        } else {
            bindUI(false, preferredDB)
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

    private fun bindUI(isLoaded: Boolean, dataBase: String) {


        val studentDatabase = StudentDataBase
        val dao = studentDatabase.getDatabase(requireContext())

        val openHelper = DataBaseOpenHelper(requireContext())

        binding.saveBtn.setOnClickListener {

            val name = binding.nameEdt.text.toString()
            val family = binding.familyEdt.text.toString()
            val nationalCode = binding.nationalCodeEdt.text.toString()
            val score = binding.scoreEdt.text.toString()

            val gender: Gender = if (binding.maleRadioBtn.isChecked) Gender.MALE else Gender.FEMALE

            if (name.isNotEmpty() and family.isNotEmpty() and nationalCode.isNotEmpty() and score.isNotEmpty()) {
                val student = Student(name, family, nationalCode, score, gender)

                if (isLoaded) { //update student
                    if (dataBase == DataBase.Sqlite.name) {
                        if (openHelper.updateStudent(student) != 0) {
                            showToast(
                                requireContext(),
                                "${student.name} ${student.family} updated! SQLite"
                            )
                        } else
                            showToast(requireContext(), "Error in updating student")
                    }else{
                        GlobalScope.launch {
                            dao.studentDao().update(student)
                        }
                        showToast(requireContext(),"${student.name} ${student.family} updated! ROOM")
                    }

                } else { //store new student
                    if (dataBase == DataBase.Sqlite.name) {
                        if (!openHelper.checkStudentExists(student.nationalCode)) {
                            if (openHelper.addStudent(student) != 0.toLong())
                                showToast(
                                    requireContext(),
                                    "${student.name} ${student.family} added! SQLite"
                                )
                            else
                                showToast(requireContext(), "Error in adding student")
                        } else {
                            showToast(
                                requireContext(),
                                "Student already existed with this National Code!"
                            )
                        }
                    }else{
                        GlobalScope.launch {
                            dao.studentDao().insert(student)
                        }
                        showToast(requireContext(),"${student.name} ${student.family} added! ROOM")
                    }
                }


                //Room implementation
                /*if (isLoaded){
                    GlobalScope.launch {
                        dao.studentDao().update(student)
                    }
                    showToast("${student.name} ${student.family} updated!")
                }else{
                    GlobalScope.launch {
                        dao.studentDao().insert(student)
                    }
                    showToast("${student.name} ${student.family} added!")
                }*/

            } else showToast(requireContext(), "fill all parts!")
        }
    }


}