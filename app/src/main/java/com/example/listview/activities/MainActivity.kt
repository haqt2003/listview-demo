package com.example.listview.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.listview.models.Employee
import com.example.listview.adapters.EmployeeAdapter
import com.example.listview.R
import com.example.listview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), EmployeeAdapter.OnAdapterListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: EmployeeAdapter
    private val itemList: MutableList<Employee> = mutableListOf()

    private var isLongClickMode = false
    private var isSelectAll = false

    private val addEmployeeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                data?.let {
                    val employee = it.getSerializableExtra("employee") as Employee
                    itemList.add(employee)
                    adapter.notifyDataSetChanged()

                }
            }
        }

    private val editEmployeeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                data?.let {
                    val employee = it.getSerializableExtra("employee") as Employee
                    val employeeEdit = itemList.find { it.id == employee.id }
                    employeeEdit?.name = employee.name
                    employeeEdit?.department = employee.department
                    employeeEdit?.status = employee.status
                    adapter.notifyDataSetChanged()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        itemList.add(Employee("B21DCPT057", "Tran Quang Ha", "Dev", "Full-time"))
        itemList.add(Employee("B21DCPT008", "Tran Quang Huy", "Marketing", "Intern"))
        itemList.add(Employee("B21DCPT609", "Tran Quang He", "Design", "Intern"))

        adapter = EmployeeAdapter(this, itemList, this)

        with(binding) {
            lvList.adapter = adapter

            lvList.setOnItemLongClickListener { parent, view, position, id ->
                if (!isLongClickMode) {
                    isLongClickMode = true
                    tbSelect.visibility = View.VISIBLE
                    tvItemSelected.text = "0"
                    adapter.onToggleMode()
                }
                true
            }

            ivClose.setOnClickListener {
                isLongClickMode = false
                tbSelect.visibility = View.GONE
                adapter.onToggleMode()
            }

            ivSelectAll.setOnClickListener {
                isSelectAll = !isSelectAll
                if (isSelectAll) {
                    adapter.selectAll()
                } else {
                    adapter.deselectAll()
                }
            }

            etSearch.addTextChangedListener {
                val searchText = it.toString()

                if (searchText.isEmpty()) {
                    adapter.updateList(itemList)
                } else {
                    val filteredList = itemList.filter { item ->
                        item.name.contains(searchText, ignoreCase = true)
                    }.toMutableList()
                    adapter.updateList(filteredList)
                }
            }
        }

        binding.lvList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("employee", itemList[position])
            startActivity(intent)
        }

        binding.btAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            addEmployeeLauncher.launch(intent)
        }

        binding.ivDelete.setOnClickListener {
            val itemsToKeep = itemList.filter { !it.isSelected }
            if (itemsToKeep.size == itemList.size) {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show()
            } else {
                itemList.clear()
                itemList.addAll(itemsToKeep)
                adapter.notifyDataSetChanged()
                isLongClickMode = false
                binding.tbSelect.visibility = View.GONE
                adapter.onToggleMode()
                Toast.makeText(this, "Delete succesfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun updateSelectedItemCount(count: Int) {
        binding.tvItemSelected.text = "$count"
    }

    override fun editEmployee(employee: Employee) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("employee", employee)
        editEmployeeLauncher.launch(intent)
    }
}