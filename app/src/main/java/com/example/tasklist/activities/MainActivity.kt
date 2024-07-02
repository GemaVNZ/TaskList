package com.example.tasklist.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.adapter.TaskAdapter
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var adapter : TaskAdapter

    private lateinit var taskList : List<Task>

    private lateinit var taskDAO : TaskDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskDAO = TaskDAO(this)

        taskList = taskDAO.findAll()

        adapter = TaskAdapter(taskList) {
            Toast.makeText(this, "Click en tarea:", Toast.LENGTH_LONG).show()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}