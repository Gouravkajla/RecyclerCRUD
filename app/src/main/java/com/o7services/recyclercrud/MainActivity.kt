package com.o7services.recyclercrud

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.o7services.recyclercrud.databinding.ActivityMainBinding
import com.o7services.recyclercrud.databinding.AddLayoutBinding
import com.o7services.recyclercrud.databinding.EditLayoutBinding

class MainActivity : AppCompatActivity(), UserInterface {
    lateinit var binding: ActivityMainBinding
    lateinit var studentAdapter: StudentAdapter
    val db = Firebase.firestore
    var showStudentModel = ArrayList<StudentModel>()

    var studentlist = ArrayList<StudentModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentAdapter = StudentAdapter(showStudentModel, this)
        binding.rycList.layoutManager = LinearLayoutManager(this)
        binding.rycList.adapter = studentAdapter
        binding.etSearch.doOnTextChanged { text, start, before, count ->
            if ((text ?: "").isEmpty()) {
                showStudentModel.clear()
                showStudentModel.addAll(studentlist)
            } else if ((text ?: "").length > 2) {
                showStudentModel.clear()
                var list = ArrayList<StudentModel>()
                list.addAll(studentlist.filter { element ->
                    element.name?.contains(
                        text ?: "",
                        ignoreCase = true
                    ) == true
                })
                showStudentModel.addAll(list)
            }
        }
        /* db.collection("User").get().addOnSuccessListener {
            for(documents in it){
                var studentModle = StudentModle()
                studentModle = documents.toObject(StudentModle::class.java)
                studentModle.key = documents.id?:""
                studentlist.add(studentModle)
            }
            studentAdapter.notifyDataSetChanged()
        }*/
        db.collection("User").get().addOnSuccessListener {
            db.collection("User").addSnapshotListener { value, error ->
                for (snapshots in value!!.documentChanges) {
                    when (snapshots.type) {
                        DocumentChange.Type.ADDED -> {
                            var studentModel = StudentModel()
                            studentModel = snapshots.document.toObject(StudentModel::class.java)
                            studentModel.key = snapshots.document.id ?: ""
                            studentlist.add(studentModel)
                            showStudentModel.add(studentModel)
                            studentAdapter.notifyDataSetChanged()
                        }
                        DocumentChange.Type.MODIFIED -> {

                        }
                        DocumentChange.Type.REMOVED -> {}
                        else -> {
                        }
                    }
                }
            }

            binding.ftnAdd.setOnClickListener {
                val customDialog = Dialog(this)
                val dialogBinding = AddLayoutBinding.inflate(layoutInflater)
                customDialog.setContentView(dialogBinding.root)
                dialogBinding.btnAdd.setOnClickListener {
                    if (dialogBinding.etName.text.isEmpty()) {
                        dialogBinding.etName.error = "Enter Your Name"
                    } else if (dialogBinding.etRollNo.text.isEmpty()) {
                        dialogBinding.etRollNo.error = "Enter Your Roll number"

                    } else {
                        db.collection("User")
                            .add(
                                StudentModel(
                                    dialogBinding.etName.text.toString(),
                                    dialogBinding.etRollNo.text.toString()
                                )
                            )
                            .addOnSuccessListener { }
                        customDialog.dismiss()
                    }
                }
                customDialog.show()
            }
        }
    }

    override fun listUpdate(position: Int) {
        val customDialog1 = Dialog(this)
        val dialogBinding1 = EditLayoutBinding.inflate(layoutInflater)
        customDialog1.setContentView(dialogBinding1.root)
        dialogBinding1.btnEdit.setOnClickListener {
            if (dialogBinding1.etName.text.isEmpty()) {
                dialogBinding1.etName.error = "Enter Your Name"
            } else if (dialogBinding1.etRollNo.text.isEmpty()) {
                dialogBinding1.etRollNo.error = "Enter your Roll No"
            } else {
                /* studentlist.set(
                     position,
                     StudentModle(
                         dialogBinding1.etName.text.toString(),
                         dialogBinding1.etRollNo.text.toString()
                     )
                 )
                 db.collection("User").document(studentlist[position].key ?: "")
                     .set(
                         StudentModle(
                             dialogBinding1.etName.text.toString(),
                             dialogBinding1.etRollNo.text.toString()
                         )
                     )
                     .addOnSuccessListener { }*/
                var updateModle = StudentModel()
                updateModle.name = dialogBinding1.etName.text.toString()
                updateModle.rollno = dialogBinding1.etRollNo.text.toString()
                updateModle.key = studentlist[position].key
                db.collection("User").document(updateModle.key ?: "").set(updateModle)


                studentAdapter.notifyDataSetChanged()
                customDialog1.dismiss()
            }
            dialogBinding1.btnDelete.setOnClickListener {
                if (dialogBinding1.etName.text.toString().isEmpty()) {
                    dialogBinding1.etName.error = "Enter Your  name"
                } else if (dialogBinding1.etRollNo.text.toString().isEmpty()) {
                    dialogBinding1.etRollNo.error = "Enter Your Rollno"
                } else {
                    studentlist.remove(
                        StudentModel(
                            dialogBinding1.etName.text.toString(),
                            dialogBinding1.etRollNo.text.toString()
                        )
                    )
                    studentAdapter.notifyDataSetChanged()
                    customDialog1.dismiss()
                }
            }
        }
        customDialog1.show()
    }
}