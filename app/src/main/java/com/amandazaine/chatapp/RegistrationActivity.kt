package com.amandazaine.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amandazaine.chatapp.databinding.ActivityRegistrationBinding
import com.amandazaine.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegistrationBinding.inflate( layoutInflater )
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var name: String
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

        initToolbar()
        onClick()
    }

    private fun onClick() {
        binding.buttonRegister.setOnClickListener {
            if( validateForm() ) {
                registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if( task.isSuccessful ) {
                    val id = firebaseAuth.currentUser?.uid

                    saveUserData(id, name, email, null)

                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                }
            }
            .addOnFailureListener { exception ->
                try {
                    throw exception
                } catch (errorWeakPasswork: FirebaseAuthWeakPasswordException) {
                    errorWeakPasswork.printStackTrace()
                    showUserMessage("Weak password, try again")
                } catch (errorExistingUser: FirebaseAuthUserCollisionException) {
                    errorExistingUser.printStackTrace()
                    showUserMessage("User already exists")
                } catch (errorEmail: FirebaseAuthInvalidCredentialsException) {
                    errorEmail.printStackTrace()
                    showUserMessage("Invalid email address")
                }
            }
    }

    private fun saveUserData(id: String?, name: String, email: String, image: String?) {
        if( id != null && id.isNotEmpty() ) {
            val user = User(id, name, email, image)
            firestore
                .collection("users")
                .document(id)
                .set(user)
                .addOnSuccessListener {
                    showUserMessage("Registration successful")
                }
                .addOnFailureListener {
                    showUserMessage("Error saving user data")
                }
        }
    }

    private fun showUserMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun validateForm(): Boolean {
        name = binding.inputEditName.text.toString()
        email = binding.inputEditEmail.text.toString()
        password = binding.inputEditPassword.text.toString()

        if( name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() ) {
            binding.inputLayoutName.error = null
            binding.inputLayoutEmail.error = null
            binding.inputLayoutPassword.error = null
            return true
        } else {
            binding.inputLayoutName.error = "Name is required"
            binding.inputLayoutEmail.error = "Email is required"
            binding.inputLayoutPassword.error = "Password is required"
            return false
        }
    }

    private fun initToolbar() {
        val toolbar = binding.includeToolbar.materialToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Registration"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}