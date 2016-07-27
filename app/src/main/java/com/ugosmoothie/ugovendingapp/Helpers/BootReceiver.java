//Preparation for Kiosk Mode

package com.ugosmoothie.ugovendingapp.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;

/**
 * Created by SachinApiah on 7/27/2016.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, PurchaseSmoothie.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}
