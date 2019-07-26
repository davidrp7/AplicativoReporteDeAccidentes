
package google.maps.colombia.aplicativoreportedeaccidentes.Request;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistroRequest extends StringRequest{

    private static final String REGISTRO_REQUEST_URL="http://192.168.0.12/Register.php";

    private Map<String,String> params;

    public RegistroRequest(String user_nombre, String user_apellido, String user_correo, String user_contrasena, Response.Listener<String> listener){
        super(Method.POST,REGISTRO_REQUEST_URL,listener, null);
        params=new HashMap<>();
        params.put("user_nombre",user_nombre);
        params.put("user_apellido",user_apellido);
        params.put("user_correo",user_correo);
        params.put("user_contrasena",user_contrasena);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
