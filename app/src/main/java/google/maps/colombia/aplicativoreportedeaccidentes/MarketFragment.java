package google.maps.colombia.aplicativoreportedeaccidentes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class MarketFragment extends DialogFragment {
    public static final String ARGUMENTO_TITLE = "TITLE";
    public static final String ARGUMENTO_FULL_SNIPPET = "FULL_SNIPPET";

    private String title;
    private String fullsnippet;

    public static MarketFragment newInstance(String title, String fullsnippet){
        MarketFragment fragment = new MarketFragment();
        Bundle b = new Bundle();
        b.putString(ARGUMENTO_TITLE, title);
        b.putString(ARGUMENTO_FULL_SNIPPET, fullsnippet);
        fragment.setArguments(b);
        return fragment;
    }

    public void onCreate(Bundle saveInstaceState) {
        super.onCreate(saveInstaceState);
        Bundle args = getArguments();

        title= args.getString(ARGUMENTO_TITLE);
        fullsnippet = args.getString(ARGUMENTO_FULL_SNIPPET);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(fullsnippet).create();
        return dialog;
    }
}
