package com.example.pro_management.util

import java.util.*

// Define event and message types

class MainmenuItem(val msg:String){
}

class MessageSortBy(val msg:String) {
}

//login result       loginNet -> login activity
class MessageLoginResult(
    val succ: Boolean,

    val info:String,

    val code:Number,
)

// Message type for ordinary request
class MessageRequest(
    val request: String,
    val resCode: Number,
    val msg: String,
)

class NewTaskApply(
    val taskName:String,
    val memberEmail:String,
    val deadline:String,
    val description:String,
)

class onQuerySuccess(
    val code:Int,
    val msg: String
)

class RequestFieldMsg(
    val msg: String
)

class ProQueryResult(
    val code: Int,
    val msg: String)

class AutoLoginRes(
    val msg:String,
    val code:Int
)

class Signout(
    val code: Int,
    val msg: String
)

class MsgA(
    val code: Int
)

class TaskCreated(
val code:Int
)

class TaskAciClose(
    val code:Int
)

class LoadData(
    val code:Int
)

class TaskCompleted(
    val task_id:Int
)