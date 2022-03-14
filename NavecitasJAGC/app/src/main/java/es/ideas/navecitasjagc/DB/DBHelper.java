package es.ideas.navecitasjagc.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    //Variables finales de la base de datos.
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NMOMBRE = "b_marcianitos.db";
    public static final String T_JUGADORES = "t_jugadores";

    //Constructor de la base de datos.
    public DBHelper(@Nullable Context context) {
        super(context, DB_NMOMBRE, null, DATABASE_VERSION);
    }

    /**
     * Creación de la base de datos SQLite, por medio de las variables
     * finales que tenemos en el principìo de la clase.
     * @param db base de datos.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_JUGADORES + "(" +
                "idJugador INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "puntuacion INTEGER NOT NULL)");
    }

    /**
     * Cuando actualcemos la base de datos, se eliminará
     * la existente y se creará de nuevo
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + T_JUGADORES); onCreate(db);
    }
}
