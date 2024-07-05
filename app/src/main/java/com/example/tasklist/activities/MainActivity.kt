package com.example.tasklist.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.adapter.SwipeToDeleteCallback
import com.example.tasklist.adapter.TaskAdapter
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityMainBinding
import com.example.tasklist.utils.DatabaseManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: TaskAdapter

    private lateinit var taskList: MutableList<Task>

    private lateinit var taskDAO: TaskDAO

    //private lateinit var addTaskLauncher: ActivityResultLauncher<Intent>

    private lateinit var dbHelper : DatabaseManager

    private var showArchivedTasks = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseManager(this)
        taskDAO = TaskDAO(this)
        taskList = mutableListOf()

        adapter = TaskAdapter(
            taskList,
            onItemClick = { position ->
                val task = taskList[position]
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("TASK_ID", task.id)
                startActivity(intent)
                //Toast.makeText(this, "Click en tarea: ${taskList[position].name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteSwipe = { position ->
                val task = adapter.getItem(position)
                if (showArchivedTasks) {
                    taskDAO.deleteTask(task) // Eliminar tarea definitivamente
                    Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                } else {
                    task.archived = true
                    taskDAO.updateTask(task) // Archivar tarea
                }
                adapter.removeItem(position)
                //Toast.makeText(this, "Tarea archivada", Toast.LENGTH_SHORT).show()
                updateTaskList()
            })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)


        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
            //addTaskLauncher.launch(intent)
        }

        updateTaskList ()
    }

    override fun onResume() {
        super.onResume()
        taskList.clear()
        taskList.addAll(getTasksToShow())
        adapter.updateData(taskList)
    }

    // Función para actualizar la lista de tareas desde la base de datos
    private fun updateTaskList() {
        taskList.clear()
        taskList.addAll(taskDAO.getActiveTasks())
        adapter.updateData(taskList)
    }

    //Función para crear el icono de archivado en el menú principal
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    //Función para hacer la flecha de retroceso del menú y añadir el icono de archivar
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
                R.id.action_trash -> {
                    showArchivedTasks = !showArchivedTasks
                    taskList.clear()
                    taskList.addAll(getTasksToShow())
                    adapter.updateData(taskList)
                    true
                }

                android.R.id.home -> {
                    finish()
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }

    //Función para mostrar tareas archivadas
    private fun getTasksToShow(): List<Task> {
        return if (showArchivedTasks) {
            taskDAO.getArchivedTasks()
        } else {
            taskDAO.getActiveTasks()
        }
    }


    /*addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
{ result ->
    if (result.resultCode == RESULT_OK) {
        //Recuperar la tarea desde el Detail Activity
        val data = result.data
        val taskName = data?.getStringExtra("taskName") ?: return@registerForActivityResult
        val newTask = Task(-1, taskName) // -1 because the ID will be generated automatically

        try {
        // Guardar la nueva tarea en la base de datos
        val taskId = taskDAO.insert(newTask)

        // Actualizar la lista de tareas desde la base de datos
        taskList.clear()
        taskList.addAll(taskDAO.findAll())
        adapter.updateData(taskList)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al insertar tarea: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }
}*/

    //Función para actualizar el adapter. Además se puede clickar en él.
    /*adapter = TaskAdapter(taskList) {
        val task = taskList[it]
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("TASK_ID", task.id)
        startActivity(intent) }
        */

}


