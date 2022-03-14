package es.ideas.navecitasjagc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import android.os.Handler;

public class PantallaCarga extends AppCompatActivity {

    LottieAnimationView lottie;
    TextView appname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);

        lottie = findViewById(R.id.lottie);
        appname = findViewById(R.id.appName);

        //está pantalla nos aparecerá cuando iniciemos la app, es un afnimación y justo al terminar
        //se nos abrira la app.

         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(PantallaCarga.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 8750);



    }
}
