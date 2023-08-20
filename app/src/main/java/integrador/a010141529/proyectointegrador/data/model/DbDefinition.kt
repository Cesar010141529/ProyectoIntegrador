package integrador.a010141529.proyectointegrador.data.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.io.Serializable

class TbcDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object TbcContract {
        // Table contents are grouped together in an anonymous object.
        object Users : BaseColumns {
            const val TABLE_NAME = "users"
            const val USERNAME = "username"
            const val PASSWORD = "password"
            const val CCT = "cct"
            const val IS_LOGGED = "is_logged"
        }

        object DatosInfraestructura : BaseColumns {
            const val TABLE_NAME = "infraestructura"
            const val CCT = "cct"
            const val PREGUNTA11 = "dato11"
            const val PREGUNTA12 = "dato12"
            const val PREGUNTA13 = "dato13"
            const val PREGUNTA21 = "dato21"
            const val PREGUNTA22 = "dato22"
            const val PREGUNTA23 = "dato23"
        }

        class Infraestructura : Serializable {
            var pregunta11: String = ""
            var pregunta12: String = ""
            var pregunta13: String = ""
            var pregunta21: String = ""
            var pregunta22: String = ""
            var pregunta23: String = ""
        }

        object DatosEquipamiento : BaseColumns {
            const val TABLE_NAME = "equipamiento"
            const val CCT = "cct"
            const val PREGUNTA11 = "dato11"
            const val PREGUNTA12 = "dato12"
            const val PREGUNTA13 = "dato13"
            const val PREGUNTA21 = "dato21"
            const val PREGUNTA22 = "dato22"
            const val PREGUNTA23 = "dato23"
        }

        class Equipamiento : Serializable {
            var pregunta11: String = ""
            var pregunta12: String = ""
            var pregunta13: String = ""
            var pregunta21: String = ""
            var pregunta22: String = ""
            var pregunta23: String = ""
        }

        object DatosAcademicos : BaseColumns {
            const val TABLE_NAME = "acedemicos"
            const val CCT = "cct"
            const val PREGUNTA11 = "dato11"
            const val PREGUNTA12 = "dato12"
            const val PREGUNTA13 = "dato13"
            const val PREGUNTA21 = "dato21"
            const val PREGUNTA22 = "dato22"
            const val PREGUNTA23 = "dato23"
        }

        class Academicos : Serializable {
            var pregunta11: String = ""
            var pregunta12: String = ""
            var pregunta13: String = ""
            var pregunta21: String = ""
            var pregunta22: String = ""
            var pregunta23: String = ""
        }
    }

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TbcContract.Users.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${TbcContract.Users.USERNAME} TEXT," +
                "${TbcContract.Users.IS_LOGGED} INTEGER default 0)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TbcContract.Users.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 5
        const val DATABASE_NAME = "tbc.db"
    }
}