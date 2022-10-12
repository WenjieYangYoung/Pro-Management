package com.example.pro_management.util

import android.annotation.SuppressLint
import com.example.pro_management.tasks.Project
import com.example.pro_management.tasks.Project1
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MyDateTime {
    companion object {
        val Day_Of_Week =
            arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        var date = Date()
        var calendar = Calendar.getInstance()

        public fun GetDayOfWeek(): String {
            calendar.time = date
            var index = calendar.get(Calendar.DAY_OF_WEEK) - 1
            if (index < 0) index = 0
            return Day_Of_Week[index]
        }

        @SuppressLint("SimpleDateFormat")
        public fun GetDayOfMonth():String{
            var fmt = SimpleDateFormat("yyyy-MM-dd")
            return fmt.format(date)
        }

        fun copyProtoLDT(ls_str:MutableList<Project>, ls_ldt:MutableList<Project1>){
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


            for (i in ls_str) {
                val c_ddl = i.project_deadline.toCharArray()
                c_ddl[10] = 'T'

                val ddl = LocalDateTime.parse(String(c_ddl))

                val p = Project1(i.project_id, i.project_name, i.project_description, i.project_status, i.project_manager, i.project_member, ddl)
                ls_ldt.add(p)
            }

        }

        fun copyProToStr(ls_str:MutableList<Project>, ls_ldt:MutableList<Project1>){

            for (i in ls_ldt) {
                var c_ddl = i.project_deadline.toString().toCharArray()
                c_ddl[10] = ' '
                val p = Project(i.project_id, i.project_name, i.project_description, i.project_status, i.project_manager, i.project_member, String(c_ddl))
                ls_str.add(p)
            }

        }

    }
}