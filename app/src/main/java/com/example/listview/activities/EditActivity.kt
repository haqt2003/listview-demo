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
import com.example.listview.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private val binding: ActivityEditBinding by lazy {
        ActivityEditBinding.inflate(layoutInflater)
    }

    private val departments = arrayOf("Dev", "Marketing", "Design")
    private val statuses = arrayOf("Full-time", "Intern")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_edit)) { v, insets ->
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

        val intent = intent
        val employee = intent.getSerializableExtra("employee") as Employee
        binding.etId.setText(employee.id)
        binding.etName.setText(employee.name)
        binding.spDepartment.setSelection(departments.indexOf(employee.department))
        binding.spStatus.setSelection(statuses.indexOf(employee.status))

        binding.btSave.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(
                "employee",
                Employee(
                    binding.etId.text.toString(),
                    binding.etName.text.toString(),
                    binding.spDepartment.selectedItem.toString(),
                    binding.spStatus.selectedItem.toString()
                )
            )
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}