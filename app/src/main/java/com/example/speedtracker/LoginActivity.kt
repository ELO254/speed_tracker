package com.example.speedtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    lateinit var edtemail:EditText
    private lateinit var edtpass:EditText
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.loginbutton)
        edtemail = findViewById(R.id.email)
        edtpass = findViewById(R.id.edtPassword)
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
//            intent = Intent(this,speedActivity::class.java)
//            startActivity(intent)
            login()
        }

    }
    private  fun login() {
        val email = edtemail.text.toString()
        val pass = edtpass.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this){
            if (it.isSuccessful){
                val intent = Intent(this,speedActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Successfull LoggedIn",Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this, "Login failed check on your details and try again", Toast.LENGTH_SHORT).show()
        }


    }
}