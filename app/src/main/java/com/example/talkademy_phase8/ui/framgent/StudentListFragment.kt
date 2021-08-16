package com.example.talkademy_phase8.ui.framgent

import android.content.Context
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
import com.example.talkademy_phase8.util.Gender
import com.example.talkademy_phase8.util.UiUtil.Companion.getPreferredDataBase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//filters
private const val ALL = "all"
private const val MALE = "male"
private const val FEMALE = "female"
private const val SCORE = "score"
private const val NAME = "name"
private const val FAMILY = "family"
private const val GENDER = "gender"

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
            setUpRecycler(dataBaseOpenHelper.getAllStudents())
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
        val singleItems = arrayOf(ALL, MALE, FEMALE, SCORE, NAME, FAMILY, GENDER)
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
                    ALL -> setUpRecycler(dataBaseOpenHelper.getAllStudents())
                    MALE -> setUpRecycler(dataBaseOpenHelper.getStudentsByGender(Gender.MALE))
                    FEMALE -> setUpRecycler(dataBaseOpenHelper.getStudentsByGender(Gender.FEMALE))
                    SCORE -> setUpRecycler(dataBaseOpenHelper.getStudentsByScoreOrder())
                    NAME -> setUpRecycler(dataBaseOpenHelper.getStudentsByNameOrder())
                    FAMILY -> setUpRecycler(dataBaseOpenHelper.getStudentsByFamilyOrder())
                    GENDER -> setUpRecycler(
                        dataBaseOpenHelper.getStudentsByGender(Gender.FEMALE) +
                                dataBaseOpenHelper.getStudentsByGender(Gender.MALE)
                    )
                }
            }
            DataBase.Room.name ->{
                when (result) {
                    ALL -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getAllStudents())
                        }
                    }
                    MALE -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsByGender(Gender.MALE))
                        }
                    }
                    FEMALE -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsByGender(Gender.FEMALE))
                        }
                    }
                    SCORE -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsSortByScore())
                        }
                    }
                    NAME -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsSortByName())
                        }
                    }
                    FAMILY -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsSortByFamily())
                        }
                    }
                    GENDER -> {
                        GlobalScope.launch {
                            setUpRecycler(dao.studentDao().getStudentsByGender(Gender.FEMALE) +
                                    dao.studentDao().getStudentsByGender(Gender.MALE))
                        }
                    }
                }
            }
        }

    }
    //region room filter implementation
    /*private fun selectedFilter(, dao: StudentDataBase) {
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
    }*/
    //endregion
}