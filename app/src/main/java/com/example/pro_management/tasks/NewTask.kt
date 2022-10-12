package com.example.pro_management

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.pro_management.data.Task
import com.example.pro_management.tasks.NewTaskFrag
import com.example.pro_management.user.CurrentUser
import com.example.pro_management.util.NewTaskApply
import com.example.pro_management.util.RequestFieldMsg
import com.example.pro_management.util.TaskCreated
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class NewTask : AppCompatActivity() {

    val newTaskFrag = NewTaskFrag()
    lateinit var taskbox:LinearLayout

    val tasks = mutableListOf<Task>()

    var task_ls = mutableMapOf<Button, LinearLayout>()
    var task_name_map = mutableMapOf<Button, String>()

     var pro_name: String = ""

     var description:String =""

     var project_id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        EventBus.getDefault().register(this)

        pro_name = intent.getStringExtra("project name")!!
        description = intent.getStringExtra("project_des")!!


        init()
    }

    private fun init(){
        taskbox = findViewById<LinearLayout>(R.id.new_task_box)

        findViewById<Button>(R.id.btn_new_task_next).setOnClickListener{
            onNewTask()
        }
        findViewById<Button>(R.id.btn_new_task_finish).setOnClickListener{
            onFinish()
        }
    }

    fun onFinish(){

        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
        var min_t = LocalDateTime.parse("1900.01.01 00:00", formatter)

        var email_total = ""

        for(i in tasks){
            val t = LocalDateTime.parse(i.str_deadline, formatter)
            if(t.isAfter(min_t)){
                min_t = t
            }

            email_total += (i.member_email)
            if(tasks.indexOf(i) != tasks.count() - 1){
                email_total += ','
            }
        }

        val temp = min_t.toString().toCharArray()
        temp[10] = ' '



        val url = "http://35.246.127.252:5000/projectcreate"

        if(pro_name.isNotEmpty() && description.isNotEmpty()){
            val reqBody = FormBody.Builder()
                .add("project name", pro_name)
                .add("project description",description)
                .add("project deadline", String(temp))
                .add("project member", email_total)
                .build()

            val request = Request.Builder()
                .addHeader("Cookie", CurrentUser.getUser().getSession())
                .url(url)
                .header("Connection", "close")
                .post(reqBody)
                .build()

            val client = OkHttpClient.Builder().retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS).build()

            client.newCall(request)
                .enqueue(object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val res:String? = response.body?.string()

                        var res_pro:ProjectCreateResponse
                        val gson = Gson()
                        res_pro = gson.fromJson(res, ProjectCreateResponse::class.java)

                        if(res!=null){
                            if(res_pro.result == "success"){
                                project_id = res_pro.project_id
                                createTasks()
                            }else{
                                EventBus.getDefault().post(RequestFieldMsg(res_pro.info))
                            }
                        }
                    }

                })
        }
    }

    fun createTasks(){
        val url = "http://35.246.127.252:5000/taskcreate"

        if(project_id == 0){
            return
        }

        for(task in tasks){
            val req_body = FormBody.Builder()
                .add("task name", task.task_name)
                .add("parent project id", project_id.toString())
                .add("task requirement", task.description)
                .add("assigned user account", task.member_email)
                .add("deadline", task.str_deadline + ":00")
                .build()

            val request = Request.Builder()
                .addHeader("Cookie", CurrentUser.getUser().getSession())
                .url(url)
                .header("Connection", "close")
                .post(req_body)
                .build()

            val client = OkHttpClient.Builder().retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS).build()

            client.newCall(request).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val res:String? = response.body?.string()

                    val gson = Gson()
                    val resRes = gson.fromJson(res, TaskCreateResponse::class.java)
                    if(!res.isNullOrEmpty()){
                        if(resRes.result == "success"){
                            EventBus.getDefault().post(TaskCreated(0))
                        }
                    }
                }

            })

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTaskAcClosed(event: TaskCreated){
        val intent = Intent()
        intent.putExtra("task_num", tasks.count())
        setResult(207, intent)
        finish()
    }

    fun onNewTask(){
        supportFragmentManager.beginTransaction().add(newTaskFrag, NewTaskFrag.TAG).commit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onProCreateField(event:RequestFieldMsg){
        Toast.makeText(this, event.msg, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("ResourceAsColor")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onTaskApply(event: NewTaskApply){
        supportFragmentManager.beginTransaction().remove(newTaskFrag).commit()

        //supportFragmentManager.beginTransaction().add(newTaskFrag, UserFragment.TAG).commit()
        val nameView = TextView(this)

        val innerLayout = LinearLayout(this)




        with(nameView){
            setTextSize(20.0F)
            width = 360
            setText(event.taskName)
            gravity = Gravity.CENTER_VERTICAL
        }

        val deadline = TextView(this)

        with(deadline){
            setText(event.deadline)
            width = 320
            setTextSize(15.0F)
            gravity = Gravity.START
            gravity = Gravity.CENTER_VERTICAL
        }

        val delete_btn = Button(this)
        delete_btn.setOnClickListener{
            onDetele(it)
        }

        with(delete_btn){
            setBackgroundColor(R.color.purple_500)

            setText("Delete")
        }

        with(innerLayout){
            setHorizontalGravity(LinearLayout.HORIZONTAL)
            setBackgroundColor(R.color.dark_mat)
        }

        try{
            val ly_para = ViewGroup.MarginLayoutParams(innerLayout.width-20, innerLayout.height-20)
            ly_para.setMargins(20,0,10,0)
            nameView.layoutParams = ly_para

            val btn_ly_para = ViewGroup.MarginLayoutParams(innerLayout.width-20, innerLayout.height-20)
            btn_ly_para.setMargins(20, 10, 10, 10)
            delete_btn.layoutParams = btn_ly_para

        }catch (e:Exception){
            print((e.message))
        }

        innerLayout.addView(nameView)
        innerLayout.addView(deadline)
        innerLayout.addView(delete_btn)


        taskbox.addView(innerLayout)

        tasks.add(Task(event.taskName, event.memberEmail, event.deadline, event.description))
        task_ls[delete_btn] = innerLayout
        task_name_map[delete_btn] = event.taskName
    }



    fun onDetele(view: View){
        taskbox.removeView(task_ls[view])
        val name = task_name_map[view]
        task_name_map.remove(view)
        for(i in tasks){
            if(i.task_name == name){
                tasks.remove(i)
                break
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

data class ProjectCreateResponse(
    val result:String,
    val info:String,
    val project_id:Int
)

data class TaskCreateResponse(
    val result: String,
    val info: String,
    val task_id:Int
)