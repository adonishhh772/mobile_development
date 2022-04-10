package uk.tees.b1162802.boro.features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context,"Airplane Mode enabled", Toast.LENGTH_LONG).show();

        throw new UnsupportedOperationException("Not yet implemented");
    }
}