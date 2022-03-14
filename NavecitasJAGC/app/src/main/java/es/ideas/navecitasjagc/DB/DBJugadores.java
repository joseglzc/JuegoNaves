package es.ideas.navecitasjagc.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import es.ideas.navecitasjagc.model.Jugador;

public class DBJugadores extends DBHelper{

    Context cxt;
    //Constructor.
    public DBJugadores(@Nullable Context context) {
        super(context);
        this.cxt = context;
    }

    /**
     * Creación de un nuevo juegador en la base de datos;
     * @param jugador
     * @return idJugador
     */
    public long nuevoJugador(Jugador jugador) {
        long idJugador = 0;
        try {
            DBHelper dbHelper = new DBHelper(cxt);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //Asignar los valores a cada uno de los atributos de la clase Jugador.
            ContentValues values = new ContentValues();
            values.put("nombre", jugador.getNombre());
            values.put("puntuacion", jugador.getPuntuacion());

            //Insertar en la base de datos al nuevo jugador.
            idJugador = db.insert(T_JUGADORES, null, values);
        } catch (Exception ex) {ex.toString();}
        return idJugador;
    }

    /**
     * Devuelve una lista con todos los jugadores, la cuál ordenaremos en orden DESC,
     * por la puntuación de cada uno de los jugadores.
     * @param jugadores
     * @return lista con todos los jugadores.
     */
    public ArrayList<Jugador> consultaAllJugadores(ArrayList<Jugador> jugadores) {
        DBHelper dbHelper = new DBHelper(cxt);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Jugador jugador = null;
        Cursor c;
        c = db.rawQuery("SELECT * FROM " + T_JUGADORES+" ORDER BY puntuacion DESC" ,
                null);
        if (c.moveToFirst()) {
            do {
                jugador = new Jugador();
                jugador.setId(c.getInt(0));
                jugador.setNombre(c.getString(1));
                jugador.setPuntuacion(c.getInt(2));
                jugadores.add(jugador);
            }while (c.moveToNext());
        }
        c.close();
        return jugadores;
    }
}
