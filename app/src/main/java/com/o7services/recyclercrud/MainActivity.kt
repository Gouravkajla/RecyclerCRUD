package com.o7services.recyclercrud

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.o7services.recyclercrud.databinding.ActivityMainBinding
import com.o7services.recyclercrud.databinding.AddLayoutBinding
import com.o7services.recyclercrud.databinding.EditLayoutBinding

class MainActivity : AppCompatActivity(),UserInterface{
    lateinit var binding:ActivityMainBinding
   lateinit var studentAdapter: StudentAdapter
    val db=Firebase.firestore
   var studentlist=ArrayList<StudentModle>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

                studentAdapter= StudentAdapter(studentlist,this)
                binding.rycList.layoutManager=LinearLayoutManager(this)
                binding.rycList.adapter=studentAdapter

        binding.ftnAdd.setOnClickListener{
            val customDialog=Dialog(this)
            val dialogBinding=AddLayoutBinding.inflate(layoutInflater)
            customDialog.setContentView(dialogBinding.root)
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etName.text.isEmpty()){
                    dialogBinding.etName.error="Enter Your Name"
                }else if(dialogBinding.etRollNo.text.isEmpty()){
                    dialogBinding.etRollNo.error="Enter Your Roll number"

                }else{

                   
                    studentAdapter.notifyDataSetChanged()

                    db.collection("User")
                        .add(StudentModle(dialogBinding.etName.text.toString(), dialogBinding.etRollNo.text.toString()))
                        .addOnSuccessListener {  }
                     customDialog.dismiss()
                }
            }
            customDialog.show()
        }
    }

    override fun listUpdate(position: Int) {
        val customDialog1=Dialog(this)
        val dialogBinding1=EditLayoutBinding.inflate(layoutInflater)
        customDialog1.setContentView(dialogBinding1.root)
        dialogBinding1.btnEdit.setOnClickListener {
            if (dialogBinding1.etName.text.isEmpty()){
                dialogBinding1.etName.error="Enter Your Name"
            }else if (dialogBinding1.etRollNo.text.isEmpty()){
                dialogBinding1.etRollNo.error="Enter your Roll No"
            }else{
                studentlist.set(position,
                    StudentModle(dialogBinding1.etName.text.toString(),dialogBinding1.etRollNo.text.toString())
                )

                studentAdapter.notifyDataSetChanged()
                customDialog1.dismiss()
            }
            dialogBinding1.btnDelete.setOnClickListener {
                if (dialogBinding1.etName.text.toString().isEmpty()){
                    dialogBinding1.etName.error="Enter Your  name"
                }
                else if (dialogBinding1.etRollNo.text.toString().isEmpty()){
                    dialogBinding1.etRollNo.error="Enter Your Rollno"
                }
                else{
                    studentlist.remove(StudentModle(dialogBinding1.etName.text.toString(),dialogBinding1.etRollNo.text.toString()))
                    studentAdapter.notifyDataSetChanged()
                    customDialog1.dismiss()
                }
            }

        }
        customDialog1.show()

    }


}