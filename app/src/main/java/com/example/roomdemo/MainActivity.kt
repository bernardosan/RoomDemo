package com.example.roomdemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding?.btnAdd?.setOnClickListener {
            addRecord(employeeDao)
        }

        lifecycleScope.launch {
            employeeDao.fetchAllEmployees().collect {
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, employeeDao)
            }
        }
    }

    private fun setupListOfDataIntoRecyclerView(
        employeesList: ArrayList<EmployeeEntity>,
        employeeDao: EmployeeDao
    ) {

        if (employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeesList)

            // Set the LayoutManager that this RecyclerView will use.
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {

            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }


    //method for saving records in database
    fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()
        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()

            }
        } else {
            Toast.makeText(
                applicationContext,
                "Name or Email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}