package com.moamri.mosnad_kotlin.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import com.moamri.mosnad_kotlin.R
import com.moamri.mosnad_kotlin.models.UserData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ChangePasswordActivity : AppCompatActivity() {
    //    TODO improve UI design
    lateinit var oldPasswordEditText: EditText
    lateinit var newPasswordEditText: EditText
    lateinit var changeButton: Button
    lateinit var progressBar: ProgressBar
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText)
        newPasswordEditText = findViewById(R.id.newPasswordEditText)
        changeButton = findViewById(R.id.changeButton)
        progressBar = findViewById(R.id.progressBar)
        changeButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString()
            if(oldPassword.length < 8){
                Toast.makeText(this, "Old Password must greater than or equal to 8 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }
            val newPassword = newPasswordEditText.text.toString()
            if(newPassword.length < 8){
                Toast.makeText(this, "New Password must greater than or equal to 8 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }
            changeButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            changePassword();
        }
    }
    fun changePassword() {
        val json = Gson().toJson(
            UserData(
                "",
                UserData.currentUser!!.email,
                "",
                "",
                oldPasswordEditText.text.toString(),
                "",
                newPasswordEditText.text.toString(),
            )
        )

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

        val request = Request.Builder()
            .post(body)
            // this ip is my pc ip so the emulator can connect to
            .url("http://192.168.158.203/mosnad/change-password.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    changeButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                Log.e("ChangePasswordActivity", e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonData: String = response.body()?.string()?:""
                val data = JSONObject(jsonData)
                Log.e("LoginActivity", "${data.getString("response")}")
                if (data.getString("response") == "false"){
                    runOnUiThread{
                        changeButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@ChangePasswordActivity, "Error ${data.getString("message")}", Toast.LENGTH_LONG).show()
                    }

                }else{
                    runOnUiThread{
                        changeButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        Toast.makeText(this@ChangePasswordActivity, "Password changed successfully}", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@ChangePasswordActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }

                }

            }
        })
    }
}