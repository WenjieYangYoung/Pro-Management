package com.example.pro_management.login

import com.example.pro_management.Networks
import java.lang.StringBuilder
import java.security.MessageDigest


class LoginHelper {
    companion object{
        lateinit var CodeMap: Map<Number, String>

        public fun getCodeMsg(code: Number):String{
            if( CodeMap.count() <= 0){
                initCodeMap()
            }

            return CodeMap.getValue(code)
        }

        public fun isValidEmail(email: String) :Boolean{
            val email_pattern = """\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*""".toRegex()

            return email_pattern.matches(email)
        }

        /**
         * longer than 8 char
         * has to include at least one letter and one number
         */
        public fun isValidPasswd(passwd: String):Boolean{
            val passwd_pattern = """^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}${'$'}""".toRegex()

            return passwd_pattern.matches(passwd)
        }

        private fun initCodeMap(){
            CodeMap = mapOf(
                -1 to "unknown error",
                0 to "login success",
            )
        }

        public fun SHA1(input: String):String{
            val digest = MessageDigest.getInstance("SHA-1")
            val result = digest.digest(input.toByteArray())

            return toHex(result)
        }

        private fun toHex(input: ByteArray): String{
            val result = with(StringBuilder()){
                input.forEach {
                    val hex = it.toInt() and (0xFF)
                    val hex_str = Integer.toHexString(hex)
                    if(hex_str.length == 1){
                        this.append("0").append(hex_str)
                    }else{
                        this.append(hex_str)
                    }
                }

                this.toString()
            }

            return result
        }
    }
}