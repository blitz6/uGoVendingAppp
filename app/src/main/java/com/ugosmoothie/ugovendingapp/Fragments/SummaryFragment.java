package com.ugosmoothie.ugovendingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ugosmoothie.ugovendingapp.Data.CurrentSelection;
import com.ugosmoothie.ugovendingapp.Data.Purchase;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;
import com.ugosmoothie.ugovendingapp.WebServer.AsyncServer;

/**
 * Created by Michelle on 3/14/2016
 */
public class SummaryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.summary_view, container, false);
        final Button lang =  (Button) rootView.findViewById(R.id.lingual_tag);
        final Button previous =  (Button) rootView.findViewById(R.id.previous_tag);
        final Button ref =  (Button) rootView.findViewById(R.id.refresh_tag);
        final TextView selectedSmoothie = (TextView) rootView.findViewById(R.id.element_1_val);
        final ImageView smoothie = (ImageView) rootView.findViewById(R.id.smoothie_tag);
        final TextView selectedLiquid = (TextView) rootView.findViewById(R.id.element_2_val_3);
        final TextView selectedSupplement = (TextView) rootView.findViewById(R.id.element_3_val_3);
        final TextView totalval = (TextView) rootView.findViewById(R.id.element_4_val_3);
        final Button confirm_order = (Button) rootView.findViewById(R.id.confirm_tag);

        switch(CurrentSelection.getInstance().getCurrentSmoothie()) {
            case 0:{
                selectedSmoothie.setText(getContext().getResources().getString(R.string.smoothie_1));
                smoothie.setBackground(getContext().getResources().getDrawable(R.drawable.green_smoothie));
            }
                break;
            case 1:{
                selectedSmoothie.setText(getContext().getResources().getString(R.string.smoothie_2));
                smoothie.setBackground(getContext().getResources().getDrawable(R.drawable.pink_smoothie));
        }
                break;
            case 2:{
                selectedSmoothie.setText(getContext().getResources().getString(R.string.smoothie_3));
                smoothie.setBackground(getContext().getResources().getDrawable(R.drawable.purple_smoothie));
            }
                break;
            default:
                break;
        }

        switch(CurrentSelection.getInstance().getCurrentLiquid()) {
            case 0:{
                selectedLiquid.setText(getContext().getResources().getString(R.string.liquid_1));
            }
                break;
            case 1:{
                selectedLiquid.setText(getContext().getResources().getString(R.string.liquid_2));
            }
                break;
            case 2:{
                selectedLiquid.setText(getContext().getResources().getString(R.string.liquid_3));
            }
                break;
            default:
                break;
        }

        switch(CurrentSelection.getInstance().getCurrentSupplement()) {
            case 1:{
                selectedSupplement.setText(getContext().getResources().getString(R.string.supplement_1));
            }
                break;
            case 2:{
                selectedSupplement.setText(getContext().getResources().getString(R.string.supplement_2));
            }
                break;
            case 3:{
                selectedSupplement.setText(getContext().getResources().getString(R.string.supplement_3));
            }
                break;
            case 4:{
                selectedSupplement.setText(getContext().getResources().getString(R.string.supplement_4));
            }
                break;
            default:
                break;
        }

        totalval.setText("$" + CurrentSelection.getInstance().getTotal());


        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).ToggleLanguage();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int na = -1;
                CurrentSelection.getInstance().setCurrentSupplement(na);
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(2);
            }
        });

        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).refresh();
            }
        });

        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(4);
            }
        });


        return rootView;
    }

}