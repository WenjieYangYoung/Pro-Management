package com.example.pro_management.util

import android.content.Context
import java.io.BufferedInputStream



class AppConfig {
    companion object{
        val config= mutableMapOf<String, String>()

        fun getServerWithPort():String{
            val server = config["remote_server"]!!
            val port = config["port"]!!

            return "$server:$port"
        }

        fun init(ctx: Context){
            var lines: List<String>?

            try {
                ctx.assets.open("appConfig").use{
                            ins -> BufferedInputStream(ins).use{
    //                        bis -> text = bis.reader().readText()
                            bis -> lines = bis.reader().readLines()
                    }
                }
            }finally {

            }

            for(l in lines!!) {
                if ( l == "" || l[0] == '#' ) {
                    continue
                } else {
                    var tempL = l.split('=')
                    config.put(tempL[0].trim(), tempL[1].trim())
                }
            }
        }
    }



}