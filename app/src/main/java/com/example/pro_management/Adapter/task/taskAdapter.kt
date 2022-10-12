package com.example.pro_management.Adapter.task

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.ToggleButton
import com.example.pro_management.tasks.Task
import com.example.pro_management.R
import com.example.pro_management.user.CurrentUser
import com.example.pro_management.util.TaskCompleted
import com.google.gson.Gson
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.lang.Exception


class taskAdapter(private val context: Activity, private val arrayList: ArrayList<Task>) : ArrayAdapter<Task>(context,
    R.layout.task_list,arrayList) {

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.task_list,null)
        val taskName : TextView = view.findViewById(R.id.taskName)
        val taskDeadline : TextView = view.findViewById(R.id.taskDeadline)
        val taskStatus : ToggleButton = view.findViewById(R.id.taskStatus)
        if(CurrentUser.getUser().isManager()){
            taskStatus.isClickable = false
        }
        val teamMember : TextView? = view.findViewById(R.id.taskMember)
        var count : Int? = null

        taskName.text = arrayList[position].task_name
        taskDeadline.text = arrayList[position].task_deadline
        if (teamMember != null) {
            teamMember.text = arrayList[position].assigned_user_name
        }

        if (arrayList[position].task_status == "assigned"){
            taskDeadline.setTextColor(Color.parseColor("#CC0000"))
            taskName.setTextColor(Color.parseColor("#FF000000"))
            taskStatus.setTextColor(Color.parseColor("#CC0000"))
            taskStatus.text = arrayList[position].task_status
            notifyDataSetChanged()
        } else {
            if (arrayList[position].task_status == "complete") {
                taskName.setTextColor(Color.parseColor("#3FE2C5"))
                taskDeadline.setTextColor(Color.parseColor("#FF000000"))
                taskStatus.setTextColor(Color.parseColor("#3FE2C5"))
                taskStatus.text = arrayList[position].task_status
                notifyDataSetChanged()
            }
        }

        taskStatus.setOnCheckedChangeListener { _, isChecked ->
            count = 0
            val checked_item = arrayList[position].task_id
            taskCompleted(checked_item)
            if (isChecked) {
                arrayList[position].task_status = "complete"
                taskStatus.setTextColor(Color.parseColor("#3FE2C5"))
            } else {
                arrayList[position].task_status = "assigned"
                taskStatus.setTextColor(Color.parseColor("#CC0000"))
            }
            // Judge if all the tasks are complete
            for (i in arrayList) {
                if (i.task_status == "complete") {
                    count = count!! + 1
                }
            }
            if (count!! < arrayList.size) {
                arrayList[position].project_status = "ongoing"
            } else {
                arrayList[position].project_status = "complete"
            }
            notifyDataSetChanged()
        }
        return view
    }

    private fun taskCompleted(checkedItem: Int) {
        val url = "http://35.246.127.252:5000/taskfinish"

        val req_body = FormBody.Builder()
            .add("task id", checkedItem.toString())
            .build()

        val request = Request.Builder()
            .addHeader("Cookie", CurrentUser.getUser().getSession())
            .url(url)
            .header("Connection", "close")
            .post(req_body)
            .build()


        val client = OkHttpClient.Builder().retryOnConnectionFailure(true).build()
        client.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                var res:String?

                try{
                    res = response.body?.string()
                }catch (e:Exception){
                    taskCompleted(checkedItem)
                    return
                }

                val gson = Gson()
                val finish_res = gson.fromJson(res, TaskFinishedResponse::class.java)

                if(!res.isNullOrEmpty()){
                    if(finish_res.result == "true"){
                        println("******************* Task Completed ***********************")
                        EventBus.getDefault().post(TaskCompleted(checkedItem))
                    }
                }
            }

        })

    }
}