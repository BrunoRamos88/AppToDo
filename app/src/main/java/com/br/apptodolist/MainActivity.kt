package com.br.apptodolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.br.apptodolist.databinding.ActivityMainBinding
import com.br.apptodolist.ui.AddTaskActivity
import com.br.apptodolist.ui.TaskListAdapter
import com.br.apptodolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter

        updateList()

        insertListeners()

/*
        val getButton = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {

                binding.rvTasks.adapter = adapter
            }
        )
        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener{
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

 */
    }


    private fun insertListeners() {
        binding.fab.setOnClickListener{
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()

    }

        private fun updateList() {
            val list = TaskDataSource.getList()
            binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
            else View.GONE

            adapter.submitList(list)
        }



    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}

