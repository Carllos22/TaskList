package com.example.tasklist.activities

import android.app.Activity
import android.content.Intent
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

            if (taskName.isNotEmpty()) {
                val task = Task(-1, taskName)
                if (selectedCategory != null) {
                    task.categoryId = selectedCategory.id
                }
                taskDAO.insert(task)
                Toast.makeText(this, "Task successfully saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }

        binding.createCategoryButton.setOnClickListener {
            val intent = Intent(this, CreateCategoryActivity::class.java)
            startActivityForResult(intent, CREATE_CATEGORY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CATEGORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val categoryId = data?.getIntExtra("categoryId", -1)
            if (categoryId != -1) {
                loadCategories()
                binding.categorySpinner.setSelection(findCategoryIndexById(categoryId))
            }
        }
    }

    private fun loadCategories() {
        categories = categoryDAO.findAll()
        val categoryNames = categories.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }

    private fun findCategoryIndexById(categoryId: Int): Int {
        return categories.indexOfFirst { it.id == categoryId }
    }

    companion object {
        private const val CREATE_CATEGORY_REQUEST_CODE = 100
    }
}