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
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;

/**
 * Created by Michelle on 3/14/2016.
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
        final TextView selectedLiquid = (TextView) rootView.findViewById(R.id.element_2_val);
        final TextView selectedSupplement = (TextView) rootView.findViewById(R.id.element_3_val);
        final TextView totalval = (TextView) rootView.findViewById(R.id.element_4_val);

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

        switch(CurrentSelection.getInstance().getCurrentSmoothie()) {
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

        switch(CurrentSelection.getInstance().getCurrentSmoothie()) {
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

        /* TODO: Add button */
//        Button button = (Button) rootView.findViewById(R.id.confirmPurchaseButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                confirmPurchase();
//            }
//        });

        return rootView;
    }

    public void confirmPurchase() {

        ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(4);
    }
}