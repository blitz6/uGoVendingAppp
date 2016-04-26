package com.ugosmoothie.ugovendingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ugosmoothie.ugovendingapp.Data.CurrentSelection;
import com.ugosmoothie.ugovendingapp.Data.Purchase;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;
import com.ugosmoothie.ugovendingapp.WebServer.AsyncServer;

/**
 * Created by Michelle on 3/18/2016.
 */
public class PaymentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.payment_view, container, false);

        Button button = (Button) rootView.findViewById(R.id.emulatePaymentbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTransaction();
            }
        });

        return rootView;
    }

    public void completeTransaction() {
        // save the purchase to the database
        Purchase purchase = new Purchase(
                CurrentSelection.getInstance().getCurrentSmoothie().getId(),
                CurrentSelection.getInstance().getCurrentLiquid().getId(),
                CurrentSelection.getInstance().getCurrentSupplement().getId(),
                false,
                CurrentSelection.getInstance().getTotal()
                );
        purchase.save();

        // send the purchase to any listening clients
        AsyncServer.getInstance().SendMessage(purchase.toJSONObject());

        ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(5);
    }
}
