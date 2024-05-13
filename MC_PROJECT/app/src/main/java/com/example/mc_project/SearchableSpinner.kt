package com.example.mc_project

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.Spinner
import com.example.mc_project.databinding.SearchableSpinnerLayoutBinding

class SearchableSpinner : LinearLayout {
    private lateinit var binding: SearchableSpinnerLayoutBinding
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var filter: Filter

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = SearchableSpinnerLayoutBinding.inflate(inflater, this, true)

        adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.searchSpinner.adapter = adapter

        // Initialize the filter
        filter = adapter.filter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filter.filter(s)
            }
        })

        binding.searchSpinner.onItemSelectedListener = onItemSelectedListener
    }


    fun setEntries(entries: Array<out String>) {
        adapter.clear()
        adapter.addAll(entries.toList())
        adapter.notifyDataSetChanged()
    }

    fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener) {
        binding.searchSpinner.onItemSelectedListener = listener
    }
    fun getSelectedItem(): String? {
        return if (binding.searchSpinner.selectedItemPosition != AdapterView.INVALID_POSITION) {
            adapter.getItem(binding.searchSpinner.selectedItemPosition)
        } else {
            null
        }
    }

    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
            // Handle item selection if needed
            val selectedItem = parent?.getItemAtPosition(position).toString()
            // You can perform actions here when an item is selected
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Do nothing
        }
    }
}

