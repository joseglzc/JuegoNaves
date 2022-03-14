package es.ideas.navecitasjagc.model;

public class Jugador {
    private int id;
    private String nombre;
    private int puntuacion;

    //Cosntructores
    public Jugador() {}

    public Jugador(String nombre, int puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public Jugador(int idJugador, String nombre, int puntuacion) {
        this.id = idJugador;
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }
    //-------------- GETTERS AND SETTERS-----------------
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getPuntuacion() {
        return puntuacion;
    }
    public void setPuntuacion(int puntuacion) {this.puntuacion = puntuacion;}
    public Jugador(String nombre) {this.nombre = nombre;}

    @Override
    public String toString() {
        return "Jugador: " +
                "id=" + id +
                ", nombre='" + nombre +
                ", puntuacion=" + puntuacion;
    }
}
