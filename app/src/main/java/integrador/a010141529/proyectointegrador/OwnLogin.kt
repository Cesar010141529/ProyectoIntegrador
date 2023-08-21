package integrador.a010141529.proyectointegrador

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.Users

class OwnLogin : AppCompatActivity() {
//    private val _loginForm = MutableLiveData<LoginFormState>()
//    val loginFormState: LiveData<LoginFormState> = _loginForm

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
            val db = Firebase.firestore
            var exists = false
            var isError = false
            db.collection(Users.TABLE_NAME)
                .whereEqualTo(Users.USERNAME, username.text.toString())
                .whereEqualTo(Users.PASSWORD, password.text.toString())
                .get()
                .addOnSuccessListener { result ->
                    exists = result.count() == 1
                    if (exists) {
                        val dbHelper = TbcDbHelper(baseContext)
                        val dbLocal = dbHelper.writableDatabase
                        val projection = arrayOf(BaseColumns._ID, Users.USERNAME)
                        val selection = "${Users.USERNAME} = ?"
                        val selectionArgs = arrayOf(username.text.toString())
                        val cursor = dbLocal.query(
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
                                put(Users.IS_LOGGED, true)
                            }
                            val selection = "${Users.USERNAME} = ?"
                            val selectionArgs = arrayOf(username.text.toString())
                            val rowsModified = dbLocal.update(
                                Users.TABLE_NAME, values, selection, selectionArgs
                            )
                        } else {
                            val values = ContentValues().apply {
                                put(Users.USERNAME, username.text.toString())
                                put(Users.IS_LOGGED, true)
                            }
                            val newRowId = dbLocal?.insert(Users.TABLE_NAME, null, values)
                        }
                        val userRef = db.collection(Users.TABLE_NAME).document(username.text.toString())
                        userRef.update(Users.IS_LOGGED, true)
                            .addOnSuccessListener {
                                val welcome = "¡Bienvenido!"
                                val displayName = username.text.toString()
                                Toast.makeText(
                                    applicationContext,
                                    "$welcome $displayName",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Log.w("FIREBASE", "Error", e)
                            }
                    }  else {
                        Toast.makeText(
                            applicationContext,
                            "El usuario y/o la contraseña son inválidos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .addOnFailureListener {exception ->
                    Log.w("FIREBASE", "Error.", exception)
                    isError = true
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

    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}