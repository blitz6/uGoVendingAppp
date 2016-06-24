package com.ugosmoothie.ugovendingapp.Fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ugosmoothie.ugovendingapp.Data.*;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;
import com.ugosmoothie.ugovendingapp.SmoothiePagerAdapter;

import java.util.Locale;



/**
 * Created by Michelle on 3/14/2016
 */
public class LiquidSelectionFragment extends Fragment {

    private int water = 0;
    private int coco_water = 1;
    private int almond_milk = 2;

    public int getWater(){
        return water;
    }

    public int getCoco_water() {
        return coco_water;
    }

    public int getAlmond_milk() {
        return almond_milk;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.liquid_selection_view, container, false);
        final Button lang =  (Button) rootView.findViewById(R.id.lingual_tag);
        final Button previous =  (Button) rootView.findViewById(R.id.previous_tag);
        final Button ref =  (Button) rootView.findViewById(R.id.refresh_tag);
        RelativeLayout liquid_w = (RelativeLayout) rootView.findViewById(R.id.water_tag);
        RelativeLayout liquid_c = (RelativeLayout) rootView.findViewById(R.id.cocowater_tag);
        RelativeLayout liquid_a = (RelativeLayout) rootView.findViewById(R.id.almondmilk_tag);
        final TextView selectedSmoothie = (TextView) rootView.findViewById(R.id.element_1_val);
        final ImageView smoothie = (ImageView) rootView.findViewById(R.id.smoothie_tag);
        final TextView totalval = (TextView) rootView.findViewById(R.id.element_4_val_1);
        final Button smoothie_switch = (Button) rootView.findViewById(R.id.smoothie_switch);

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

        totalval.setText("$" + CurrentSelection.getInstance().getTotal());

        lang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).ToggleLanguage();
            }
        });

        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).refresh();
            }
        });

        ref.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).refresh();
            }
        });

        smoothie_switch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(0);
            }
        });

        //Choosing Smoothie
        liquid_w.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentLiquid(getWater());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(2);
                //((PurchaseSmoothie)getActivity()).refresh_curr_frag();
            }
        });
        liquid_c.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentLiquid(getCoco_water());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(2);
                //((PurchaseSmoothie)getActivity()).refresh_curr_frag();
            }
        });
        liquid_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentLiquid(getAlmond_milk());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(2);
                //((PurchaseSmoothie)getActivity()).refresh_curr_frag();
            }
        });

        return rootView;
    }
}