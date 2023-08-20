package integrador.a010141529.proyectointegrador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.Users

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var cancelBtn = findViewById<Button>(R.id.cancel_register)
        cancelBtn.setOnClickListener {
            val intent = Intent(this, OwnLogin::class.java)
            startActivity(intent)
        }

        val usernameEdit = findViewById<EditText>(R.id.register_username)
        val passwordEdit = findViewById<EditText>(R.id.register_password)
        val cctEdit = findViewById<EditText>(R.id.cct)

        var registerBtn = findViewById<Button>(R.id.register)
        registerBtn.setOnClickListener {
            var username = usernameEdit.text.toString()
            var password = passwordEdit.text.toString()
            var cct = cctEdit.text.toString()

            if (checkRegisterData(username, password, cct)) {
                val db = Firebase.firestore
                var exists = false
                var isError = false
                db.collection(Users.TABLE_NAME)
                    .whereEqualTo(Users.USERNAME, username)
                    .get()
                    .addOnSuccessListener { d ->
                        exists = d.count() == 1
                    }
                    .addOnFailureListener { e ->
                        isError = true
                    }

                if (!isError) {
                    if (!exists) {
                        var user = hashMapOf(
                            Users.USERNAME to username,
                            Users.PASSWORD to password,
                            Users.CCT to cct,
                            Users.IS_LOGGED to false
                        )
                        db.collection(Users.TABLE_NAME).document(username)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    applicationContext,
                                    "El usuario fue agregado",
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(this, OwnLogin::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener {e ->
                                isError = true
                            }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Ocurrió un error. Inténtelo nuevamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun checkRegisterData(username: String, password: String, cct: String) : Boolean {
        if (!isUserNameValid(username)) {
            Toast.makeText(
                applicationContext,
                "Indique un nombre de usuario",
                Toast.LENGTH_LONG
            ).show()
        }
        if (!isPasswordValid(password)) {
            Toast.makeText(
                applicationContext,
                "Indique una contraseña de al menos 5 caracteres",
                Toast.LENGTH_LONG
            ).show()
        }
        if (!isCctValid(cct)) {
            Toast.makeText(
                applicationContext,
                "La CCT debe tener 10 caracteres",
                Toast.LENGTH_LONG
            ).show()
        }
        return isUserNameValid(username) && isPasswordValid(password) && isCctValid(cct)
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
//        return if (username.contains('@')) {
//            Patterns.EMAIL_ADDRESS.matcher(username).matches()
//        } else {
//            username.isNotBlank()
//        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isCctValid(cct: String): Boolean {
        return cct.length == 10
    }
}