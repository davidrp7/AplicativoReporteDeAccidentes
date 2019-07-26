package google.maps.colombia.aplicativoreportedeaccidentes.Models;

public class Reporte {

    public String gravedad_accidente;
    public String tipo_accidente;
    private float latitud;
    private float longitud;

    public Reporte(String reporte){

    }

    public Reporte(String gravedad_accidente, String tipo_accidente, float latitud, float longitud) {
        this.gravedad_accidente = gravedad_accidente;
        this.tipo_accidente = tipo_accidente;
        this.latitud = latitud;
        this.longitud = longitud;

    }

    public String getGravedad_accidente() {
        return gravedad_accidente;
    }

    public void setGravedad_accidente(String gravedad_accidente) {
        this.gravedad_accidente = gravedad_accidente;
    }

    public String getTipo_accidente() {
        return tipo_accidente;
    }

    public void setTipo_accidente(String tipo_accidente) {
        this.tipo_accidente = tipo_accidente;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }


}
