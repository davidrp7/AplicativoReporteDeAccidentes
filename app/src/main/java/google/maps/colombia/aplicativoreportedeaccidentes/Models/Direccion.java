package google.maps.colombia.aplicativoreportedeaccidentes.Models;

public class Direccion {

    public Direccion(){

    }

    private String direccion;

    public Direccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }



    @Override
    public String toString() {
        return direccion;
    }
}
