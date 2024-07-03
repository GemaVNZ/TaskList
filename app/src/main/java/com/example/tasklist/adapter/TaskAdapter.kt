package com.example.tasklist.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.data.Task
import com.example.tasklist.databinding.ItemTaskBinding


class TaskAdapter (private var dataSet : List<Task> = emptyList(),
                   private var onItemClick:(Int) -> Unit,
                   private var onDeleteSwipe: (Int) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(), ItemTouchHelperAdapter {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TaskViewHolder(binding,onItemClick,onDeleteSwipe)
        }

        override fun getItemCount(): Int = dataSet.size

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.bind(dataSet[position])
            holder.itemView.setOnClickListener{
                onItemClick(holder.adapterPosition)
            }
        }

        fun updateData(newData: List<Task> ) {
            dataSet = newData
            notifyDataSetChanged()
        }

    override fun onItemDismiss(position: Int) {
        onDeleteSwipe(position)
    }



    class TaskViewHolder (private val binding: ItemTaskBinding,
                          onItemClick: (Int) -> Unit, // Pasar como parámetro
                          onDeleteSwipe: (Int) -> Unit // Pasar como parámetro
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    onItemClick(adapterPosition)
                }
                false
            }
        }

        fun bind(task: Task) {
            binding.nameTextView.text = task.name
        }
    }
}        //Método para pintar la vista
        //fun render (task : Task) {
            //binding.nameTextView.text = task.name




