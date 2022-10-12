package com.example.mobileapplicationdevelopmentcoursework.Adapter.project

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.pro_management.R
import com.example.pro_management.tasks.Project

class projectAdapter(private val context: FragmentActivity, private val arrayList: ArrayList<Project>) : ArrayAdapter<Project>(context,
    R.layout.project_list,arrayList) {

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.project_list,null)
        val projectName : TextView = view.findViewById(R.id.projectName)
        val projectDeadline : TextView = view.findViewById(R.id.projectDeadline)
        val projectStatus : TextView = view.findViewById(R.id.projectStatus)
        val projectManager : TextView = view.findViewById(R.id.projectManager)
        val projectMember : TextView = view.findViewById(R.id.projectMember)
        val projectImage : ImageView = view.findViewById(R.id.ProjectImage1)

        projectName.text = arrayList[position].project_name
        projectDeadline.text = arrayList[position].project_deadline
        projectStatus.text = arrayList[position].project_status
        projectManager.text = arrayList[position].project_manager
        projectMember.text = arrayList[position].project_member

        if (arrayList[position].project_status == "ongoing"){
            projectName.setTextColor(Color.parseColor("#FF000000"))
            projectDeadline.setTextColor(Color.parseColor("#CC0000"))
            projectStatus.setTextColor(Color.parseColor("#CC0000"))
            projectImage.setImageResource(R.drawable.ongoing)
            notifyDataSetChanged()
        }
        else if (arrayList[position].project_status == "complete"){
            projectName.setTextColor(Color.parseColor("#3FE2C5"))
            projectDeadline.setTextColor(Color.parseColor("#FF000000"))
            projectStatus.setTextColor(Color.parseColor("#3FE2C5"))
            projectImage.setImageResource(R.drawable.completed_1)
            notifyDataSetChanged()
        }
        return view
    }
}

