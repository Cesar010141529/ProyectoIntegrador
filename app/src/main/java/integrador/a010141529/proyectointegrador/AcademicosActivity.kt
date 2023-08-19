package integrador.a010141529.proyectointegrador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.gson.Gson
import integrador.a010141529.proyectointegrador.data.model.Academicos
import integrador.a010141529.proyectointegrador.data.model.Equipamiento
import integrador.a010141529.proyectointegrador.ui.login.LoginActivity
import java.io.File

class AcademicosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academicos)

        val filename = "academicos.json"
        val gson = Gson()
        val contentFile = readFile(filename)
        if (contentFile != null && contentFile.isNotEmpty()) {
            var indata = gson.fromJson<Equipamiento>(contentFile, Equipamiento::class.java)
            val pregunta11 = findViewById<EditText>(R.id.pregunta1_1_text)
            val pregunta12 = findViewById<EditText>(R.id.pregunta1_2_text)
            val pregunta13 = findViewById<EditText>(R.id.pregunta1_3_text)
            val pregunta21 = findViewById<EditText>(R.id.pregunta2_1_text)
            val pregunta22 = findViewById<EditText>(R.id.pregunta2_2_text)
            val pregunta23 = findViewById<EditText>(R.id.pregunta2_3_text)
            pregunta11.setText(indata.pregunta11)
            pregunta12.setText(indata.pregunta12)
            pregunta13.setText(indata.pregunta13)
            pregunta21.setText(indata.pregunta21)
            pregunta22.setText(indata.pregunta22)
            pregunta23.setText(indata.pregunta23)
        }

        var logoutClick = findViewById<ImageView>(R.id.logoutImg)
        logoutClick.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        var backBtnClick = findViewById<Button>(R.id.back_equipamiento)
        backBtnClick.setOnClickListener {
            val intent = Intent(this, EquipamientoActivity::class.java)
            startActivity(intent)
        }

        var saveBtnClick = findViewById<Button>(R.id.save_academicos)
        saveBtnClick.setOnClickListener{
            var data = Academicos()
            data.pregunta11 = findViewById<EditText>(R.id.pregunta1_1_text).text.toString()
            data.pregunta12 = findViewById<EditText>(R.id.pregunta1_2_text).text.toString()
            data.pregunta13 = findViewById<EditText>(R.id.pregunta1_3_text).text.toString()
            data.pregunta21 = findViewById<EditText>(R.id.pregunta2_1_text).text.toString()
            data.pregunta22 = findViewById<EditText>(R.id.pregunta2_2_text).text.toString()
            data.pregunta23 = findViewById<EditText>(R.id.pregunta2_3_text).text.toString()
            var jsonValue = gson.toJson(data)
            Log.d("ANSWERS", jsonValue)
            createFile(filename, gson.toJson(data))
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