package es.ideas.navecitasjagc.hilo;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Random;

import es.ideas.navecitasjagc.DB.DBJugadores;
import es.ideas.navecitasjagc.GameObject;
import es.ideas.navecitasjagc.MainActivity;
import es.ideas.navecitasjagc.model.Jugador;
import es.ideas.navecitasjagc.R;
import es.ideas.navecitasjagc.entidad.Explosion;
import es.ideas.navecitasjagc.entidad.Misil;
import es.ideas.navecitasjagc.entidad.NaveJugador;
import es.ideas.navecitasjagc.entidad.NaveMarciana;

public class Motor extends SurfaceView implements Runnable {

    static final int TIEMPO_MAX_COLISION = 35;
    volatile boolean isPlaying;
    Thread juegoHilo = null;
    Context cxt;
    int anchoScreen, altoScreen;
    ArrayList<GameObject> objetosJuego;
    boolean isGameOver;
    volatile boolean isColision;
    int score =0;
    SoundPool soundPool;
    private int tiempoColision;
    private final int tiempoMaxJuego;
    private int contadorTiempoJuego;
    private final int idExplosion;
    private final Paint paint;
    private final SurfaceHolder surfaceHolder;
    private Canvas canvas;
    static Jugador jugador;

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
        idExplosion = soundPool.load(cxt, R.raw.sonido_explosion, 0);
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            control();
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            juegoHilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        isGameOver=false;
        juegoHilo = new Thread(this);
        juegoHilo.start();
    }

    private void update() {
        --contadorTiempoJuego;
        if (contadorTiempoJuego == 0) { //Parar el juego
            isPlaying = false;
            isGameOver=true;

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
                    objetosJuego.remove(1);
                    objetosJuego.add(1, new NaveMarciana(cxt, new Random().nextInt(anchoScreen)
                            , 100, 50, anchoScreen));
                }
            }
            else {
                if (objetosJuego.size()>2){
                    if (Rect.intersects(((Misil)objetosJuego.get(2)).getColision(),((NaveMarciana)objetosJuego
                            .get(1)).getColision())){
                        int posXcolision,posYcolision, numObjetos;
                        score+=100;
                        isColision=true;
                        soundPool.play(idExplosion,1,1,0,0,1);
                        tiempoColision = TIEMPO_MAX_COLISION;
                        contadorTiempoJuego=tiempoMaxJuego;
                        posXcolision=objetosJuego.get(1).getX();
                        posYcolision=objetosJuego.get(1).getY();
                        numObjetos=objetosJuego.size();
                        for (int i= 1; i<numObjetos; i++){
                            objetosJuego.remove(1);
                        }
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

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


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
                        if (!isColision){
                            Misil misil = new Misil(cxt);
                            misil.setX(naveJugador.getX() + naveJugador.getAncho() / 2);
                            misil.setY(naveJugador.getY() - misil.getAlto());
                            misil.setVelocidad(50);
                            objetosJuego.add(misil);
                        }
                    }
                    break;
            }
            return true;
        }
    }

    public boolean isGameOver() {return isGameOver;}
}
