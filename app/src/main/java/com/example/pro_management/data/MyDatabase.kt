package com.example.pro_management.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TeamMember::class], version = 1, exportSchema = false)
abstract class MyDatabase:RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE:MyDatabase? = null

        fun getInstance(context: Context): MyDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, MyDatabase::class.java, "my_database")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }


                return instance
            }
        }
    }

    abstract val teamMemberDatabaseDao: TeamMemberDatabaseDao

}