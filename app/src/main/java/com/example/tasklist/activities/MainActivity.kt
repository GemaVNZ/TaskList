package com.example.tasklist.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.adapter.TaskAdapter
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: TaskAdapter

    private lateinit var taskList: MutableList<Task>

    private lateinit var taskDAO: TaskDAO

    private lateinit var addTaskLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskDAO = TaskDAO(this)

        taskList = mutableListOf()

        //Función para actualizar el adapter. Además se puede clickar en él.
        adapter = TaskAdapter(taskList) {
            val task = taskList[it]
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("TASK_ID", task.id)
            startActivity(intent)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        var addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val taskName = data.getStringExtra("taskName")
                    if (taskName != null) {
                        val newTask = Task(-1, taskName)
                        taskList.add(newTask)
                        adapter.updateData(taskList)
                    }
                }
            }
        }

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            addTaskLauncher.launch(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        taskList = taskDAO.findAll().toMutableList()

        adapter.updateData(taskList)
    }

    //Función para hacer la flecha de retroceso del menú
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                android.R.id.home -> {
                    finish()
                    return true
                }
            }
            return super.onOptionsItemSelected(item)
        }
}


