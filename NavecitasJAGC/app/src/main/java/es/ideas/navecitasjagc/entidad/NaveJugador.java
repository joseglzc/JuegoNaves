package es.ideas.navecitasjagc.entidad;

import android.content.Context;

import es.ideas.navecitasjagc.GameObject;
import es.ideas.navecitasjagc.R;

public class NaveJugador extends GameObject {
    int x_Max;
    boolean seleccionado;

    public NaveJugador (Context cxt, int anchoScreen, int altoScreen){
        super(cxt, anchoScreen/2, altoScreen/2, R.drawable.nave);
        x_Max = anchoScreen;
        setY(altoScreen-getAlto());
    }

    @Override
    public void setX(int x){
        if ((x<=(x_Max-getAncho())) && (x>=0)){
            super.setX(x);
        }
    }

    @Override
    public void update() {}

    //-------------GETTERS AND SETTERS---------------
    public int getAncho(){return getBitmap().getWidth();}
    public int getAlto(){return getBitmap().getHeight();}
    public boolean isSeleccionado(){return seleccionado;}
    public void setSeleccionado(boolean seleccionado){this.seleccionado=seleccionado;}
}
