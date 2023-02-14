package com.example.handyjobs.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.handyjobs.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountOptions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_options)
    }
}