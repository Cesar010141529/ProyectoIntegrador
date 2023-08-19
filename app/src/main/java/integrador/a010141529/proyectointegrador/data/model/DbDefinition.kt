package integrador.a010141529.proyectointegrador.data.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class TbcDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object TbcContract {
        // Table contents are grouped together in an anonymous object.
        object Users : BaseColumns {
            const val TABLE_NAME = "users"
            const val USERNAME = "user_name"
            const val PASSWORD = "password"
            const val LOGGED = "logged"
        }
    }

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TbcContract.Users.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${TbcContract.Users.USERNAME} TEXT," +
                "${TbcContract.Users.PASSWORD} TEXT," +
                "${TbcContract.Users.LOGGED} INTEGER default 0)"

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
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "tbc.db"
    }
}