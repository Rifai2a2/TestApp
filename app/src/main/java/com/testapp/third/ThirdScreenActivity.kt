package com.testapp.third

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testapp.adapter.UserAdapter
import com.testapp.databinding.ActivityThirdScreenBinding
import com.testapp.response.DataItem


class ThirdScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdScreenBinding
    private val viewModel by viewModels<ThirdScreenViewModel>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = "Third Screen"


        val layoutManager = LinearLayoutManager(this)
        binding.rvList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvList.addItemDecoration(itemDecoration)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.resetPage()
            viewModel.getUsers()
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (!binding.swipeRefreshLayout.isRefreshing && lastVisibleItemPosition == totalItemCount - 1) {
                    viewModel.getUsers()
                }
            }
        })

        viewModel.userResponse.observe(this) { userResponse ->
            if (userResponse != null) {
                userResponse.data?.let { setReviewData(it) }
                showLoading(false)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                showLoading(false)
            }
        }

        adapter = UserAdapter { selectedUser ->
            val intent = Intent()
            intent.putExtra("selectedUserName", "${selectedUser.firstName} ${selectedUser.lastName}")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.rvList.adapter = adapter
        viewModel.getUsers()
        showLoading(true)
    }

    private fun setReviewData(users: List<DataItem?>) {
        if (users.isNotEmpty()) {
            adapter.submitList(users)
            binding.rvList.visibility = View.VISIBLE
            binding.emptyStateTextView.visibility = View.GONE
        } else {
            binding.rvList.visibility = View.GONE
            binding.emptyStateTextView.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
