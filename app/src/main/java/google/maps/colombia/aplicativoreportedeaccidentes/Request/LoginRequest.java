package google.maps.colombia.aplicativoreportedeaccidentes.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL="http://192.168.0.12/Login.php";

    private Map<String,String> params;

    public LoginRequest(String user_correo, String user_contrasena, Response.Listener<String> listener){
        super(Method.POST,LOGIN_REQUEST_URL,listener, null);
        params=new HashMap<>();
        params.put("user_correo",user_correo);
        params.put("user_contrasena",user_contrasena);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
