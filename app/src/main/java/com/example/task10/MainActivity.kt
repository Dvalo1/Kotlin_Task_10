package com.example.task10

import android.R.id.message
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html.fromHtml
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailText = "Email <font color=#f1355b>*</font>"
        val passText = "Password <font color=#f1355b>*</font>"
        signupEmail.hint = fromHtml(emailText)
        signupPassword.hint = fromHtml(passText)

        register_btn.setOnClickListener {

            val email = signupEmail.text.toString()
            val password = signupPassword.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()) {

                auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast(this, "Registration successful!", Toast.LENGTH_LONG)
                        finish()
                    } else {
                        try {
                            throw task.exception!!
                        } catch (weak_pass: FirebaseAuthWeakPasswordException) {
                            showToast(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT)
                        } catch (malformed_email: FirebaseAuthInvalidCredentialsException ) {
                            showToast(this, "Wrong E-Mail address format entered!", Toast.LENGTH_SHORT)
                        } catch (email_exists: FirebaseAuthUserCollisionException) {
                            showToast(this, "E-Mail address already in use!", Toast.LENGTH_SHORT)
                        }
                    }
                }
            } else {
                showToast(this, "Input fields are empty!", Toast.LENGTH_SHORT)
            }

        }
    }
    private fun showToast(context: Context, msg: String, duration: Int) {
        val toast = Toast.makeText(context, msg, duration)
        val view: View = toast.view
        view.setBackgroundResource(android.R.drawable.toast_frame)
        val toastMessage = view.findViewById(message) as TextView
        toastMessage.setTextColor(Color.parseColor("#e0dfdf"))
        toast.show()
    }
}