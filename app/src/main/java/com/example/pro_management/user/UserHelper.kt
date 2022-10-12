package com.example.pro_management.user

import com.example.pro_management.MainActivity

class UserHelper {
    companion object{

        //load user info from disk
        public fun loadLocalUser(user: CurrentUser, activity: MainActivity):Boolean{
            //only test
            return !user.getUsername().isEmpty()

        }
    }
}