package es.ideas.navecitasjagc.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import es.ideas.navecitasjagc.MainActivity;
import es.ideas.navecitasjagc.R;
import es.ideas.navecitasjagc.hilo.Motor;

public class GameMotor extends AppCompatActivity {

    private Motor motor;
    final int tiempoDeJuegoMax = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_motor);

        //Indicar el tamaño de la pantalla.
        Display display = getWindowManager().getDefaultDisplay();
        Point tamayo = new Point();
        display.getSize(tamayo);

        motor = new Motor (this, tamayo.x, tamayo.y, tiempoDeJuegoMax);
        //Ocultar la barra y poner la pantalla en modo envolvente.
        View decorView= getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        setContentView(motor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity (new Intent(this, MainActivity.class));
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        motor.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        motor.resume();
    }
}