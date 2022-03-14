package es.ideas.navecitasjagc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.ideas.navecitasjagc.model.Puntuacion;
import es.ideas.navecitasjagc.ui.GameMotor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button inicioJuego, puntuaciones, salir;
    EditText nombre;
    public static String nombreJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicioJuego = findViewById(R.id.btnJugar);
        inicioJuego.setOnClickListener(this);

        puntuaciones = findViewById(R.id.btnPuntuacion);
        puntuaciones.setOnClickListener(this);

        salir = findViewById(R.id.btnSalir);
        salir.setOnClickListener(this);

        nombre = findViewById(R.id.nombreJugador);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnJugar:
                if (nombre.getText().toString().equals("")){
                    Toast.makeText(this, "Introudce un nombre!!", Toast.LENGTH_SHORT).show();
                }else{
                    nombreJ = nombre.getText().toString();
                    Intent i = new Intent(this, GameMotor.class);
                    startActivity(i);
                    finish();
                }
                break;
            case R.id.btnPuntuacion:
                Intent intent = new Intent(this, Puntuacion.class);
                startActivity(intent);
                break;
            case R.id.btnSalir:
                finish();
                break;
        }
    }
}