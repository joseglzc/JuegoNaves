package es.ideas.navecitasjagc.hilo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import es.ideas.navecitasjagc.DB.DBJugadores;
import es.ideas.navecitasjagc.GameObject;
import es.ideas.navecitasjagc.MainActivity;
import es.ideas.navecitasjagc.PantallaCarga;
import es.ideas.navecitasjagc.model.Jugador;
import es.ideas.navecitasjagc.R;
import es.ideas.navecitasjagc.entidad.Explosion;
import es.ideas.navecitasjagc.entidad.Misil;
import es.ideas.navecitasjagc.entidad.NaveJugador;
import es.ideas.navecitasjagc.entidad.NaveMarciana;
import es.ideas.navecitasjagc.model.Puntuacion;
import es.ideas.navecitasjagc.ui.GameMotor;

public class Motor extends SurfaceView implements Runnable {

    volatile boolean isPlaying;
    Thread juegoHilo = null;
    Context cxt;
    SoundPool soundPool;
    int idDisparo;
    int idGameOverSound;
    int anchoScreen, altoScreen;
    boolean isGameOver;
    volatile boolean isColision;
    int score =0;
    private int contadorTiempoJuego;
    private int tiempoColision;
    private Canvas canvas;
    private final int tiempoMaxJuego;
    private final int idExplosion;
    private final Paint paint;
    private final SurfaceHolder surfaceHolder;
    static final int TIEMPO_MAX_COLISION = 35;
    static Jugador jugador;
    ArrayList<GameObject> objetosJuego;

    public Motor(Context cxt, int x, int y, int tiempoJuego) {
        super(cxt);
        this.cxt = cxt;
        anchoScreen = x;
        altoScreen = y;

        surfaceHolder = getHolder();
        paint = new Paint();

        //Creación de los objetos del juego.
        objetosJuego = new ArrayList<GameObject>();
        objetosJuego.add(new NaveJugador(cxt, anchoScreen, altoScreen));
        objetosJuego.add(new NaveMarciana(
                cxt, new Random().nextInt(anchoScreen),
                75, 10, anchoScreen));

        //Inicializar parámetros de control de ejecución.
        tiempoMaxJuego = tiempoJuego;
        contadorTiempoJuego = tiempoJuego;
        isPlaying = true;
        isColision = false;
        isGameOver = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = (new SoundPool.Builder()).setMaxStreams(5).build();
        } else {
            new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        idExplosion = soundPool.load(cxt, R.raw.sonido_explosion, 1);
        idDisparo = soundPool.load(cxt, R.raw.sonido_disparo_laser, 1);
        idGameOverSound = soundPool.load(cxt, R.raw.sonido_game_over, 1);
    }

    /**
     * Mientras que esté jugando, el juego irá actualizando el canvas, dibujando y se
     * seguirá controlando la nave. Al ser un hilo necesita este método run();
     */
    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            control();
        }


    }

    /**
     * Persistencia de datos cuando la app se pone en pause.
     */
    public void pause() {
        isPlaying = false;
        try {
            juegoHilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Persistencia de datos cuando la app se pone en resume().
     */
    public void resume() {
        isPlaying = true;
        isGameOver=false;
        juegoHilo = new Thread(this);
        juegoHilo.start();
    }

    /**
     * Actualización de la pantalla, en la que se comprueba si el usuario
     * está o no jugando y si hay o no algún impacto del misil con la nave
     * enemiga; en caso en el que se produzca un impacto, se sumará puntuación
     * al score y se reseteará el tiempo.
     * En caso en el que se finalize el tiempo, el usuario perderá y se guardará
     * la puntuación del mismo en la base de datos SQLite.
     */
    private void update() {
        --contadorTiempoJuego;
        if (contadorTiempoJuego == 0) { //Parar el juego
            isPlaying = false;
            isGameOver=true;

            soundPool.play(idGameOverSound, 1, 1, 1, 0,1);

            //Al finalizar el juego, añadimos un nuevo jugador a la base de datos.
            Jugador j = new Jugador();
            j.setNombre(MainActivity.nombreJ);
            j.setPuntuacion(score);
            DBJugadores db = new DBJugadores(cxt);
            long id =db.nuevoJugador(j);

        } else {
            if (isColision) {
                tiempoColision -= 1;
                if ( tiempoColision == 0){
                    isColision = false;
                    objetosJuego.remove(1);//Eliminamos la nave marciana
                    //Añadimos una nueva nave marciana
                    objetosJuego.add(1, new NaveMarciana(cxt, new Random().nextInt(anchoScreen)
                            , 100, 50, anchoScreen));
                }
            }
            else {
                if (objetosJuego.size()>2){//Cuando haya misil
                    if (Rect.intersects(((Misil)objetosJuego.get(2)).getColision(),((NaveMarciana)objetosJuego
                            .get(1)).getColision())){
                        int posXcolision,posYcolision, numObjetos;
                        score+=100;
                        isColision=true;
                        soundPool.play(idExplosion,1,1,0,0,1);
                        tiempoColision = TIEMPO_MAX_COLISION;
                        contadorTiempoJuego+=150;
                        posXcolision=objetosJuego.get(1).getX();
                        posYcolision=objetosJuego.get(1).getY();
                        numObjetos=objetosJuego.size();
                        for (int i= 1; i<numObjetos; i++){
                            objetosJuego.remove(1);
                        }
                        //En caso en el que haya colisión
                        objetosJuego.add(new Explosion(cxt,posXcolision,posYcolision));
                    }else if(objetosJuego.get(2).getY()<0){
                        objetosJuego.remove(2);
                    }
                }
                for (GameObject objetoJuego : objetosJuego) {
                    objetoJuego.update();
                }
            }
        }
    }

    /**
     * Pinta el canvas dándole vida a nuestro juego, al mismo tiempo se encarga de actualizar
     * los textos del tiempo y puntuación.
     */
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            paint.setTextSize(70);
            canvas.drawText("Tiempo: "+Integer.toString(contadorTiempoJuego), 50,
                    70, paint);
            canvas.drawText("Pts: "+Integer.toString(score), 450,
                    70, paint);
            synchronized (objetosJuego) {
                for (GameObject o : objetosJuego) {
                    canvas.drawBitmap(o.getBitmap(), o.getX(), o.getY(), paint);
                }
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Tiempo en el que nuestra app está en sleep para así registrar los cambios de
     * posiciones de los elementos del canvas.
     */
    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Controla el movimiento de la nave, así mismo si la nave se mueve o si queremos
     * que la misma dispare un misil, dependiendo de la acción del usuario.
     * @param motionEvent
     * @return true.
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        NaveJugador naveJugador;
        synchronized (objetosJuego) {
            naveJugador = (NaveJugador) objetosJuego.get(0);
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case (MotionEvent.ACTION_DOWN):
                    if ((motionEvent.getY() > naveJugador.getY()) && (motionEvent.getX() >=
                            naveJugador.getX()) && (motionEvent.getX() <= (naveJugador.getX() + naveJugador.getAncho()))) {
                        naveJugador.setSeleccionado(true);
                    }
                    break;
                case (MotionEvent.ACTION_MOVE):
                    if (naveJugador.isSeleccionado()) naveJugador.setX((int) motionEvent.getX());
                    break;
                case (MotionEvent.ACTION_UP):
                    if (naveJugador.isSeleccionado()) {
                        naveJugador.setSeleccionado(false);
                            Misil misil = new Misil(cxt);
                            misil.setX(naveJugador.getX() + naveJugador.getAncho() / 2);
                            misil.setY(naveJugador.getY() - misil.getAlto());
                            misil.setVelocidad(50);
                            objetosJuego.add(misil);
                            soundPool.play(idDisparo, 1, 1, 1, 0,1);
                    }
                    break;
            }
            return true;
        }
    }

    public boolean isGameOver() {return isGameOver;}
}
