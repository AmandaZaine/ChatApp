package com.amandazaine.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amandazaine.chatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate( layoutInflater )
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView( binding.root )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onClick()
    }

    override fun onStart() {
        super.onStart()
        verifyLoggedUser()
    }

    private fun verifyLoggedUser() {
        val currentUser = firebaseAuth.currentUser
        if( currentUser != null ) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun onClick() {
        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        binding.buttonLogIn.setOnClickListener {
            if( validateForm() ) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    .addOnFailureListener { exception ->
                        try {
                            exception
                        } catch (invalidUser: FirebaseAuthInvalidUserException) {
                            invalidUser.printStackTrace()
                            Toast.makeText(this, "Invalid user", Toast.LENGTH_LONG).show()
                        } catch (error: Exception) {
                            error.printStackTrace()
                            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    private fun validateForm(): Boolean {
        email = binding.inputEditLoginEmail.text.toString()
        password = binding.inputEditLoginPassword.text.toString()

        if( email.isNotEmpty() && password.isNotEmpty() ) {
            binding.inputLoginEmail.error = null
            binding.inputLoginPassword.error = null
            return true
        } else {
            binding.inputLoginEmail.error = "Email is required"
            binding.inputLoginPassword.error = "Password is required"
            return false
        }
    }

}