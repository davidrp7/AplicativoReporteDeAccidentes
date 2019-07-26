package google.maps.colombia.aplicativoreportedeaccidentes.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PruebaRequest  extends StringRequest{

    private static final String PRUEBA_REQUEST_URL="http://192.168.0.12/Prueba.php";

    private Map<String,String> params;

    public PruebaRequest(String tipo_accidente, String grav_accidente, Response.Listener<String> listener){
        super(Method.POST,PRUEBA_REQUEST_URL,listener, null);
        params=new HashMap<>();
        params.put("tipo_accidente",tipo_accidente);
        params.put("grav_accidente",grav_accidente);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
