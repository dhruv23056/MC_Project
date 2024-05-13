package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Room database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()

        // Set up onClickListeners for sign-up and sign-in buttons
        findViewById<Button>(R.id.buttonSignIn).setOnClickListener {
            toggleVisibility(isSignUp = false)
        }

        findViewById<Button>(R.id.buttonSignUp).setOnClickListener {
            toggleVisibility(isSignUp = true)
        }

        // Set up onClickListener for submit button
        findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            if (findViewById<EditText>(R.id.editTextEmail).visibility == EditText.VISIBLE) {
                // Handle sign-up
                val username = findViewById<EditText>(R.id.editTextUsername).text.toString()
                val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
                val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
                val mobileNo = findViewById<EditText>(R.id.editTextMobileNo).text.toString()

                if (validateSignUpInput(username, email, password, mobileNo)) {
                    signUp(username, email, password, mobileNo)
                }
            } else {
                // Handle sign-in
                val username = findViewById<EditText>(R.id.editTextUsername).text.toString()
                val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
                signIn(username, password)
            }
        }
    }

    private fun toggleVisibility(isSignUp: Boolean) {
        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val mobileNoEditText = findViewById<EditText>(R.id.editTextMobileNo)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val submitButton = findViewById<Button>(R.id.buttonSubmit)

        if (isSignUp) {
            // Show sign-up fields
            usernameEditText.visibility = EditText.VISIBLE
            emailEditText.visibility = EditText.VISIBLE
            mobileNoEditText.visibility = EditText.VISIBLE
            passwordEditText.visibility = EditText.VISIBLE
            submitButton.visibility = Button.VISIBLE
        } else {
            // Show sign-in fields
            usernameEditText.visibility = EditText.VISIBLE
            emailEditText.visibility = EditText.GONE
            mobileNoEditText.visibility = EditText.GONE
            passwordEditText.visibility = EditText.VISIBLE
            submitButton.visibility = Button.VISIBLE
        }
    }

    // Function to validate sign-up input
    private fun validateSignUpInput(
        username: String,
        email: String,
        password: String,
        mobileNo: String
    ): Boolean {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || mobileNo.isEmpty()) {
            showToast("All fields are required")
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid email")
            return false
        }
        if (password.length < 8) {
            showToast("Password should be at least 8 characters long")
            return false
        }
        if (mobileNo.length != 10) {
            showToast("Mobile number should be 10 digits long")
            return false
        }
        return true
    }

    // Function to handle user sign-up
    private fun signUp(username: String, email: String, password: String, mobileNo: String) {
        GlobalScope.launch(Dispatchers.IO) {
            // Check if the email already exists
            val existingUserWithEmail = db.userDao().getUserByEmail(email)
            if (existingUserWithEmail != null) {
                showToast("Email is already in use")
            } else {
                // Proceed with sign-up
                val user = User(username, email, password, mobileNo)
                db.userDao().insert(user)
                showToast("Sign up successful")
            }
        }
    }

    // Function to handle user sign-in
    private fun signIn(username: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val user = db.userDao().getUserByUsername(username)
            if (user == null) {
                showToast("User not registered. Please sign up first.")
            } else {
                if (user.password == password) {
                    // Password matched, proceed with sign-in
                    showToast("Sign in successful")
                    // Navigate to the next activity
                    navigateToNextActivity()
                } else {
                    showToast("Incorrect password")
                }
            }
        }
    }

    // Function to navigate to the next activity
    private fun navigateToNextActivity() {
        val intent = Intent(this, Activity2::class.java)
        startActivity(intent)
    }

    // Function to show toast message
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}
