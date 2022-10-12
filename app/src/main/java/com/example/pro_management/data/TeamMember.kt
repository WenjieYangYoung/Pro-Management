package com.example.pro_management.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_member")
data class TeamMember(

    //same as userId
    @PrimaryKey
    val id:String,

    @ColumnInfo(name = "username")
    val username:String,

    @ColumnInfo(name = "fullname")
    val fullname:String,

    @ColumnInfo(name = "email")
    val email:String,

    @ColumnInfo(name = "skill_description")
    val skillDescription:String,
)