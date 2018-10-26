package baaka.io.fitnesstracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var user = FirebaseAuth.getInstance().currentUser

        val intent = if(user == null)
            Intent(this, LoginActivity::class.java)
        else
            Intent(this, HomeActivity::class.java)

        startActivity(intent)
    }
}
