package com.example.listview.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.listview.R
import com.example.listview.databinding.LayoutItemBinding
import com.example.listview.models.Employee

class EmployeeAdapter(
    private val context: Context,
    private var itemList: MutableList<Employee>,
    private val listener: OnAdapterListener
) : BaseAdapter() {

    private var isLongClickMode = false

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            LayoutItemBinding.inflate(inflater, parent, false)
        } else {
            LayoutItemBinding.bind(convertView)
        }

        val item = itemList[position]

        if (isLongClickMode) {
            binding.cbSelect.visibility = View.VISIBLE
            binding.tvEdit.visibility = View.VISIBLE
        } else {
            binding.cbSelect.visibility = View.GONE
            binding.tvEdit.visibility = View.GONE
        }

        binding.tvId.text = item.id
        binding.tvName.text = item.name
        when (item.department) {
            "Dev" -> binding.ivDepartment.setImageResource(R.drawable.ic_dev)
            "Marketing" -> binding.ivDepartment.setImageResource(R.drawable.ic_marketing)
            "Design" -> binding.ivDepartment.setImageResource(R.drawable.ic_design)
        }
        when (item.status) {
            "Full-time" -> binding.ivStatus.setImageResource(R.drawable.ic_employee)
            "Intern" -> binding.ivStatus.setImageResource(R.drawable.ic_intern)
        }

        binding.cbSelect.isChecked = item.isSelected

        binding.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            item.isSelected = isChecked
            val selectedItems = itemList.filter { it.isSelected }
            listener.updateSelectedItemCount(selectedItems.size)
        }

        binding.tvEdit.setOnClickListener {
            listener.editEmployee(item)
        }

        return binding.root
    }

    fun selectAll() {
        itemList.forEach { it.isSelected = true }
        notifyDataSetChanged()
    }

    fun deselectAll() {
        itemList.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    fun onToggleMode() {
        isLongClickMode = !isLongClickMode
    }

    fun updateList(newList: MutableList<Employee>) {
        itemList = newList
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun editEmployee(employee: Employee)
        fun updateSelectedItemCount(count: Int)
    }
}