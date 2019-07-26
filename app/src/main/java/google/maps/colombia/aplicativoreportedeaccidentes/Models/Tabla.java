package google.maps.colombia.aplicativoreportedeaccidentes.Models;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class Tabla {
    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla

    public Tabla(TableLayout tabla, Activity actividad) {
        this.tabla = tabla;
        this.actividad = actividad;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
    }
}
