package com.testapp.second

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.testapp.databinding.ActivitySecondScreenBinding
import com.testapp.third.ThirdScreenActivity

class SecondScreenActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_THIRD_SCREEN = 123
    }

    private lateinit var binding: ActivitySecondScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = "Second Screen"

        val showName = intent.getStringExtra("showName")
        binding.textViewShowName.text = showName

        val selectedUserName = intent.getStringExtra("selectedUserName")
        if (selectedUserName != null) {
            binding.textViewSelectedUser.text = "Selected User Name: $selectedUserName"
        }

        binding.btnChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdScreenActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_THIRD_SCREEN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_THIRD_SCREEN && resultCode == Activity.RESULT_OK) {
            val selectedUserName = data?.getStringExtra("selectedUserName")
            binding.textViewSelectedUser.text = selectedUserName
        }
    }
}
