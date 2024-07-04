package com.example.tasklist.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.adapter.TaskAdapter
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityArchivedTaskBinding
import com.example.tasklist.utils.DatabaseManager

class ArchivedTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchivedTaskBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var dbHelper: DatabaseManager
    private lateinit var taskDAO: TaskDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchivedTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseManager(this)
        taskDAO = TaskDAO(this)

        recyclerView = binding.recyclerView

        taskAdapter = TaskAdapter(
            dataSet = emptyList(),
            onItemClick = { position -> /* Manejar clic en item */ },
            onDeleteSwipe = { position ->
                val task = taskAdapter.getItem(position)
                taskDAO.delete(task)
                taskAdapter.removeItem(position)
            }
            )

            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ArchivedTaskActivity)
                adapter = taskAdapter
            }

            loadArchivedTasks()

        }

        private fun loadArchivedTasks() {
            val tasks = taskDAO.getArchivedTasks()
            taskAdapter.updateData(tasks)
    }
    }
