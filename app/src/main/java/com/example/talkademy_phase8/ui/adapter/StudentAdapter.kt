package com.example.talkademy_phase8.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.talkademy_phase8.R
import com.example.talkademy_phase8.data.entity.Student
import com.example.talkademy_phase8.data.local.SQLite.DataBaseOpenHelper
import com.example.talkademy_phase8.data.local.room.StudentDataBase
import com.example.talkademy_phase8.databinding.StudentItemBinding
import com.example.talkademy_phase8.ui.framgent.StudentListFragmentDirections
import com.example.talkademy_phase8.util.DataBase
import com.example.talkademy_phase8.util.Gender
import com.example.talkademy_phase8.util.UiUtil.Companion.getPreferredDataBase
import com.example.talkademy_phase8.util.UiUtil.Companion.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudentAdapter() : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private val students = ArrayList<Student>()

    fun setStudents(students: List<Student>) {
        this.students.clear()
        this.students.addAll(students)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding: StudentItemBinding =
            StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val preferredDB = getPreferredDataBase(parent.context as AppCompatActivity)
        return StudentViewHolder(binding, preferredDB)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size


    inner class StudentViewHolder(
        private val binding: StudentItemBinding,
        private val preferredDB: String
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.studentName.text = student.name
            binding.studentFamily.text = student.family
            binding.studentScore.text = student.score
            val gender = student.gender

            if (gender == Gender.MALE)
                binding.studentImage.setImageResource(R.drawable.male)
            else
                binding.studentImage.setImageResource(R.drawable.female)


            itemView.setOnLongClickListener {
                showDialog(student, adapterPosition, itemView.context, preferredDB)
                return@setOnLongClickListener (true)
            }
        }
    }

    private fun showDialog(student: Student, position: Int, context: Context, preferredDB: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle("${student.name} ${student.family}")
            .setMessage("Choose action to do with selected student")
            .setNeutralButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setNegativeButton("Delete") { dialog, which ->
                if (preferredDB == DataBase.Room.name) {
                    val studentDatabase = StudentDataBase
                    val dao = studentDatabase.getDatabase(context)
                    GlobalScope.launch {
                        dao.studentDao().delete(student)
                    }
                    showToast(context, "${student.name} deleted! ROOM")
                } else {
                    val openHelper = DataBaseOpenHelper(context)
                    openHelper.deleteEmployee(student)
                    showToast(context, "${student.name} deleted! SQLite")
                }
                students.removeAt(position)
                //update list and remove item
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, students.size)

            }
            .setPositiveButton("Edit") { dialog, which ->
                val action = StudentListFragmentDirections.editStudent(student)
                Navigation.findNavController(context as Activity, R.id.student_item_root)
                    .navigate(action)
            }
            .show()
    }


}