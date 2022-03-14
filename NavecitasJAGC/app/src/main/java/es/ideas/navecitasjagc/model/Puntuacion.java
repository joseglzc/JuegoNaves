package es.ideas.navecitasjagc.model;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.ideas.navecitasjagc.DB.DBJugadores;
import es.ideas.navecitasjagc.R;
import es.ideas.navecitasjagc.adaptador.Adaptador;

public class Puntuacion extends AppCompatActivity implements View.OnClickListener{
    Button btnSalir;
    RecyclerView recyclerView;
    static ArrayList<Jugador> jugadores = new ArrayList<>();
    static Adaptador adaptador;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);
        //Instanciamos los objetos del layer.
        btnSalir = findViewById(R.id.btnSalir);
        recyclerView = findViewById(R.id.RecyclerView);
        btnSalir.setOnClickListener(this);
        //Instanciamos la base de datos.
        DBJugadores dbJugadores = new DBJugadores(this);
        //Cada vez que entramos actualizamos la lista, para que esté ordenada.
        jugadores.removeAll(jugadores);
        dbJugadores.consultaAllJugadores(jugadores);
        //Añadimos le adaptador al RecyclerView
        adaptador = new Adaptador(jugadores,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
