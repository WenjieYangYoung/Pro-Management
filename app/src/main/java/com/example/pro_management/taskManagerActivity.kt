package com.example.pro_management.util

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.example.pro_management.Adapter.task.taskAdapter
import com.example.pro_management.R
import com.example.pro_management.databinding.ActivityTaskManagerBinding
import com.example.pro_management.tasks.Task
import com.example.pro_management.tasks.TaskHelper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.time.format.DateTimeFormatter

const val ProjectNameManager = "com.example.openDataCoursework.ProjectNameManager"
const val ProjectDeadlineManager = "com.example.openDataCoursework.ProjectDeadlineManager"
const val ProjectStatusManager = "com.example.openDataCoursework.ProjectStatusManager"
const val ProjectDescriptionManager = "com.example.openDataCoursework.ProjectDescriptionManager"
const val ProjectManagerManager = "com.example.openDataCoursework.ProjectManagerManager"
const val TaskListManager = "com.example.openDataCoursework.TaskListManager"

class taskManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskManagerBinding
    private lateinit var taskListManager: ArrayList<Task>
    private lateinit var filterResult: ArrayList<Task>
    private var listView: ListView? = null
    val taskLs = ArrayList<com.example.pro_management.tasks.Task>()
    private var mAdapter: taskAdapter? = null
    var task_com_num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityTaskManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EventBus.getDefault().register(this)

        val projectName = intent.getStringExtra(ProjectNameManager)
        val projectDeadline = intent.getStringExtra(ProjectDeadlineManager)
        val projectStatus = intent.getStringExtra(ProjectStatusManager)
        val projectDescription = intent.getStringExtra(ProjectDescriptionManager)
        val projectManager = intent.getStringExtra(ProjectManagerManager)

        binding.projectName.text = projectName
        binding.projectDeadline.text = projectDeadline
        binding.projectStatus.text = projectStatus
        binding.projectManager.text = projectManager
        binding.projectDescription.text = projectDescription

        if (projectStatus == "complete") {
            binding.projectName.setTextColor(Color.parseColor("#3FE2C5"))
            binding.projectDeadline.setTextColor(Color.parseColor("#FF000000"))
            binding.projectStatus.setTextColor(Color.parseColor("#3FE2C5"))
            binding.ProjectImage1.setImageResource(R.drawable.completed_1)
        }
        else if (projectStatus == "ongoing") {
            binding.projectName.setTextColor(Color.parseColor("#FF000000"))
            binding.projectDeadline.setTextColor(Color.parseColor("#CC0000"))
            binding.projectStatus.setTextColor(Color.parseColor("#CC0000"))
            binding.ProjectImage1.setImageResource(R.drawable.ongoing)
        }
        val btn_back = findViewById<ImageButton>(R.id.btn_task_back)
        btn_back.setOnClickListener{
            onBack()
        }


        taskListManager = ArrayList()

        val project_id = intent.getIntExtra("project_id", 0)

        queryTasks(project_id)

    }

    private fun queryTasks(projectId: Int) {
        TaskHelper.queryTask(projectId, taskLs)
    }

    fun loadData(){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        task_com_num = 0
        for(i in taskLs){
            if(i.task_status == "complete"){
                task_com_num +=1
            } }
        binding.listview.adapter = taskAdapter(this, taskLs)
        filter()
        findViewById<TextView>(R.id.txt_num_com).setText("Completed: "+task_com_num.toString()+'/'+taskLs.count().toString())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSortTasks(event: onQuerySuccess) {
        println("\n******************* Task Query Success ******************\n")
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun onBack(){
        setResult(501)
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTaskCompleted(event: TaskCompleted){
        completeAddOne()
    }

    private fun completeAddOne() {
        task_com_num += 1

        findViewById<TextView>(R.id.txt_num_com).setText("Completed: "+task_com_num.toString()+'/'+taskLs.count().toString())
    }

    private fun filter(){
        // Change the "Enter" key to "Search" when the Editview gets focus.
        binding.filterSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.filterSearch.inputType = EditorInfo.TYPE_CLASS_TEXT
        binding.filterSearch.isSingleLine = true
        fun search() {
            val name = binding.filterSearch.text.toString()
            filterResult = ArrayList()
            if (TextUtils.isEmpty(name)) {
                binding.listview.adapter = taskAdapter(this, taskLs)
            } else {
                for (i in taskLs) {
                    if (i.assigned_user_name == name){
                        filterResult.add(i)
                    } }
                binding.listview.adapter = taskAdapter(this, filterResult) } }
        //add the listener of imeOptions
        binding.filterSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            false }
    }
}
