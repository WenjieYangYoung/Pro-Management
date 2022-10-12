package com.example.mobileapplicationdevelopmentcoursework.Adapter.task

class Task (
    val task_id: Int,
    var task_name: String,
    val assigned_user_id: Int,
    val parent_project_id: Int,
    val importance: Int,
    val task_requirement: String,
    var task_status: String,
    var project_status: String,
    var task_deadline: String,
    )