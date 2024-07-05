package com.example.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.tasklist.utils.DatabaseManager


//Se crea una clase donde realizaremos todas las funciones que se pueden atribuir a una tabla.
//Insertar registros, borrar, actualizar y leer -> CRUD
class TaskDAO (context: Context) {

    private val databaseManager: DatabaseManager = DatabaseManager(context)

    //Función para insertar los datos

    fun insert(task: Task) : Long {
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, if (task.done) 1 else 0)
        values.put(Task.COLUMN_NAME_ARCHIVED_TASK, if (task.archived) 1 else 0)

        return db.insert(Task.TABLE_NAME, null, values)

        //val newRowId = db.insert(Task.TABLE_NAME, null, values)
        //task.id = newRowId.toInt()

    }

    //Función para actualizarlo los datos
    fun update(task: Task) {
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, task.done)

        db.update(
            Task.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ${task.id}",
            null
        )
    }


    //Función para borrar los datos

    fun deleteTask(task: Task) {
        val db = databaseManager.writableDatabase

        // Si la tarea está archivada, la eliminamos definitivamente de la base de datos
        if (task.archived) {
            db.delete(
                Task.TABLE_NAME,
                "${BaseColumns._ID} = ?",
                arrayOf(task.id.toString())
            )
        } else {
            // Si no está archivada, la marcamos como archivada (como antes)
            val values = ContentValues().apply {
                put(Task.COLUMN_NAME_ARCHIVED_TASK, 1) // Marcar como archivada
            }
            db.update(
                Task.TABLE_NAME,
                values,
                "${BaseColumns._ID} = ?",
                arrayOf(task.id.toString())
            )
        }
    }

    //Función para leer los datos de una columna
    fun find(id: Int) : Task? {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME_TITLE, Task.COLUMN_NAME_DONE)

        val cursor = db.query(
            Task.TABLE_NAME,                                // La tabla de la búsqueda
            projection,                                     // La columna o columnas que quieres devolver
            "${BaseColumns._ID} = $id",             // La claúsula where
            null,                                // los valores de la claúsula where
            null,                                   // don't group the rows
            null,                                    // don't filter by row groups
            null                                    // El orden
        )

        var task: Task? = null
        if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) == 1
            task = Task(id, name, done)
        }
        cursor.close()
        db.close()
        return task
    }

    //Función para leerlos todos
    fun findAll() : List<Task> {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME_TITLE, Task.COLUMN_NAME_DONE)

        val cursor = db.query(
            Task.TABLE_NAME,                        // La tabla de la búsqueda
            projection,                             // La columna o columnas que quieres devolver
            null,                                   // La claúsula where
            null,                                   // los valores de la claúsula where
            null,                                   // don't group the rows
            null,                                   // don't filter by row groups
            null                                    // The sort order
        )

        var tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) == 1
            val task = Task(id, name, done)
            tasks.add(task)
        }
        cursor.close()
        db.close()
        return tasks
    }

    fun getActiveTasks(): List<Task> {
        val db = databaseManager.readableDatabase
        val cursor = db.query(
            Task.TABLE_NAME,
            null,
            "${Task.COLUMN_NAME_ARCHIVED_TASK} = ?",
            arrayOf("0"),
            null,
            null,
            null
        )

        val tasks = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val task = Task(
                    id = getInt(getColumnIndexOrThrow(android.provider.BaseColumns._ID)),
                    name = getString(getColumnIndexOrThrow(com.example.tasklist.data.Task.COLUMN_NAME_TITLE)),
                    done = getInt(getColumnIndexOrThrow(com.example.tasklist.data.Task.COLUMN_NAME_DONE)) == 1,
                    archived = getInt(getColumnIndexOrThrow(com.example.tasklist.data.Task.COLUMN_NAME_ARCHIVED_TASK)) == 1
                )
                tasks.add(task)
            }
        }
        cursor.close()
        return tasks
    }

    fun getArchivedTasks(): List<Task> {
        val db = databaseManager.readableDatabase
        val cursor = db.query(
            Task.TABLE_NAME,
            null,
            "${Task.COLUMN_NAME_ARCHIVED_TASK} = ?",
            arrayOf("1"),
            null,
            null,
            null
        )

        val tasks = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val task = Task(
                    id = getInt(getColumnIndexOrThrow(android.provider.BaseColumns._ID)),
                    name = getString(getColumnIndexOrThrow(com.example.tasklist.data.Task.COLUMN_NAME_TITLE)),
                    done = getInt(getColumnIndexOrThrow(com.example.tasklist.data.Task.COLUMN_NAME_DONE)) == 1,
                    archived = getInt(getColumnIndexOrThrow(com.example.tasklist.data.Task.COLUMN_NAME_ARCHIVED_TASK)) == 1
                )
                tasks.add(task)
            }
        }
        cursor.close()
        return tasks
    }

    fun updateTask(task: Task) {
            val db = databaseManager.writableDatabase
            val values = ContentValues().apply {
            put(Task.COLUMN_NAME_TITLE, task.name)
            put(Task.COLUMN_NAME_DONE, if (task.done) 1 else 0)
            put(Task.COLUMN_NAME_ARCHIVED_TASK, if (task.archived) 1 else 0)
        }

        db.update(Task.TABLE_NAME, values, "${BaseColumns._ID} = ?", arrayOf(task.id.toString()))
    }


    /*fun delete(task: Task) {
       val db = databaseManager.writableDatabase

       val deletedRows = db.delete(Task.TABLE_NAME, "${BaseColumns._ID} = ${task.id}", null)
   }

        fun deleteTask(task: Task) {
            val db = databaseManager.writableDatabase
            db.delete(Task.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(task.id.toString()))
    }*/
}