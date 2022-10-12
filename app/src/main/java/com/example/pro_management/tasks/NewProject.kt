package com.example.pro_management.tasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.pro_management.NewTask
import com.example.pro_management.R

class NewProject : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_project)

        init()
    }

    private fun init(){
        findViewById<ImageButton>(R.id.btn_new_pro_back).setOnClickListener {
            onBack()
        }
        findViewById<Button>(R.id.btn_next_step).setOnClickListener {
            onNextStep()
        }
    }

    fun onBack(){
        val intent = Intent()
        intent.putExtra("new project", false)
        setResult(205, intent)
        finish()
    }

    private fun onNextStep(){
        val project = findViewById<EditText>(R.id.edit_new_project)

        val des = findViewById<EditText>(R.id.edit_new_pro_des)

        if(project.text.toString().isEmpty()){
            Toast.makeText(this, "Project need a name", Toast.LENGTH_SHORT).show()
            return
        }
        if(des.text.toString().isEmpty()){
            Toast.makeText(this, "Description must not be none", Toast.LENGTH_SHORT).show()
            return
        }


        val intent = Intent()
        intent.putExtra("project name",project.text.toString() )
        intent.putExtra("project_des", des.text.toString())

        intent.setClass(this, NewTask::class.java)
        startActivityForResult(intent, 206)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 206 && resultCode ==207){
            var num = data?.getIntExtra("task_num", 0)
            val intent = Intent()
            intent.putExtra("task_name", num)
            setResult(205, intent)
            finish()
        }
    }
}