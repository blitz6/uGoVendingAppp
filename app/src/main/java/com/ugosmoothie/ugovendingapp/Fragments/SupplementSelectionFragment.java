package com.ugosmoothie.ugovendingapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ugosmoothie.ugovendingapp.Data.*;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;

/**
 * Created by Michelle on 3/14/2016
 */

public class SupplementSelectionFragment extends Fragment {

    private int no_supplement = 0;
    private int smashed_almonds = 1;
    private int pumpkin_seed_powder = 2;
    private int ground_flax_seed = 3;
    private int hemp_protein = 4;

    public int getNo_supplement(){
        return no_supplement;
    }
    public int getSmashed_almonds(){
        return smashed_almonds;
    }
    public int getPumpkin_seed_powder(){
        return pumpkin_seed_powder;
    }
    public int getGround_flax_seed(){
        return ground_flax_seed;
    }
    public int getHemp_protein(){
        return hemp_protein;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.supplement_selection_view, container, false);
        RelativeLayout supplement_n = (RelativeLayout) rootView.findViewById(R.id.nextTime_tag);
        RelativeLayout supplement_a = (RelativeLayout) rootView.findViewById(R.id.alm_tag);
        RelativeLayout supplement_p = (RelativeLayout) rootView.findViewById(R.id.pmkn_tag);
        RelativeLayout supplement_f = (RelativeLayout) rootView.findViewById(R.id.flx_tag);
        RelativeLayout supplement_h = (RelativeLayout) rootView.findViewById(R.id.hp_tag);
        final Button lang =  (Button) rootView.findViewById(R.id.lingual_tag);
        final Button previous =  (Button) rootView.findViewById(R.id.previous_tag);
        final Button ref =  (Button) rootView.findViewById(R.id.refresh_tag);
        final TextView selectedSmoothie = (TextView) rootView.findViewById(R.id.element_1_val);
        final ImageView smoothie = (ImageView) rootView.findViewById(R.id.smoothie_tag);
        final TextView selectedLiquid = (TextView) rootView.findViewById(R.id.element_2_val_2);
        final TextView totalval = (TextView) rootView.findViewById(R.id.element_4_val_2);
        final Button smoothie_switch = (Button) rootView.findViewById(R.id.smoothie_switch);
        final Button liquid_switch = (Button) rootView.findViewById(R.id.liquid_switch);

        //Updates the selected product on Summary bar
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
        }

        totalval.setText("$" + CurrentSelection.getInstance().getTotal());

        //Language Change
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).ToggleLanguage();
            }
        });

        //Previous fragment
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int na = -1;
                CurrentSelection.getInstance().setCurrentLiquid(na);
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
            }
        });

        //Refresh App
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).refresh();
            }
        });

        //Navigation on Summary bar
        smoothie_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(0);
            }
        });

        liquid_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
            }
        });

        //Supplement Selection: Save on data on remote cache and move to next fragment
        supplement_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSupplement(getNo_supplement());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(3);
            }
        });
        supplement_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSupplement(getSmashed_almonds());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(3);
            }
        });
        supplement_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSupplement(getPumpkin_seed_powder());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(3);
            }
        });
        supplement_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSupplement(getGround_flax_seed());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(3);
            }
        });
        supplement_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSupplement(getHemp_protein());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(3);
            }
        });

        return rootView;
    }

}
