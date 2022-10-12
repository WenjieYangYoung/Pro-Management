package com.example.mobileapplicationdevelopmentcoursework.User.projectManager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.mobileapplicationdevelopmentcoursework.Adapter.project.projectAdapter
import com.example.mobileapplicationdevelopmentcoursework.Adapter.task.Task
import com.example.pro_management.MainActivity

import com.example.pro_management.databinding.FragmentProjectManagerBinding
import com.example.pro_management.util.taskManagerActivity
import com.example.pro_management.tasks.Project
import com.google.android.material.snackbar.Snackbar

const val ProjectNameManager = "com.example.openDataCoursework.ProjectNameManager"
const val ProjectDeadlineManager = "com.example.openDataCoursework.ProjectDeadlineManager"
const val ProjectStatusManager = "com.example.openDataCoursework.ProjectStatusManager"
const val ProjectDescriptionManager = "com.example.openDataCoursework.ProjectDescriptionManager"
const val ProjectManagerManager = "com.example.openDataCoursework.ProjectManagerManager"

class projectManagerFragment : Fragment() {

    private var _binding: FragmentProjectManagerBinding? = null
    private lateinit var projectListManager: ArrayList<Project>
    private lateinit var taskListManager: ArrayList<Task>
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectManagerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        projectListManager = ArrayList()

        var asd = activity as MainActivity
        for(i in asd.proLs){
            projectListManager.add(i)
        }

        binding.listview.isClickable = true
        binding.listview.adapter = projectAdapter(requireActivity(), projectListManager)
        binding.listview.setOnItemClickListener { parent,view,position, id ->
            val projectName = projectListManager[position].project_name
            val projectDeadline = projectListManager[position].project_deadline
            val projectStatus = projectListManager[position].project_status
            val projectDescription = projectListManager[position].project_description
            val projectManager = projectListManager[position].project_manager
            val projectId = projectListManager[position].project_id

            val toTask = Intent(activity, taskManagerActivity::class.java)
            toTask.putExtra(ProjectNameManager,projectName)
            toTask.putExtra(ProjectDeadlineManager,projectDeadline)
            toTask.putExtra(ProjectStatusManager,projectStatus)
            toTask.putExtra(ProjectDescriptionManager,projectDescription)
            toTask.putExtra(ProjectManagerManager,projectManager)
            toTask.putExtra("project_id", projectId)
            startActivityForResult(toTask, 500)
        }

        return root
    }
}
