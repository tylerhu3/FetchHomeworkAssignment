package com.tyler.hu.fetchhomework

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FetchItemAdapter
    private lateinit var progressBar: View
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var errorView: LinearLayout
    private lateinit var errorTextView: TextView
    private lateinit var onlineDataViewModel: OnlineDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        onlineDataViewModel = ViewModelProvider(this)[OnlineDataViewModel::class.java]

        initViews()
        setupWindowInsets()
        observeViewModelStates()
        fetchData()
        setupSwipeRefresh()
    }

    private fun initViews() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = FetchItemAdapter()
        recyclerView.adapter = adapter
        errorView = findViewById(R.id.errorLayout)
        errorTextView = findViewById(R.id.errorTextView)
        progressBar = findViewById(R.id.progressBar)
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
                recyclerView.visibility = if (error != null) View.GONE else View.VISIBLE
                errorView.visibility = if (error != null) View.VISIBLE else View.GONE
                errorTextView.text = error ?: ""
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

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    onlineDataViewModel.fetchData()
                }
            }
        }
    }
}