package integrador.a010141529.proyectointegrador

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import integrador.a010141529.proyectointegrador.data.model.Equipamiento
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.DatosEquipamiento
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.Users
import java.io.File

class EquipamientoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipamiento)

        val dbHelper = TbcDbHelper(baseContext)
        val dbLocal = dbHelper.writableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            Users.USERNAME,
            Users.IS_LOGGED)
        val selection = "${Users.IS_LOGGED} = ?"
        val selectionArgs = arrayOf("1")
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
        var usernameLocal = ""
        if (userFound > 0) {
            with (cursor) {
                moveToFirst()
                usernameLocal = getString(getColumnIndexOrThrow(Users.USERNAME))
            }
        }
        cursor.close()

        if (userFound != 1)
        {
            val intent = Intent(this, OwnLogin::class.java)
            startActivity(intent)
        }

        val db = Firebase.firestore
        var userRef = db.collection(Users.TABLE_NAME).document(usernameLocal)
        userRef.get()
            .addOnSuccessListener { userfb ->
                if (userfb != null) {
                    var equipamientoRef = db.collection(DatosEquipamiento.TABLE_NAME)
                        .document(userfb.get(Users.CCT).toString())
                    equipamientoRef.get()
                        .addOnSuccessListener { dEquipamiento ->
                            if (dEquipamiento.exists()) {
                                val pregunta11 = findViewById<EditText>(R.id.pregunta1_1_text)
                                val pregunta12 = findViewById<EditText>(R.id.pregunta1_2_text)
                                val pregunta13 = findViewById<EditText>(R.id.pregunta1_3_text)
                                val pregunta21 = findViewById<EditText>(R.id.pregunta2_1_text)
                                val pregunta22 = findViewById<EditText>(R.id.pregunta2_2_text)
                                val pregunta23 = findViewById<EditText>(R.id.pregunta2_3_text)
                                var pivote = dEquipamiento.get(DatosEquipamiento.PREGUNTA11)
                                if (pivote != null && pivote.toString().isNotBlank()) {
                                    pregunta11.setText(pivote.toString())
                                }
                                pivote = dEquipamiento.get(DatosEquipamiento.PREGUNTA12)
                                if (pivote != null && pivote.toString().isNotBlank()) {
                                    pregunta12.setText(pivote.toString())
                                }
                                pivote = dEquipamiento.get(DatosEquipamiento.PREGUNTA13)
                                if (pivote != null && pivote.toString().isNotBlank()) {
                                    pregunta13.setText(pivote.toString())
                                }
                                pivote = dEquipamiento.get(DatosEquipamiento.PREGUNTA21)
                                if (pivote != null && pivote.toString().isNotBlank()) {
                                    pregunta21.setText(pivote.toString())
                                }
                                pivote = dEquipamiento.get(DatosEquipamiento.PREGUNTA22)
                                if (pivote != null && pivote.toString().isNotBlank()) {
                                    pregunta22.setText(pivote.toString())
                                }
                                pivote = dEquipamiento.get(DatosEquipamiento.PREGUNTA23)
                                if (pivote != null && pivote.toString().isNotBlank()) {
                                    pregunta23.setText(pivote.toString())
                                }
                            }
                        }
                }
            }

        var backBtnClick = findViewById<Button>(R.id.back_infraestructura)
        backBtnClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var saveBtnClick = findViewById<Button>(R.id.save_equipamiento)
        saveBtnClick.setOnClickListener{
            var data = Equipamiento()
            data.pregunta11 = findViewById<EditText>(R.id.pregunta1_1_text).text.toString()
            data.pregunta12 = findViewById<EditText>(R.id.pregunta1_2_text).text.toString()
            data.pregunta13 = findViewById<EditText>(R.id.pregunta1_3_text).text.toString()
            data.pregunta21 = findViewById<EditText>(R.id.pregunta2_1_text).text.toString()
            data.pregunta22 = findViewById<EditText>(R.id.pregunta2_2_text).text.toString()
            data.pregunta23 = findViewById<EditText>(R.id.pregunta2_3_text).text.toString()
            val db = Firebase.firestore
            var userRef = db.collection(Users.TABLE_NAME).document(usernameLocal)
            userRef.get()
                .addOnSuccessListener { userfb ->
                    if (userfb != null) {
                        var equipamientoRef = db.collection(DatosEquipamiento.TABLE_NAME)
                            .document(userfb.get(Users.CCT).toString())
                        equipamientoRef.get()
                            .addOnSuccessListener { dEquipamiento ->
                                var datos = hashMapOf<String, Any>(
                                    DatosEquipamiento.PREGUNTA11 to data.pregunta11,
                                    DatosEquipamiento.PREGUNTA12 to data.pregunta12,
                                    DatosEquipamiento.PREGUNTA13 to data.pregunta13,
                                    DatosEquipamiento.PREGUNTA21 to data.pregunta21,
                                    DatosEquipamiento.PREGUNTA22 to data.pregunta22,
                                    DatosEquipamiento.PREGUNTA23 to data.pregunta23,
                                )
                                if (dEquipamiento.exists()) {
                                    equipamientoRef.update(datos)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "La sección Equipamiento fue actualizada",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            val intent = Intent(this, AcademicosActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "Ocurrió un error. Inténtelo nuevamente",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                } else {
                                    equipamientoRef.set(datos)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "La sección Equipamiento fue actualizada",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            val intent = Intent(this, AcademicosActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "Ocurrió un error. Inténtelo nuevamente",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            }
                    }
                }
        }

        var logoutClick = findViewById<ImageView>(R.id.logoutImg)
        logoutClick.setOnClickListener {
            val dbHelper = TbcDbHelper(baseContext)
            val dbLocal = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(Users.IS_LOGGED, 0)
            }
            val rowsModified = dbLocal.update(
                Users.TABLE_NAME,
                values,
                null,
                null
            )

            val intent = Intent(this, OwnLogin::class.java)
            startActivity(intent)
        }

        var infraestructuraLabel = findViewById<TextView>(R.id.infraestructura_label)
        infraestructuraLabel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var academicosLabel = findViewById<TextView>(R.id.academicos_label)
        academicosLabel.setOnClickListener {
            val intent = Intent(this, AcademicosActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createFile(filename: String, content: String) {
        val externalDir = getExternalFilesDir(null)
        val file = File(externalDir, "/$filename")
        file.writeText(content)
    }

    private fun readFile(filename: String) : String {
        return try {
            val externalDir = getExternalFilesDir(null)
            val file = File(externalDir, "/$filename")
            val text = file.readText()
            Log.d("FILE_CONTENT", text)
            text
        } catch (e: Exception) {
            ""
        }
    }
}