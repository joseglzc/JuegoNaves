package es.ideas.navecitasjagc.entidad;

import android.content.Context;

import es.ideas.navecitasjagc.GameObject;
import es.ideas.navecitasjagc.R;

public class Explosion extends GameObject {
    //Explosión con la posición y la imagen de la misma.
    public Explosion (Context context, int x, int y){
        super(context,x,y, R.drawable.explosion);
    }
    @Override
    public void update() {}
}
