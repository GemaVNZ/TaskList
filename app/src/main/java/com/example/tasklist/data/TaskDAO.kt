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
    fun insert(task: Task) {
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, task.done)

        val newRowId = db.insert(Task.TABLE_NAME, null, values)
        task.id = newRowId.toInt()
    }

    //Función para actualizarlo los datos
    fun update(task: Task) {
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, task.done)

        val updatedRows = db.update(
            Task.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ${task.id}",
            null
        )
    }


    //Función para borrar los datos
    fun delete(task: Task) {
        val db = databaseManager.writableDatabase

        val deletedRows = db.delete(Task.TABLE_NAME, "${BaseColumns._ID} = ${task.id}", null)
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
}