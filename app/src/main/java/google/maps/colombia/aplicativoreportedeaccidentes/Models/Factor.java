package google.maps.colombia.aplicativoreportedeaccidentes.Models;

public class Factor {


    public Factor() {
    }

    public String nombre_factor;

    public Factor(String nombre_factor) {
        this.nombre_factor = nombre_factor;
    }


    public String getNombre_factor() {
        return nombre_factor;
    }

    public void setNombre_factor(String nombre_factor) {
        this.nombre_factor = nombre_factor;
    }

    @Override
    public String toString() {
        return nombre_factor;
    }

}
