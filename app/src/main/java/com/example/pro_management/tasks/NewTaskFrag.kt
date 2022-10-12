package com.example.pro_management.tasks

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pro_management.R
import com.example.pro_management.util.NewTaskApply
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.greenrobot.eventbus.EventBus

class NewTaskFrag: BottomSheetDialogFragment() {

    lateinit var activityView:View

    companion object{
        const val TAG = "NewTask"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{

        activityView = inflater.inflate(R.layout.fragment_new_task, container, false)

        return activityView
    }

    override fun onResume() {
        super.onResume()
        val display = activity?.windowManager?.defaultDisplay

        val metrics = DisplayMetrics()
        display?.getMetrics(metrics)

        super.getDialog()?.window?.setLayout(-1, -1)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init(){
        activityView.findViewById<Button>(R.id.ben_new_task_finish).setOnClickListener{
            onFinish()
        }

        activityView.findViewById<EditText>(R.id.edit_new_task_name).setText("")
        activityView.findViewById<EditText>(R.id.edit_new_task_email).setText("")
        activityView.findViewById<EditText>(R.id.edit_new_task_date).setText("")
        activityView.findViewById<EditText>(R.id.edit_new_task_time).setText("")
        activityView.findViewById<EditText>(R.id.edit_new_task_descri).setText("")

    }

    private fun onFinish(){

        val str_taskname = activityView.findViewById<EditText>(R.id.edit_new_task_name).text.toString()
        val str_memberEmail = activityView.findViewById<EditText>(R.id.edit_new_task_email).text.toString()
        val str_date = activityView.findViewById<EditText>(R.id.edit_new_task_date).text.toString()
        val str_time = activityView.findViewById<EditText>(R.id.edit_new_task_time).text.toString()
        val str_des = activityView.findViewById<EditText>(R.id.edit_new_task_descri).text.toString()

        if(str_taskname.isEmpty()){
            Toast.makeText(activity, "Invalid task name", Toast.LENGTH_SHORT).show()
            return
        }
        if(str_memberEmail.isEmpty()){
            Toast.makeText(activity, "Invalid email", Toast.LENGTH_SHORT).show()
            return
        }
        if(str_date.isEmpty() || str_time.isEmpty()){
            Toast.makeText(activity, "Invalid datetime", Toast.LENGTH_SHORT).show()
            return
        }

        EventBus.getDefault().post(NewTaskApply(str_taskname, str_memberEmail, "$str_date $str_time", str_des))

    }
}