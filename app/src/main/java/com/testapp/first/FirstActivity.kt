package com.testapp.first

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.testapp.databinding.ActivityFirstBinding
import com.testapp.second.SecondScreenActivity

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnCheck.setOnClickListener {
            val sentence = binding.palindromEditText.text.toString()

            val isPalindrome = checkPalindrome(sentence)

            val message = if (isPalindrome) {
                "$sentence, isPalindrome"
            } else {
                "$sentence, not palindrome"
            }

            showDialog(message)
        }

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, SecondScreenActivity::class.java)
            intent.putExtra("showName", binding.nameEditText.text.toString())
            startActivity(intent)
        }
    }

    private fun checkPalindrome(sentence: String): Boolean {
        val cleanSentence = sentence.replace("\\s".toRegex(), "")
        val reversedSentence = cleanSentence.reversed()
        return cleanSentence.equals(reversedSentence, ignoreCase = true)
    }

    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }
}
