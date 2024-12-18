package com.example.deneme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton = view.findViewById<Button>(R.id.button2)
        loginButton.setOnClickListener {
            val menuFragment = MenuFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, menuFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }
}
