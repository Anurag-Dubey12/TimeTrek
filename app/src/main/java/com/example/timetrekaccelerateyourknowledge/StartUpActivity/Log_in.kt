package com.example.timetrekaccelerateyourknowledge.StartUpActivity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.timetrekaccelerateyourknowledge.MainActivity
import com.example.timetrekaccelerateyourknowledge.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Log_in : AppCompatActivity() {
    private lateinit var alreadyEmail: EditText
    private lateinit var alreadyPassfield: EditText
    private lateinit var loginButton: Button
    private lateinit var googleLogin: Button
    private lateinit var forgetpas: TextView
    private lateinit var createacc:TextView
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseUser: FirebaseUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        alreadyEmail = findViewById(R.id.alreadyemail)
        alreadyPassfield = findViewById(R.id.alreadypassfield)
        createacc = findViewById(R.id.createaccount)
        loginButton = findViewById(R.id.login_button)
        googleLogin = findViewById(R.id.google_login)
        forgetpas = findViewById(R.id.forgetpass)
        firebaseauth=FirebaseAuth.getInstance()
        progressDialog=ProgressDialog(this)

        loginButton.setOnClickListener {
            val email=alreadyEmail.text.toString()
            val pass=alreadyPassfield.text.toString()
            if(email.isEmpty()){
                alreadyEmail.error="Enter emal first"
            }
            if(pass.isEmpty()){
                alreadyPassfield.error="Enter Password"
            }
            if(email.isNotEmpty() && pass.isNotEmpty()){
                firebaseauth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener{
                    if (it.isSuccessful){
                        Intent(this,MainActivity::class.java).also { startActivity(it) }
                        Toast.makeText(this,"Welcome User!",Toast.LENGTH_SHORT).show()
                    }
                else{
                    Toast.makeText(this,"Something went wrong Or maybe you have not created account previously",Toast.LENGTH_SHORT).show()
                    }
                    }
                forgetpas.setOnClickListener {
                    resetpass()
                }

            }
        }
        createacc.setOnClickListener {
            Intent(this,SignIn::class.java).also { startActivity(it) }
        }

        checksignin()
    }

    private fun checksignin() {
        val loggoogleaccount= GoogleSignIn.getLastSignedInAccount(this)
        if(loggoogleaccount!=null){
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
            }
        }
        val currentuser=FirebaseAuth.getInstance().currentUser
        if(currentuser!=null){
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun resetpass() {
        val email=alreadyEmail.text.toString()
        if(email.isEmpty()){
            alreadyEmail.error="Please enter your mail to reset"
        }
        else{
            firebaseauth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(this,"Email has been sent ",Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }
                .addOnFailureListener {e->
                    Toast.makeText(this,"Enter A Valid Email ${e.message}",Toast.LENGTH_SHORT).show()
                    progressDialog.cancel()
                }
            progressDialog.setMessage("Sending Mail")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        }

    }


}