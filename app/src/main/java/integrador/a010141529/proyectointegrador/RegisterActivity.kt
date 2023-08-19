package integrador.a010141529.proyectointegrador

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper
import integrador.a010141529.proyectointegrador.ui.login.afterTextChanged

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

        var registerBtn = findViewById<Button>(R.id.register)
        registerBtn.setOnClickListener {
            var username = usernameEdit.text.toString()
            var password = passwordEdit.text.toString()

            if (checkRegisterData(username, password)) {
                val dbHelper = TbcDbHelper(baseContext)
                val db = dbHelper.writableDatabase

                val projection = arrayOf(
                    BaseColumns._ID,
                    TbcDbHelper.TbcContract.Users.USERNAME,
                    TbcDbHelper.TbcContract.Users.PASSWORD
                )
                val selection = "${TbcDbHelper.TbcContract.Users.USERNAME} = ?"
                val selectionArgs = arrayOf(username)
                val cursor = db.query(
                    TbcDbHelper.TbcContract.Users.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
                )
                val userFound = cursor.count
                cursor.close()

                if (userFound < 1) {
                    val values = ContentValues().apply {
                        put(TbcDbHelper.TbcContract.Users.USERNAME, username)
                        put(TbcDbHelper.TbcContract.Users.PASSWORD, password)
                        put(TbcDbHelper.TbcContract.Users.LOGGED, false)
                    }
                    val newRowId = db?.insert(TbcDbHelper.TbcContract.Users.TABLE_NAME, null, values)
                    if (newRowId?.toInt() == -1) {
                        Toast.makeText(
                            applicationContext,
                            "Ocurrió un error. Inténtelo nuevamente",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "El usuario fue agregado",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(this, OwnLogin::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "El usuario fue registrado previamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun checkRegisterData(username: String, password: String) : Boolean {
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
        return isUserNameValid(username) && isPasswordValid(password)
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