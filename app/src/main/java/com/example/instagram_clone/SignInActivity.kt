package com.example.instagram_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mAuthListner: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        mAuth = FirebaseAuth.getInstance()
        mAuthListner = FirebaseAuth.AuthStateListener {  }

        println(mAuth?.currentUser)
    }

//    Sign in clicked
    fun signInClicked(view: View){
    val userEmail = email.text.toString()
    val userPassword = password.text.toString()

    mAuth?.signInWithEmailAndPassword(userEmail, userPassword)?.addOnCompleteListener{ task ->
        if(task.isSuccessful){
            val intent = Intent(this, Feed::class.java)
            startActivity(intent)
        }
    }?.addOnFailureListener { exception ->
        if(exception !== null){
            Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    }

//    Sign up Clicked
    fun signUpClicked(view:View){
        val userEmail = email.text.toString()
        val userPassword = password.text.toString()
        mAuth?.createUserWithEmailAndPassword(userEmail,userPassword)?.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Toast.makeText(this, "User Created", Toast.LENGTH_LONG).show()
            }
        }?.addOnFailureListener{exception ->
            if(exception !== null){
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}
