package com.testapp.third

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.testapp.api.ApiConfig
import com.testapp.api.ApiService
import com.testapp.response.ListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdScreenViewModel : ViewModel() {

    private val _userResponse = MutableLiveData<ListResponse>()
    val userResponse: LiveData<ListResponse> get() = _userResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var currentPage = 1

    private val apiService = ApiConfig.retrofit.create(ApiService::class.java)

    fun getUsers() {
        val call = apiService.getUsers(page = 1, perPage = 10)

        call.enqueue(object : Callback<ListResponse> {
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                if (response.isSuccessful) {
                    _userResponse.value = response.body()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                _error.value = "Failure: ${t.message}"
            }
        })
    }

    fun resetPage() {
        currentPage = 1
    }
}
