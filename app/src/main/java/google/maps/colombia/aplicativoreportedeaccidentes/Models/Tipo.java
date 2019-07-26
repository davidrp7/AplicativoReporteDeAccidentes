package google.maps.colombia.aplicativoreportedeaccidentes.Models;

public class Tipo {

    public Tipo() {
    }

    public String nombre_tipo;

    public Tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }

    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }

    @Override
    public String toString() {
        return nombre_tipo;
    }
}
