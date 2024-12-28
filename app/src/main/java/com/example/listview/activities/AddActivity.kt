package com.example.listview.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listview.models.Employee
import com.example.listview.R
import com.example.listview.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    private val binding: ActivityAddBinding by lazy {
        ActivityAddBinding.inflate(layoutInflater)
    }
    private val departments = arrayOf("Dev", "Marketing", "Design")
    private val statuses = arrayOf("Full-time", "Intern")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_add)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val departmentAdapter = ArrayAdapter(this, R.layout.spinner_item, departments)
        val statusAdapter = ArrayAdapter(this, R.layout.spinner_item, statuses)

        binding.spDepartment.adapter = departmentAdapter
        binding.spStatus.adapter = statusAdapter

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btAdd.setOnClickListener {
            val id = binding.etId.text.toString()
            val name = binding.etName.text.toString()
            val department = binding.spDepartment.selectedItem.toString()
            val status = binding.spStatus.selectedItem.toString()

            if (id.isNotEmpty() && name.isNotEmpty() && department.isNotEmpty() && status.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("employee", Employee(id, name, department, status))
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}