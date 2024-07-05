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
        fun removeItem(position: Int) {
            val mutableList = dataSet.toMutableList()
            mutableList.removeAt(position)
            dataSet = mutableList
            notifyItemRemoved(position)
    }

        fun getItem(position: Int): Task {
            return dataSet[position]
    }


    override fun onItemDismiss(position: Int) {
        onDeleteSwipe(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        // Implementar lógica de movimiento si es necesario
        return false
    }

    class TaskViewHolder (private val binding: ItemTaskBinding,
                          onItemClick: (Int) -> Unit, // Pasar como parámetro
                          onDeleteSwipe: (Int) -> Unit // Pasar como parámetro
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)

            }

        }

        fun bind(task: Task) {
            binding.nameTextView.text = task.name
        }

    }
}        //Método para pintar la vista
        //fun render (task : Task) {
            //binding.nameTextView.text = task.name

/*binding.root.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    onItemClick(adapterPosition)
                }
                false
            }*/




