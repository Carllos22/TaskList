package com.example.tasklist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.tasklist.data.daos.CategoryDAO
import com.example.tasklist.data.entities.Task
import com.example.tasklist.data.daos.TaskDAO
import com.example.tasklist.data.entities.Category
import com.example.tasklist.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding

    private lateinit var taskDAO: TaskDAO

    private lateinit var categoryDAO: CategoryDAO

    private lateinit var categories: List<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskDAO = TaskDAO(this)
        categoryDAO = CategoryDAO(this)

        loadCategories()

        binding.saveButton.setOnClickListener {
            val categoryIndex = binding.categorySpinner.selectedItemPosition
            val selectedCategory = if (categoryIndex != AdapterView.INVALID_POSITION) {
                categories[binding.categorySpinner.selectedItemPosition]
            } else {
                null
            }
            val taskName = binding.nameEditTextView.text.toString()
            val task = Task(-1, taskName)
            taskDAO.insert(task)
            Toast.makeText(this, "Task successfully saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadCategories() {
        categories = categoryDAO.findAll()
        val categoryNames = categories.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }
}