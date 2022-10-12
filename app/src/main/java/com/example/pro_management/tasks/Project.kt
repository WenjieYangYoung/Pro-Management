package com.example.pro_management.tasks

data class Project(
    val project_id:Int,
    val project_name:String,
    val project_description: String,
    var project_status: String,
    val project_manager: String,
    val project_member: String,
    var project_deadline: String
)


