package es.ideas.navecitasjagc.entidad;

import android.content.Context;
import android.graphics.Rect;

import es.ideas.navecitasjagc.GameObject;
import es.ideas.navecitasjagc.R;

public class Misil extends GameObject {

    int velocidad;
    Rect colision;
    volatile boolean isColision;
    int score;
    //Cosntructor del misisil, con la posición del mismo.
    public Misil(Context context, int x, int y, int velocidad){
        super(context,x,y, R.drawable.misil);
        this.velocidad= velocidad;
        isColision=false;
        colision=new Rect(x,y,x+getAncho(),y+getAlto());

    }
    public Misil(Context context){
        this(context,0,0,0);
    }

    //Actualización de la posición del misil
    @Override
    public void update() {
        setY(getY()-velocidad);
        colision.left=getX();
        colision.right = getX()+getAncho();
        colision.top = getY();
        colision.bottom = getY() + getAlto();
    }

    //-------------GETTERS AND SETTERS---------------
    public void setVelocidad(int velocidad){
        this.velocidad = velocidad;
    }
    public int getAncho(){return getBitmap().getWidth();}
    public int getAlto(){return getBitmap().getHeight();}
    public Rect getColision(){return colision;}
}
