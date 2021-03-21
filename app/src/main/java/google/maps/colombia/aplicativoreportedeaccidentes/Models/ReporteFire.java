package google.maps.colombia.aplicativoreportedeaccidentes.Models;

public class ReporteFire {

    private String direccion;
    private String gravedadaccidente;
    private String tipoaccidente;

    public ReporteFire(){ }

    public ReporteFire(String direccion, String gravedadaccidente, String tipoaccidente){
        this.direccion = direccion;
        this.gravedadaccidente = gravedadaccidente;
        this.tipoaccidente = tipoaccidente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGravedadaccidente() {
        return gravedadaccidente;
    }

    public void setGravedadaccidente(String gravedadaccidente) {
        this.gravedadaccidente = gravedadaccidente;
    }

    public String getTipoaccidente() {
        return tipoaccidente;
    }

    public void setTipoaccidente(String tipoaccidente) {
        this.tipoaccidente = tipoaccidente;
    }
}
