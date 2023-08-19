package integrador.a010141529.proyectointegrador

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.Users
import integrador.a010141529.proyectointegrador.ui.login.LoginFormState
import integrador.a010141529.proyectointegrador.ui.login.afterTextChanged

class OwnLogin : AppCompatActivity() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_login)

        // Objects
        var loginBtn = findViewById<Button>(R.id.login)
        var username = findViewById<EditText>(R.id.username)
        var password = findViewById<EditText>(R.id.password)
        var registerLink = findViewById<TextView>(R.id.action_register_user)

        username.afterTextChanged {
            loginDataChanged(username.text.toString(), password.text.toString(), loginBtn)
        }
        password.afterTextChanged {
            loginDataChanged(username.text.toString(), password.text.toString(), loginBtn)
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val dbHelper = TbcDbHelper(baseContext)
            val db = dbHelper.writableDatabase
            val projection = arrayOf(
                BaseColumns._ID,
                Users.USERNAME,
                Users.PASSWORD)
            val selection = "${Users.USERNAME} = ? and ${Users.PASSWORD} = ?"
            val selectionArgs = arrayOf(username.text.toString(), password.text.toString())
            val cursor = db.query(
                Users.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )
            val userFound = cursor.count
            cursor.close()

            if (userFound == 1) {
                val values = ContentValues().apply {
                    put(Users.LOGGED, true)
                }
                val selection = "${Users.USERNAME} = ?"
                val selectionArgs = arrayOf(username.text.toString())
                val rowsModified = db.update(
                    Users.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
                )
                val welcome = "¡Bienvenido!"
                val displayName = username.text.toString()
                Toast.makeText(
                    applicationContext,
                    "$welcome $displayName",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "El usuario y/o la contraseña son inválidos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun loginDataChanged(username: String, password: String, loginBtn: Button) {
        loginBtn.isEnabled = isUserNameValid(username) && isPasswordValid(password)
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
}