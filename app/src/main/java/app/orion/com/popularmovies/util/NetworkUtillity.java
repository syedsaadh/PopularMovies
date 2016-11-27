package app.orion.com.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by syedaamir on 27-11-2016.
 */

public class NetworkUtillity {
    private Context context;
    public NetworkUtillity(Context context){
        this.context = context;
    }
    public boolean isInternetAvailable(){
        boolean isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    public void showError(){
        Toast.makeText(context,"Ohh.. Internet Connection Lost",Toast.LENGTH_SHORT).show();
    }
}
