package com.moamri.mosnad_kotlin.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.moamri.mosnad_kotlin.R
import com.moamri.mosnad_kotlin.models.UserData

class MainActivity : AppCompatActivity() {
    //    TODO improve UI design
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (UserData.currentUser==null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.changePasswordButton).setOnClickListener {
            val intent = Intent(this@MainActivity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.logoutPasswordButton).setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }
}