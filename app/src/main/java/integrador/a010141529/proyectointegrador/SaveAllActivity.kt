package integrador.a010141529.proyectointegrador

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.DatosAcademicos
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.DatosEquipamiento
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.DatosInfraestructura
import integrador.a010141529.proyectointegrador.data.model.TbcDbHelper.TbcContract.Users


class SaveAllActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_all)

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

        var infraestructuraEdit = findViewById<Button>(R.id.infraestructura_edit)
        var infraestructuraDelete = findViewById<Button>(R.id.infraestructura_delete)
        var equipamientoEdit = findViewById<Button>(R.id.equipamiento_edit)
        var equipamientoDelete = findViewById<Button>(R.id.equipamiento_delete)
        var academicosEdit = findViewById<Button>(R.id.academicos_edit)
        var academicosDelete = findViewById<Button>(R.id.academicos_delete)

        var infraestructuraLabel = findViewById<TextView>(R.id.infraestructura_label)
        infraestructuraLabel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var equipamientoLabel = findViewById<TextView>(R.id.equipamiento_label)
        equipamientoLabel.setOnClickListener {
            val intent = Intent(this, EquipamientoActivity::class.java)
            startActivity(intent)
        }

        var academicosLabel = findViewById<TextView>(R.id.academicos_label)
        academicosLabel.setOnClickListener {
            val intent = Intent(this, AcademicosActivity::class.java)
            startActivity(intent)
        }

        infraestructuraEdit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        equipamientoEdit.setOnClickListener {
            val intent = Intent(this, EquipamientoActivity::class.java)
            startActivity(intent)
        }

        academicosEdit.setOnClickListener {
            val intent = Intent(this, AcademicosActivity::class.java)
            startActivity(intent)
        }

        infraestructuraDelete.setOnClickListener {
            val db = Firebase.firestore
            var userRef = db.collection(Users.TABLE_NAME).document(usernameLocal)
            userRef.get()
                .addOnSuccessListener { userfb ->
                    if (userfb.exists()) {
                        var infraestructuraRef = db.collection(DatosInfraestructura.TABLE_NAME)
                            .document(userfb.get(Users.CCT).toString())
                        infraestructuraRef.get()
                            .addOnSuccessListener { dInfraestructura ->
                                if (dInfraestructura.exists()) {
                                    db.collection(DatosInfraestructura.TABLE_NAME).document(dInfraestructura.id).delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "La información de Infraestructura fue eliminada",
                                                Toast.LENGTH_LONG
                                            ).show()
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

        equipamientoDelete.setOnClickListener {
            val db = Firebase.firestore
            var userRef = db.collection(Users.TABLE_NAME).document(usernameLocal)
            userRef.get()
                .addOnSuccessListener { userfb ->
                    if (userfb.exists()) {
                        var equipamientoRef = db.collection(DatosEquipamiento.TABLE_NAME)
                            .document(userfb.get(Users.CCT).toString())
                        equipamientoRef.get()
                            .addOnSuccessListener { dEquipamiento ->
                                if (dEquipamiento.exists()) {
                                    db.collection(DatosEquipamiento.TABLE_NAME).document(dEquipamiento.id).delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "La información de Equipamiento fue eliminada",
                                                Toast.LENGTH_LONG
                                            ).show()
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

        academicosDelete.setOnClickListener {
            val db = Firebase.firestore
            var userRef = db.collection(Users.TABLE_NAME).document(usernameLocal)
            userRef.get()
                .addOnSuccessListener { userfb ->
                    if (userfb.exists()) {
                        var academicoRef = db.collection(DatosAcademicos.TABLE_NAME)
                            .document(userfb.get(Users.CCT).toString())
                        academicoRef.get()
                            .addOnSuccessListener { dAcademico ->
                                if (dAcademico.exists()) {
                                    db.collection(DatosAcademicos.TABLE_NAME).document(dAcademico.id).delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "La información de Datos académicos fue eliminada",
                                                Toast.LENGTH_LONG
                                            ).show()
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
    }
}