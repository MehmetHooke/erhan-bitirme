package com.example.deneme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Register fragment layoutunu bağla
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Auth başlat
        firebaseAuth = FirebaseAuth.getInstance()

        // UI öğelerini manuel olarak bağla
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val surnameEditText = view.findViewById<EditText>(R.id.surnameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        // Register butonuna tıklama dinleyicisi
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                performRegistration(name, surname, email, password)
            } else {
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performRegistration(email: String, password: String, name: String, surname: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun saveUserData(name: String, surname: String, email: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("Users")

        val user = mapOf(
            "name" to name,
            "surname" to surname,
            "email" to email
        )

        database.child(userId).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "User registered successfully!", Toast.LENGTH_SHORT).show()
                // İsterseniz başka bir ekrana yönlendirebilirsiniz
            } else {
                Toast.makeText(
                    context,
                    "Failed to save user data: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
