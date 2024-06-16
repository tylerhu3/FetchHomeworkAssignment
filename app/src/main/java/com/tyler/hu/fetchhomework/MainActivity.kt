package com.tyler.hu.fetchhomework

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FetchItemAdapter
    private lateinit var progressBar: View
    private lateinit var errorView: LinearLayout
    private lateinit var errorTextView: TextView
    private lateinit var onlineDataViewModel: OnlineDataViewModel
    private lateinit var tryAgainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        onlineDataViewModel = ViewModelProvider(this)[OnlineDataViewModel::class.java]

        initViews()
        setupWindowInsets()
        observeViewModelStates()
        fetchData()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = FetchItemAdapter()
        recyclerView.adapter = adapter
        errorView = findViewById(R.id.errorLayout)
        errorTextView = findViewById(R.id.errorTextView)
        progressBar = findViewById(R.id.progressBar)
        tryAgainButton = findViewById(R.id.retryButton)
        tryAgainButton.setOnClickListener {
            fetchData()
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeViewModelStates() {
        lifecycleScope.launch {
            onlineDataViewModel.isLoading.collectLatest { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            onlineDataViewModel.fetchedItems.collectLatest { items ->
                adapter.submitList(items.flatMap { it.value })
            }
        }

        lifecycleScope.launch {
            onlineDataViewModel.error.collectLatest { error ->
                recyclerView.visibility = if (error) View.GONE else View.VISIBLE
                errorView.visibility = if (error) View.VISIBLE else View.GONE
            }
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                onlineDataViewModel.fetchData()
            }
        }
    }
}