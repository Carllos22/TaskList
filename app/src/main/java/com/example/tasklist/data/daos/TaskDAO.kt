package com.example.tasklist.data.daos

import android.content.ContentValues
import android.content.Context
import com.example.tasklist.data.entities.Task
import com.example.tasklist.utils.DatabaseManager

class TaskDAO(context: Context) {

    private val databaseManager: DatabaseManager = DatabaseManager(context)

    fun insert(task: Task) {
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, task.done)
        values.put(Task.COLUMN_NAME_CATEGORY, task.categoryId)

        val newRowId = db.insert(Task.TABLE_NAME, null, values)
        task.id = newRowId.toInt()

        db.close()
    }

    fun update(task: Task) {
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, task.done)
        values.put(Task.COLUMN_NAME_CATEGORY, task.categoryId)

        val updatedRows = db.update(
            Task.TABLE_NAME,
            values,
            "${DatabaseManager.COLUMN_NAME_ID} = ${task.id}",
            null
        )

        db.close()
    }

    fun delete(task: Task) {
        val db = databaseManager.writableDatabase

        val deletedRows = db.delete(Task.TABLE_NAME, "${DatabaseManager.COLUMN_NAME_ID} = ${task.id}", null)

        db.close()
    }

    fun find(id: Int) : Task? {
        val db = databaseManager.readableDatabase

        val projection = Task.COLUMN_NAMES

        val cursor = db.query(
            Task.TABLE_NAME,
            projection,
            "${DatabaseManager.COLUMN_NAME_ID} = $id",
            null,
            null,
            null,
            null
        )

        var task: Task? = null
        if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) == 1
            val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))
            task = Task(id, name, done, categoryId)
        }
        cursor.close()
        db.close()
        return task
    }
    fun findAll() : List<Task> {
        val db = databaseManager.readableDatabase

        val projection = Task.COLUMN_NAMES

        val cursor = db.query(
            Task.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            "${Task.COLUMN_NAME_DONE} ASC"
        )

        var tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.COLUMN_NAME_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) == 1
            val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))
            val task = Task(id, name, done, categoryId)
            tasks.add(task)
        }
        cursor.close()
        db.close()
        return tasks
    }
}