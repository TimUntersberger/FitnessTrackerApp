package baaka.io.fitnesstracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_signup_link.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        login_submit.setOnClickListener {
            val password = login_password.text.toString();
            val email = login_email.text.toString();

            if (isValidInput(email, password)) {
                login(email, password)
            }

        }
    }

    private fun isValidInput(email: String, password: String): Boolean {
        var valid = true

        if(email.isEmpty()){
            login_email.error = "Please enter an email"
            login_email.requestFocus()
            valid = false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            login_email.error = "Please enter a valid email"
            login_email.requestFocus()
            valid = false
        }
        else if(password.isEmpty()){
            login_password.error = "Please enter a password"
            login_password.requestFocus()
            valid = false
        }

        if(valid){
            login_email.error = null
            login_password.error = null
        }

        return valid
    }

    private fun login(email: String, password: String){
        login_submit.isEnabled = false
        login_process.visibility = View.VISIBLE

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            .addOnFailureListener {
                if(it is FirebaseAuthInvalidUserException){
                    login_email.error = "Account with the entered email does not exist"
                    login_email.requestFocus()
                }
                else if(it is FirebaseAuthInvalidCredentialsException){
                    login_password.error = "Invalid password"
                    login_password.requestFocus()
                }
                login_process.visibility = View.GONE
                login_submit.isEnabled = true
            }
    }

    override fun onBackPressed() {
    }
}
