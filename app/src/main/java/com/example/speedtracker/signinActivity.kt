package com.example.speedtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class signinActivity : AppCompatActivity() {
//    lateinit var textlogin:TextView
    lateinit var edtemail:EditText
    private lateinit var Edtpass:EditText
    lateinit var Edtconpaa:EditText
    private lateinit var Btnsignin:Button
    lateinit var TvDirectlogin:TextView
    lateinit var username:EditText
    lateinit var contact:EditText
    lateinit var carplate:EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

//        textlogin = findViewById(R.id.txtsignin)
        edtemail = findViewById(R.id.Semail)
        Edtpass = findViewById(R.id.edtSPassword)
        Edtconpaa = findViewById(R.id.edtSconfirmpass)
        Btnsignin = findViewById(R.id.btnSsignin)
        TvDirectlogin = findViewById(R.id.txtsignin)
        username = findViewById(R.id.usernameS)
        contact = findViewById(R.id.edtScontact)
        carplate = findViewById(R.id.edtcar_Splate)


        auth = Firebase.auth

//        textlogin.setOnClickListener {
//            intent = Intent(this,LoginActivity::class.java)
//            startActivity(intent)
//        }
        TvDirectlogin.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        Btnsignin.setOnClickListener {
            SignupUser()


        }
    }
    private fun SignupUser(){
        val email=edtemail.text.toString()
        val pass=Edtpass.text.toString()
        val confirmpass=Edtconpaa.text.toString()
        val user = username.text.toString()
        val contact = contact.text.toString()
        val carplate = carplate.text.toString()

        if (email.isBlank() || pass.isBlank() || confirmpass.isBlank() || user.isBlank() || contact.isBlank() || carplate.isBlank()){
            Toast.makeText(this,"Please Email and password cant be blank",Toast.LENGTH_LONG).show()
            return
        }  else if (pass != confirmpass){
            Toast.makeText(this,"Password do not match",Toast.LENGTH_LONG).show()
            return

        }
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){
            var ref = FirebaseDatabase.getInstance().getReference().child("Users/$pass")
            var userData = User(user, contact,carplate)
            ref.setValue(userData)

            if (it.isSuccessful){
                Toast.makeText(this,"Signed successfully you can now login",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"Failed to create",Toast.LENGTH_LONG).show()
            }
        }
    }
}