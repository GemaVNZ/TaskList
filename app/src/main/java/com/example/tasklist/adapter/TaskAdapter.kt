package com.example.tasklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.data.Task
import com.example.tasklist.databinding.ItemTaskBinding

class TaskAdapter (private var dataSet : List<Task> = emptyList(),
                   private val onItemClickListener:(Int) -> Unit) :
    RecyclerView.Adapter<TaskViewHolder>()  {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TaskViewHolder(binding)
        }

        override fun getItemCount(): Int = dataSet.size

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.render(dataSet[position])
            holder.itemView.setOnClickListener{
                onItemClickListener(holder.adapterPosition)
            }
        }

        fun updateData(newData: List<Task> ) {
            dataSet = newData
            notifyDataSetChanged()
        }

    }

    class TaskViewHolder (private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        //Método para pintar la vista
        fun render (task : Task) {
            binding.nameTextView.text = task.name

        }

    }