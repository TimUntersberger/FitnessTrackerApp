package baaka.io.fitnesstracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_login_link.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        register_submit.setOnClickListener {
            var email = register_email.text.toString()
            var password = register_password.text.toString()
            var confirmPassword = register_confirm_password.text.toString()

            if(isValidInput(email, password, confirmPassword))
                register(email, password)
        }
    }

    private fun register(email: String, password: String) {
        register_submit.isEnabled = false
        register_process.visibility = View.VISIBLE

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("RegisterActivity", it.user.toString())
            }
            .addOnFailureListener {
                if(it is FirebaseAuthUserCollisionException){
                    register_email.error = "The email address is already in use by another account"
                    register_email.requestFocus()
                }
                register_process.visibility = View.GONE
                register_submit.isEnabled = true
            }
    }

    private fun isValidInput(email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if(email.isEmpty()){
            register_email.error = "Please enter an email"
            register_email.requestFocus()
            isValid = false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            register_email.error = "Please enter a valid email"
            register_email.requestFocus()
            isValid = false
        }
        else if(password.isEmpty()){
            register_password.error = "Please enter a password"
            register_password.requestFocus()
            isValid = false
        }
        else if(confirmPassword.isEmpty()){
            register_confirm_password.error = "Please enter the password again"
            register_confirm_password.requestFocus()
            isValid = false
        }
        else if(password != confirmPassword){
            register_confirm_password.error = "The passwords don't match"
            register_confirm_password.requestFocus()
            isValid = false
        }

        if(isValid){
            register_email.error = null
            register_password.error = null
        }

        return isValid
    }
}
