package com.example.tasklist.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.adapters.TaskAdapter
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityMainBinding
//import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
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
            taskDAO.delete(taskList[it])
            Toast.makeText(this, "Task successfully deleted", Toast.LENGTH_SHORT).show()
            loadData()
        }, {
            val task = taskList[it]
            task.done = !task.done
            taskDAO.update(task)
            loadData()
        })

        binding.recyclerView.adapter = adapter
        //binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)


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