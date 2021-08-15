package com.example.talkademy_phase8.ui.framgent

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkademy_phase8.R
import com.example.talkademy_phase8.data.Student
import com.example.talkademy_phase8.data.local.StudentDao
import com.example.talkademy_phase8.data.local.StudentDataBase
import com.example.talkademy_phase8.databinding.FragmentStudentListBinding
import com.example.talkademy_phase8.ui.adapter.StudentAdapter
import com.example.talkademy_phase8.util.Gender
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding
        get() = _binding!!

    private var currentFilter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        val studentDatabase = StudentDataBase
        val dao = studentDatabase.getDatabase(requireContext())

        GlobalScope.launch {
            setUpRecycler((dao.studentDao().getAllStudents()))
        }

        binding.filterFab.setOnClickListener {
            showFilterDialog(dao)
        }
    }

    private fun setUpRecycler(studentList: List<Student>) {
        val studentAdapter = StudentAdapter(requireContext())
        studentAdapter.setStudents(studentList)
        requireActivity().runOnUiThread {
            binding.studentsRecyclerView.apply {
                println(studentAdapter.itemCount)
                adapter = studentAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun showFilterDialog(dao: StudentDataBase) {
        val singleItems = arrayOf("All", "Male", "Female", "Score", "Name", "Family", "Gender")
        val checkedItem = currentFilter

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Filter")
            .setNeutralButton("cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                selectedFilter(singleItems[which], dao)
                currentFilter = which
                dialog.dismiss()
            }
            .show()
    }

    private fun selectedFilter(result: String, dao: StudentDataBase) {
        when (result) {
            "ALl" -> {
                updateView(dao.studentDao().getAllStudents())
            }
            "Male" -> {
                updateView(dao.studentDao().getStudentsByGender(Gender.MALE))
            }
            "Female" -> {
                updateView(dao.studentDao().getStudentsByGender(Gender.FEMALE))
            }
            "Score" -> {
                updateView(dao.studentDao().getStudentsSortByScore())
            }
            "Name" -> {
                updateView(dao.studentDao().getStudentsSortByName())
            }
            "Family" -> {
                updateView(dao.studentDao().getStudentsSortByFamily())
            }
            "Gender" -> {
                updateView(dao.studentDao().getStudentsByGender(Gender.FEMALE) + dao.studentDao().getStudentsByGender(Gender.MALE))
            }
        }
    }

    private fun updateView(list: List<Student>) {
        GlobalScope.launch {
            setUpRecycler(
                list
            )
        }
    }

}