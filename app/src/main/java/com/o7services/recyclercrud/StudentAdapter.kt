package com.o7services.recyclercrud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.o7services.recyclercrud.databinding.ItemRvLayoutBinding

class StudentAdapter(val studentList:ArrayList<StudentModel>, var list:UserInterface):RecyclerView.Adapter<StudentAdapter.viewHolder>(){
    class viewHolder (val binding:ItemRvLayoutBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding=ItemRvLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return viewHolder(binding)


    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
    holder.binding.tvName.text=studentList[position].name
    holder.binding.tvRollNo.text=studentList[position].rollno
    holder.itemView.setOnClickListener {
        list.listUpdate(position)
    }

    }

    override fun getItemCount(): Int {
         return studentList.size
    }


}