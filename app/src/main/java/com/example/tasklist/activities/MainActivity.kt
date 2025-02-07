package com.example.tasklist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.adapters.TaskAdapter
import com.example.tasklist.data.entities.Task
import com.example.tasklist.data.daos.TaskDAO
import com.example.tasklist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: TaskAdapter

    private lateinit var taskList: List<Task>

    private lateinit var taskDAO: TaskDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskDAO = TaskDAO(this)

        adapter = TaskAdapter(emptyList(), {
            Toast.makeText(this, "Click on task: ${taskList[it].name}", Toast.LENGTH_SHORT).show()
        }, {
            val task = taskList[it]
            taskDAO.delete(task)
            Toast.makeText(this, "Task successfully removed", Toast.LENGTH_SHORT).show()
            loadData()
        }, {
            val task = taskList[it]
            task.done = !task.done
            taskDAO.update(task)
            loadData()
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    private fun loadData() {
        taskList = taskDAO.findAll()

        adapter.updateData(taskList)
    }
}