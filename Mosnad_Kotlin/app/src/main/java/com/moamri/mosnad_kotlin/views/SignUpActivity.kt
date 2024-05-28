package com.moamri.mosnad_kotlin.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.moamri.mosnad_kotlin.R
import com.moamri.mosnad_kotlin.models.UserData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class SignUpActivity : AppCompatActivity() {
    lateinit var memberNameEditText:EditText
    lateinit var emailEditText:EditText
    lateinit var phoneEditText:EditText
    lateinit var passwordEditText:EditText
    lateinit var passwordConfirmEditText:EditText
    lateinit var signUpButton:Button
    lateinit var progressBar: ProgressBar
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        memberNameEditText = findViewById(R.id.memberNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        passwordConfirmEditText = findViewById(R.id.passwordConfirmationEditText)
        signUpButton = findViewById(R.id.signUpButton)
        progressBar = findViewById(R.id.progressBar)
        findViewById<TextView>(R.id.haveAccountTextView).setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        signUpButton.setOnClickListener {
            val name = memberNameEditText.text.toString()
            if(name.length <3 || !name.contains("[a-zA-Z]".toRegex())){
                Toast.makeText(this, "Name Must Be greater than 2 letters and contains only letters", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }
            val email = emailEditText.text.toString()
            if(!email.contains("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex())){
                Toast.makeText(this, "Email not valid", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }
            val phone = phoneEditText.text.toString()
            if(!phone.contains("^(77|71|73|70).*\$".toRegex()) && phone.length == 9){
                Toast.makeText(this, "Phone Must be yemeni number with 9 digits", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }
            if (passwordEditText.text.toString() == passwordConfirmEditText.text.toString()){
                val password = passwordEditText.text.toString()
                if(!password.contains("[A-Za-z]".toRegex())
                    ||!password.contains("\\d".toRegex()) || password.length < 8){
                    Toast.makeText(this, "Password must contains at least one letter and one number and  greater than or equal to 8 characters", Toast.LENGTH_LONG).show()
                    return@setOnClickListener;
                }
                signUpButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                signUp();
            }else{
                Toast.makeText(this, "Passwords Not Match", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun signUp() {
        val json = Gson().toJson(UserData(
            memberNameEditText.text.toString(),
            emailEditText.text.toString(),
            phoneEditText.text.toString(),
            "",
            passwordEditText.text.toString(),
            passwordConfirmEditText.text.toString(),
            null
        ))

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

        val request = Request.Builder()
            .post(body)
            // this ip is my pc ip so the emulator can connect to
            .url("http://192.168.158.203/mosnad/sign-up.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    signUpButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                Log.e("SignUpActivity", e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonData: String = response.body()?.string()?:""
                val data = JSONObject(jsonData)
                Log.e("SignUpActivity", "${data.getString("response")}")
                if (data.getString("response") == "false"){
                    runOnUiThread{
                        signUpButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@SignUpActivity, "Error ${data.getString("message")}", Toast.LENGTH_LONG).show()
                    }

                }else{
                    runOnUiThread{
                        signUpButton.visibility = View.VISIBLE
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
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                }

            }
        })
    }
}