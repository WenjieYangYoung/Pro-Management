package com.example.pro_management.tasks

import com.example.pro_management.user.CurrentUser
import com.example.pro_management.util.ProQueryResult
import com.example.pro_management.util.onQuerySuccess
import com.google.gson.Gson
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import java.io.IOException

class TaskHelper {

    companion object{
         fun queryPro(proLs:MutableList<Project>){
             val corrUser = CurrentUser.getUser()

             val request = Request.Builder()
                 .addHeader("Cookie", CurrentUser.getUser().getSession())
                 .url("http://35.246.127.252:5000/projectquery")
                 .header("Connection", "close")
                 .get()
                 .build()

             OkHttpClient()
                 .newCall(request)
                 .enqueue(object :Callback{
                     override fun onFailure(call: Call, e: IOException) {
                     }

                     override fun onResponse(call: Call, response: Response) {
                         var res:String? = ""
                         try{
                             res = response.body?.string()
                         }catch (e:IOException){
                             queryPro(proLs)
                             return
                         }

                         val gson = Gson()
                         val proJson = gson.fromJson(res, ProQueryResponse::class.java)

                         if(res!!.isNotEmpty()){
                             if(proJson.result == "success"){

                                 // Add project to the list
                                 for(i in proJson.data){
                                    proLs.add(i)
                                 }

                                 EventBus.getDefault().post(ProQueryResult(0, "query"))
                                 println("\n******************* Project Query Success ******************\n")

                             }else{
                                 EventBus.getDefault().post(ProQueryResult(-1, "query"))
                             }
                         }

                     }

                 })
        }

        fun queryTask(project_id:Int, taskLs: MutableList<Task>){


                val corrUser = CurrentUser.getUser()
                val session = corrUser.getSession()

                val req_body = FormBody.Builder()
                    .add("project id", project_id.toString())
                    .build()


                var url = "http://35.246.127.252:5000/taskquerymember"
                if(CurrentUser.getUser().isManager()){
                    url = "http://35.246.127.252:5000/taskquerymanager"
                }

                val request = Request.Builder()
                    .addHeader("Cookie", CurrentUser.getUser().getSession())
                    .url(url)
                    .header("Connection", "close")
                    .post(req_body)
                    .build()


            val client = OkHttpClient.Builder().retryOnConnectionFailure(true).build()
                client
                    .newCall(request)
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            TODO("Not yet implemented")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            var res:String? = ""
                            try{
                                res = response.body?.string()
                            }catch (e:IOException){
                                queryTask(project_id, taskLs)
                                return
                            }

                            val gson = Gson()
                            val res_data = gson.fromJson(res, TaskQueryResponse::class.java)

                            if (!res.isNullOrEmpty()) {
                                if (res_data.result == "success") {
                                    for (i in res_data.data) {
                                        taskLs.add(i)
                                    }

                                    EventBus.getDefault().postSticky(onQuerySuccess(0, "succ"))

                                }
                            }
                        }
                    })
            }
        }
}

data class ProQueryResponse(
    val result:String,
    val info:String,
    val data: List<Project>
)

data class TaskQueryResponse(
    val result:String,
    val info:String,
    val data:MutableList<Task>
)