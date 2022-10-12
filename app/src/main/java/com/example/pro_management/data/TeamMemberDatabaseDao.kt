package com.example.pro_management.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TeamMemberDatabaseDao {
    @Insert
    fun insert(member: TeamMember)

    @Query("SELECT * FROM team_member WHERE id=:key")
    fun get(key: String): TeamMember?
}