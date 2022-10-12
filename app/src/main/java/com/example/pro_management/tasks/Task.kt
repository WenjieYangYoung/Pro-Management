package com.example.pro_management.tasks

data class Task(
    val task_id: Int,
    val task_name: String,
    val assigned_user_name: String,
    val parent_project_id:Int,
    val importance:Int,
    val task_requirement: String,
    var task_status: String,
    var task_deadline:String,
    var project_status:String
)