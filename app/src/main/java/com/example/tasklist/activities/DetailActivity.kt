package com.example.tasklist.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var taskdao: TaskDAO
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        taskdao = TaskDAO(this)

        //Función para editar la nueva tarea con las diferentes funcionalidades
        val id = intent.getIntExtra("TASK_ID", -1)
        if (id != -1) {
            task = taskdao.find(id)
            binding.nameEditText.setText(task?.name)
        } else {
            task = null
        }

        binding.saveButton.setOnClickListener {
            saveTask()
        }
    }

        //Función para guardar tras hacer un click.
        private fun saveTask() {
            val taskName = binding.nameEditText.text.toString().trim()

            if (taskName.isEmpty()) {
                binding.nameEditText.error = "La tarea no puede estar vacía"
                return
            } else {
                binding.nameEditText.error = null
            }
            if (task != null) {
                task!!.name = taskName
                taskdao.update(task!!)
            } else {
                val newTask = Task(-1, taskName)
                taskdao.insert(newTask)
            }

            Toast.makeText(this, "Tarea guardada correctamente", Toast.LENGTH_LONG).show()


            val resultIntent = Intent()
            resultIntent.putExtra("taskName", taskName)
            setResult(RESULT_OK, resultIntent)
            finish()

        }

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

//Función que configura el listener para que el botón guardar del teclado guarde la tarea directamente
        /*
        binding.nameEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveTask()
                true
            } else {
                false
            }
        }

        binding.saveButton.setOnClickListener {
            saveTask()
        }
        }*/

     /*Toast.makeText(
        this,
        "Por favor, introduce un nombre para la tarea",
        Toast.LENGTH_SHORT
        ).show()
            return@setOnClickListener  // Salir del método sin guardar la tarea si está vacía*/

/*private fun showEmptyTaskNameDialog() {
    AlertDialog.Builder(this)
        .setTitle("La tarea está vacía")
        .setMessage("Por favor, introduce un nombre para la tarea")
        .setPositiveButton("OK") { dialog, which ->
            // Aquí puedes manejar alguna acción adicional si es necesario
        }
        .show()*/
