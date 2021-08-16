package com.example.talkademy_phase8.ui.framgent

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.talkademy_phase8.R
import com.example.talkademy_phase8.databinding.FragmentMainBinding
import com.example.talkademy_phase8.util.DataBase
import com.example.talkademy_phase8.util.UiUtil

const val PREF_KEY = "shared_preferences_database"
const val DATABASE_KEY = "com.example.talkademy_phase8_database"

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
        setUpPreferredDataBase(UiUtil.getPreferredDataBase(activity as AppCompatActivity))
        bindUI()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_manu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sqlite_menu -> {
                setUpPreferredDataBase(DataBase.Sqlite.name)
            }
            R.id.room_menu -> {
                setUpPreferredDataBase(DataBase.Room.name)
            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun setUpPreferredDataBase(dataBase: String){
        val sharedPref = activity?.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString(DATABASE_KEY,dataBase)
        editor?.apply()
        editor?.commit()

        binding.selectedDatabase.text = dataBase.toString()
    }
}