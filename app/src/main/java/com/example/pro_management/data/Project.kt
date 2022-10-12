package com.example.pro_management.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/***
 * Table for quick list of Projects
 * further information needs to request from the server
 */

@Entity
data class Project(
    @PrimaryKey val id:String,

    val projectName:String,

    val projectStatus:String,

    val projectDeadline:String,

    val projectManagerId:String,

    val projectManagerName:String,
)
