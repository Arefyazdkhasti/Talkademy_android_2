package com.example.talkademy_phase8.ui.framgent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkademy_phase8.data.entity.Student
import com.example.talkademy_phase8.data.local.SQLite.DataBaseOpenHelper
import com.example.talkademy_phase8.data.local.room.StudentDataBase
import com.example.talkademy_phase8.databinding.FragmentStudentListBinding
import com.example.talkademy_phase8.ui.adapter.StudentAdapter
import com.example.talkademy_phase8.util.DataBase
import com.example.talkademy_phase8.util.Filter
import com.example.talkademy_phase8.util.Gender
import com.example.talkademy_phase8.util.UiUtil.Companion.getPreferredDataBase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        val preferredDB = getPreferredDataBase(activity as AppCompatActivity)
        bindUI(preferredDB)
    }

    private fun bindUI(preferredDB: String) {
        //Room
        val studentDatabase = StudentDataBase
        val dao = studentDatabase.getDatabase(requireContext())
        //Sqlite
        val dataBaseOpenHelper = DataBaseOpenHelper(requireContext())

        if (preferredDB == DataBase.Sqlite.name)
            setUpRecycler(dataBaseOpenHelper.getStudents(Filter.All))
        else {
            GlobalScope.launch {
                setUpRecycler((dao.studentDao().getAllStudents()))
            }
        }

        binding.filterFab.setOnClickListener {
            showFilterDialog(dataBaseOpenHelper,dao, preferredDB)
        }
    }


    private fun setUpRecycler(studentList: List<Student>) {
        val studentAdapter = StudentAdapter()
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

    private fun showFilterDialog(dataBaseOpenHelper: DataBaseOpenHelper,dao:StudentDataBase, preferredDB: String) {
        val singleItems = arrayOf(Filter.All.name, Filter.Male.name, Filter.Female.name, Filter.Score.name, Filter.Name.name, Filter.Family.name, Filter.Gender.name)
        val checkedItem = currentFilter

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Filter")
            .setNeutralButton("cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }
            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                applyFilters(singleItems[which], dataBaseOpenHelper,dao,preferredDB)
                currentFilter = which
                dialog.dismiss()
            }
            .show()
    }

    private fun applyFilters(result: String, dataBaseOpenHelper: DataBaseOpenHelper,dao : StudentDataBase,preferredDB:String) {
        when(preferredDB){
            DataBase.Sqlite.name ->{
                when (result) {
                    Filter.All.name -> setUpRecycler(dataBaseOpenHelper.getStudents(Filter.All))
                    Filter.Male.name -> setUpRecycler(dataBaseOpenHelper.getStudents(Filter.Male))
                    Filter.Female.name -> setUpRecycler(dataBaseOpenHelper.getStudents(Filter.Female))
                    Filter.Score.name -> setUpRecycler(dataBaseOpenHelper.getStudents(Filter.Score))
                    Filter.Name.name -> setUpRecycler(dataBaseOpenHelper.getStudents(Filter.Name))
                    Filter.Family.name -> setUpRecycler(dataBaseOpenHelper.getStudents(Filter.Family))
                    Filter.Gender.name -> setUpRecycler(
                        dataBaseOpenHelper.getStudents(Filter.Female) + dataBaseOpenHelper.getStudents(Filter.Male)
                    )
                }
            }
            DataBase.Room.name ->{
                when (result) {
                    Filter.All.name -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getAllStudents())
                        }
                    }
                    Filter.Male.name -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsByGender(Gender.MALE))
                        }
                    }
                    Filter.Female.name -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsByGender(Gender.FEMALE))
                        }
                    }
                    Filter.Score.name -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsSortByScore())
                        }
                    }
                    Filter.Name.name -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsSortByName())
                        }
                    }
                    Filter.Family.name -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsSortByFamily())
                        }
                    }
                    Filter.Gender.name -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsByGender(Gender.FEMALE) +
                                    dao.studentDao().getStudentsByGender(Gender.MALE))
                        }
                    }
                }
            }
        }
    }
}