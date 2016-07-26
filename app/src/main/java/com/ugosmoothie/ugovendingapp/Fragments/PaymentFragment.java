package com.ugosmoothie.ugovendingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.payments.core.AndroidTerminal;
import com.payments.core.CoreSale;
import com.ugosmoothie.ugovendingapp.Data.CurrentSelection;
import com.ugosmoothie.ugovendingapp.Data.Purchase;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;
import com.ugosmoothie.ugovendingapp.WebServer.AsyncServer;

import java.math.BigDecimal;

/**
 * Created by Michelle on 3/18/2016.
 */
public class PaymentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


               //completeTransaction();
        return null;
    }

    public void completeTransaction() {
        // save the purchase to the database
        Purchase purchase = new Purchase(
                (long)CurrentSelection.getInstance().getCurrentSmoothie(),
                (long)CurrentSelection.getInstance().getCurrentLiquid(),
                (long)CurrentSelection.getInstance().getCurrentSupplement(),
                false,
                CurrentSelection.getInstance().getTotal()
                );
        purchase.save();

        // send the purchase to any listening clients
        AsyncServer.getInstance().SendMessage(purchase.toJSONObject());


      //      ((PurchaseSmoothie) getActivity()).refresh();

    }
}
