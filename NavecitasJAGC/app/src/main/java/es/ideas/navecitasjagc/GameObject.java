package es.ideas.navecitasjagc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class GameObject {
    private Bitmap bitmap;
    private int x,y;

    /**
     * Inicializaremos los parámetros recibiendo la posición de
     * las coordenadas.
     * @param cxt context
     * @param x posición en el eje x
     * @param y posición en el eje y
     * @param id id
     */
    public GameObject(Context cxt, int x, int y, int id) {
        this.x=x;
        this.y=y;
        bitmap = BitmapFactory.decodeResource(cxt.getResources(), id);
    }

    public Bitmap getBitmap() {return bitmap;}
    public void setBitmap(Bitmap bitmap) {this.bitmap = bitmap;}
    public int getX() {return x; }
    public void setX(int x) {this.x = x;}
    public int getY() {return y;}
    public void setY(int y) {this.y = y;}
    abstract public void update();
}
