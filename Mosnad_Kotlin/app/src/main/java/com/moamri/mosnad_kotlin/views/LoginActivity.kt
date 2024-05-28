package com.moamri.mosnad_kotlin.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.moamri.mosnad_kotlin.R
import com.moamri.mosnad_kotlin.models.UserData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    //    TODO improve UI design
    lateinit var emailEditText: EditText
    lateinit var passwordEditText:EditText
    lateinit var signInButton: Button
    lateinit var progressBar: ProgressBar
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signInButton = findViewById(R.id.signInButton)
        progressBar = findViewById(R.id.progressBar)
        findViewById<TextView>(R.id.createNewAccountTextView).setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if(!email.contains("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex())){
                Toast.makeText(this, "Email not valid", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }
            val password = passwordEditText.text.toString()
            if(password.length < 8){
                Toast.makeText(this, "Password must greater than or equal to 8 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }
            signInButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            signIn();

        }
    }

    fun signIn() {
        val json = Gson().toJson(
            UserData(
            "",
            emailEditText.text.toString(),
            "",
            "",
            passwordEditText.text.toString(),
            null,
                null
        )
        )

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

        val request = Request.Builder()
            .post(body)
                // this ip is my pc ip so the emulator can connect to
            .url("http://192.168.158.203/mosnad/sign-in.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    signInButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                Log.e("SignInActivity", e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonData: String = response.body()?.string()?:""
                val data = JSONObject(jsonData)
                Log.e("LoginActivity", "${data.getString("response")}")
                if (data.getString("response") == "false"){
                    runOnUiThread{
                        signInButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Error ${data.getString("message")}", Toast.LENGTH_LONG).show()
                    }

                }else{
                    runOnUiThread{
                        signInButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        UserData.currentUser = UserData(
                            data.getString("user-name"),
                            data.getString("user-email"),
                            data.getString("user-phone"),
                            data.getString("user-role"),
                            null,
                            null,
                            null
                        )
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                }

            }
        })
    }
}