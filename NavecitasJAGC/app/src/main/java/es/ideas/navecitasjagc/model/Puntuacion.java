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
        btnSalir = findViewById(R.id.btnSalir);
        recyclerView = findViewById(R.id.RecyclerView);
        btnSalir.setOnClickListener(this);
        DBJugadores dbJugadores = new DBJugadores(this);
        jugadores.removeAll(jugadores);
        dbJugadores.consultaAllJugadores(jugadores);
        adaptador = new Adaptador(jugadores,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
