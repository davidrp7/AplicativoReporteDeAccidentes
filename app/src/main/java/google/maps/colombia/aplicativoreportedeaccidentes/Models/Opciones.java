package google.maps.colombia.aplicativoreportedeaccidentes.Models;

public class Opciones {


    public Opciones() {
    }

    public String nombre_opcion;

    public String getNombre_opcion() {
        return nombre_opcion;
    }

    public void setNombre_opcion(String nombre_opcion) {
        this.nombre_opcion = nombre_opcion;
    }

    public Opciones(String nombre_opcion) {
        this.nombre_opcion = nombre_opcion;
    }

    @Override
    public String toString() {
        return nombre_opcion;
    }
}
