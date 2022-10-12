package com.example.pro_management.tasks

import java.time.LocalDateTime

data class Project1(
    val project_id:Int,
    val project_name:String,
    val project_description: String,
    var project_status: String,
    val project_manager: String,
    val project_member: String,
    val project_deadline: LocalDateTime
)