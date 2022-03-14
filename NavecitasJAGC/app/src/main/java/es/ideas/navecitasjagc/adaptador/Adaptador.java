package es.ideas.navecitasjagc.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.ideas.navecitasjagc.R;
import es.ideas.navecitasjagc.model.Jugador;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {
    ArrayList<Jugador> jugadores;
    Context cxt;

    //Constructor adaptdor
    public Adaptador(ArrayList<Jugador> objects, Context ctx) {
        this.jugadores = objects;
        this.cxt = ctx;
    }

    /**
     * Creación de cada uno de los items en el ViewHolde.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jugador, parent, false);
        return new ViewHolder(view);
    }


    /**
     * Se le dan los valores a cada uno de los elementos que tendremos en nuestros
     * items.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNombre.setText(jugadores.get(position).getNombre());
        holder.tvPuntuacion.setText("" + jugadores.get(position).getPuntuacion());

    }

    /**
     * Total de item en el RecyclerView
     * @return total items.
     */
    @Override
    public int getItemCount() {
        return jugadores.size();
    }

    /**
     * Creación del ViewHolder, en el que inicializaremos los valores que
     * tendrán nuestros items.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPuntuacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
            tvPuntuacion = (TextView) itemView.findViewById(R.id.tvPuntuacion);
        }
    }
}
