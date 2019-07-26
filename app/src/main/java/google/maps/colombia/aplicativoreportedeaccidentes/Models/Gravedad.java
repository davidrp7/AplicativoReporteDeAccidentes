package google.maps.colombia.aplicativoreportedeaccidentes.Models;

public class Gravedad {

    public Gravedad() {
    }

    public String nombre_gravedad;

    public Gravedad(String nombre_gravedad) {
        this.nombre_gravedad = nombre_gravedad;
    }

    public String getNombre_gravedad() {
        return nombre_gravedad;
    }

    public void setNombre_gravedad(String nombre_gravedad) {
        this.nombre_gravedad = nombre_gravedad;
    }

    @Override
    public String toString() {
        return nombre_gravedad;
    }
}
