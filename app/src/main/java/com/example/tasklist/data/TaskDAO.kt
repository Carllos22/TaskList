package com.example.tasklist.data
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.tasklist.utils.DatabaseManager
class TaskDAO(context: Context) {
    private val databaseManager: DatabaseManager = DatabaseManager(context)
    fun insert(task: Task) {
        val db = databaseManager.writableDatabase
        val values = ContentValues()
        values.put(Task.COLUMN_NAME_TITLE, task.name)
        values.put(Task.COLUMN_NAME_DONE, task.done)

        val newRowId = db.insert(Task.TABLE_NAME, null, values)
        task.id = newRowId.toInt()

        db.close()
    }

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

        db.close()
    }

    fun delete(task: Task) {
        val db = databaseManager.writableDatabase

        val deletedRows = db.delete(Task.TABLE_NAME, "${BaseColumns._ID} = ${task.id}", null)

        db.close()
    }

    fun find(id: Int) : Task? {
        val db = databaseManager.readableDatabase
        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME_TITLE, Task.COLUMN_NAME_DONE)
        val cursor = db.query(
            Task.TABLE_NAME,
            projection,
            "${BaseColumns._ID} = $id",
            null,
            null,
            null,
            null
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
    fun findAll() : List<Task> {
        val db = databaseManager.readableDatabase
        val projection = arrayOf(BaseColumns._ID, Task.COLUMN_NAME_TITLE, Task.COLUMN_NAME_DONE)
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