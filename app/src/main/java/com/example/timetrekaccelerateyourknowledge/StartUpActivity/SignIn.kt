package com.example.timetrekaccelerateyourknowledge.StartUpActivity

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.timetrekaccelerateyourknowledge.Data_Class.User_Details
import com.example.timetrekaccelerateyourknowledge.MainActivity
import com.example.timetrekaccelerateyourknowledge.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class SignIn : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passField: EditText
    private lateinit var createButton: Button
    private lateinit var googleLoginButton: Button
    private lateinit var alreadyHaveAccountText: TextView
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firestore : FirebaseFirestore
    val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        emailField = findViewById(R.id.emailfield)
        passField = findViewById(R.id.passfield)
        createButton = findViewById(R.id.createbutton)
        googleLoginButton = findViewById(R.id.google_creation)
        alreadyHaveAccountText = findViewById(R.id.alreadyfield)
        progressDialog=ProgressDialog(this)
        firebaseauth=FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        createButton.setOnClickListener {
          AccountCreation()
        }
        googleLoginButton.setOnClickListener {
            GoogleAccess()
        }

    }

    private fun GoogleAccess() {
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googlesignin=GoogleSignIn.getClient(this,gso)
        val signin=googlesignin.signInIntent
        startActivityForResult(signin,RC_SIGN_IN)
    }

    private fun handlesignin(account: GoogleSignInAccount) {
        Log.d(TAG,"FirebaseAuthWithGoogle ${account!!.id}" )
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseauth.signInWithCredential(credential)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user=firebaseauth.currentUser
                    addusertofirestore(user!!)
                }
                else{
                    Log.w(TAG, "SignInWithCredential:Failure", it.exception)

                }
            }
    }

    private fun addusertofirestore(user:FirebaseUser ) {
        val Usermap = hashMapOf(
            "name" to user.displayName,
            "email" to user.email,
            "photoUrl" to user.photoUrl
        )
        firestore.collection("GoogleSignINUser")
            .document(user.uid)
            .set(Usermap)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Log in successful", Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }
            .addOnFailureListener {e->
                Toast.makeText(this,"Check Internet Connectivity ${e.message}",Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }
        progressDialog.setMessage("Logging...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RC_SIGN_IN){
            val accounttask=GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val accounttask=accounttask.getResult(ApiException::class.java)
                handlesignin(accounttask)
            }
            catch (e:Exception){
                Toast.makeText(this,"Google Sing in Failed ${e.message}",Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun AccountCreation() {
        val email=emailField.text.toString()
        val pass=passField.text.toString()
        if (email.isEmpty()){
            emailField.error="Enter Email to Procced"
        }
        if(pass.isEmpty()){
            passField.error="Enter Password to Procced"
        }
        if(email.isNotEmpty() && pass.isNotEmpty()){
            firebaseauth.createUserWithEmailAndPassword(email,pass)
                .addOnSuccessListener {
                    firestore.collection("User Data")
                        .document(FirebaseAuth.getInstance().uid.toString())
                        .set(User_Details(email,pass))
                    Intent(this,MainActivity::class.java).also { startActivity(it) }
                    Toast.makeText(this, "Logging Successfully!", Toast.LENGTH_SHORT).show()
                    progressDialog.cancel()
                }
                .addOnFailureListener { e->
                    Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
                    progressDialog.cancel()
                }
            progressDialog.setMessage("Logging ...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        }
    }
}