package com.o7services.recyclercrud

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.o7services.recyclercrud.databinding.ActivityMainBinding
import com.o7services.recyclercrud.databinding.AddLayoutBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
   lateinit var studentAdapter: StudentAdapter
   var studentlist=ArrayList<StudentModle>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
                studentAdapter= StudentAdapter(studentlist)
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
                    studentlist.add(StudentModle(dialogBinding.etName.text.toString(), dialogBinding.etRollNo.text.toString()))
                     studentAdapter.notifyDataSetChanged()
                    customDialog.dismiss()

                }
            }


              customDialog.show()



        }
    }
}