package com.example.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)

    @Query("SELECT * FROM `EMPLOYEE-TABLE`")
    fun fetchAllEmployees():Flow<List<EmployeeEntity>>
    // Flow removes the necessity to always notify the recycler view that data changed

    @Query("SELECT * FROM `EMPLOYEE-TABLE` where id=:id")
    fun fetchEmployeeById(id:Int):Flow<EmployeeEntity>

}