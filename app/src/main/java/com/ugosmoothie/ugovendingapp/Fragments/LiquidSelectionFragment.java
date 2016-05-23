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
import android.widget.RelativeLayout;
import com.ugosmoothie.ugovendingapp.Data.*;
import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;
import com.ugosmoothie.ugovendingapp.SmoothiePagerAdapter;

import java.util.Locale;



/**
 * Created by Michelle on 3/14/2016.
 */
public class LiquidSelectionFragment extends Fragment {

    private int water = 1;
    private int coco_water = 2;
    private int almond_milk = 3;

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
        RelativeLayout liquid_w = (RelativeLayout) rootView.findViewById(R.id.water_tag);
        RelativeLayout liquid_c = (RelativeLayout) rootView.findViewById(R.id.cocowater_tag);
        RelativeLayout liquid_a = (RelativeLayout) rootView.findViewById(R.id.almondmilk_tag);

        lang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PurchaseSmoothie) getActivity()).ToggleLanguage();
            }
        });

        //Choosing Smoothie
        liquid_w.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSmoothie(getWater());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(2);
            }
        });
        liquid_c.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSmoothie(getCoco_water());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(2);
            }
        });
        liquid_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSelection.getInstance().setCurrentSmoothie(getAlmond_milk());
                ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(2);
            }
        });


            /*checkDefaults();

            List<Smoothie> smoothies = Smoothie.listAll(Smoothie.class);

            final SmoothieArrayAdapter smoothieAdapter = new SmoothieArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, smoothies);

            listview.setAdapter(smoothieAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CurrentSelection.getInstance().setCurrentSmoothie(Smoothie.findById(Smoothie.class, id));
                    ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(1);
                }
            });

            return rootView;
        }*/

/*    private void checkDefaults() {
        if (Smoothie.listAll(Smoothie.class).size() == 0) {
            Smoothie greenMachine = new Smoothie("Green Machine", 0l, 0l, 5.00f);
            greenMachine.save();

            Smoothie berryLicious = new Smoothie("Berry-Licious", 0l, 0l, 5.00f);
            berryLicious.save();

            Smoothie tropicalParadise = new Smoothie("Tropical Paradise", 0l, 0l, 5.00f);
            tropicalParadise.save();
        }
    }*/
        return rootView;
    }
}